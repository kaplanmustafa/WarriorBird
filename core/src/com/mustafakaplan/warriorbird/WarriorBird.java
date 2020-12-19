package com.mustafakaplan.warriorbird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class WarriorBird extends ApplicationAdapter {

	SpriteBatch batch;
	Texture background;
	Texture bird;
	Texture bee1;
	Texture bee2;
	Texture bee3;
	Texture secondBee1;
	Texture secondBee2;
	Texture secondBee3;
	Texture ufo1;
	Texture ufo2;
	Texture ufo3;

	float birdX = 0;
	float birdY = 0;
	int gameState = 0;
	float velocity = 0;
	float gravity = 0.2f;
	float distance = 0;
	float enemyVelocity = 6;
	Random random;
	int score = 0;
	int scoredEnemy = 0;

	BitmapFont font;
	BitmapFont font2;
	FreeTypeFontGenerator fontGen;

	Music gameLoop;
	Music gameOver;
	Music gamePoint;
	Music gameWaiting;

	Circle birdCircle;

	int numberOfEnemies = 4;
	float [] enemyX = new float[numberOfEnemies];
	float [] enemyOffSet = new float[numberOfEnemies];
	float [] enemyOffSet2 = new float[numberOfEnemies];
	float [] enemyOffSet3 = new float[numberOfEnemies];

	Circle [] enemyCircles;
	Circle [] enemyCircles2;
	Circle [] enemyCircles3;

	@Override
	public void create () {

		batch = new SpriteBatch();
		background = new Texture("background.png");
		bird = new Texture("bird.png");
		bee1= new Texture("bee.png");
		bee2= new Texture("bee.png");
		bee3= new Texture("bee.png");
		secondBee1= new Texture("bee2.png");
		secondBee2= new Texture("bee2.png");
		secondBee3= new Texture("bee2.png");
		ufo1= new Texture("ufo.png");
		ufo2= new Texture("ufo.png");
		ufo3= new Texture("ufo.png");

		distance = Gdx.graphics.getWidth() / 2;
		random = new Random();

		birdX = Gdx.graphics.getWidth() / 2 - bird.getHeight() / 2;
		birdY = Gdx.graphics.getHeight() / 3;

		birdCircle = new Circle();
		enemyCircles = new Circle[numberOfEnemies];
		enemyCircles2 = new Circle[numberOfEnemies];
		enemyCircles3 = new Circle[numberOfEnemies];

		fontGen = new FreeTypeFontGenerator(Gdx.files.internal("rubikblack.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
		params.color = Color.BLACK;
		params.size = 80;
		params.characters = "0123456789 GameOver!Sco:TpPlyAgin.Wh";

		gameLoop = Gdx.audio.newMusic(Gdx.files.internal("game-loop.wav"));
		gameLoop.setLooping(true);

		gameOver = Gdx.audio.newMusic(Gdx.files.internal("game-over.wav"));
		gameOver.setLooping(false);

		gamePoint = Gdx.audio.newMusic(Gdx.files.internal("game-point.wav"));
		gamePoint.setLooping(false);

		gameWaiting = Gdx.audio.newMusic(Gdx.files.internal("game-waiting.mp3"));
		gameWaiting.setLooping(true);
		gameWaiting.play();

		font2 = fontGen.generateFont(params);
		params.color = Color.WHITE;
		font = fontGen.generateFont(params);

		for(int i = 0; i < numberOfEnemies; i++) {
			enemyOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
			enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

			enemyX[i] = Gdx.graphics.getWidth() - bee1.getWidth() / 2 + i * distance;

			enemyCircles[i] = new Circle();
			enemyCircles2[i] = new Circle();
			enemyCircles3[i] = new Circle();
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState == 1) {

			if(score == 0) {
				gameWaiting.stop();
				gameLoop.play();
			}

			if(enemyX[scoredEnemy] < birdX) {
				gamePoint.play();
				score++;

				if(scoredEnemy < numberOfEnemies - 1) {
					scoredEnemy++;
				} else {
					scoredEnemy = 0;
				}
			}

			if(Gdx.input.justTouched()) {
				velocity = -7;
			}

			for(int i = 0; i < numberOfEnemies; i++) {

				if(enemyX[i] < (0 - bee1.getWidth())) {
					enemyX[i] += numberOfEnemies * distance;

					enemyOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

				} else {
					enemyX[i] -= enemyVelocity;
				}

				if(score >= 0 && score <= 19) {
					batch.draw(bee1, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);
					batch.draw(bee2, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet2[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);

					if(score > 9) {
						if(score == 10 && i == 1) {
							enemyVelocity = 7;
						} else {
							batch.draw(bee3, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet3[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);
							if(score == 19 && i == 3) {
								//do nothing
							} else {
								enemyCircles3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet3[i] + Gdx.graphics.getHeight() / 18, Gdx.graphics.getWidth() / 30);
							}

						}
					}
					if(score == 19 && i == 3) {
						//do nothing
					} else {
						enemyCircles[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet[i] + Gdx.graphics.getHeight() / 18, Gdx.graphics.getWidth() / 30);
						enemyCircles2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet2[i] + Gdx.graphics.getHeight() / 18, Gdx.graphics.getWidth() / 30);
					}

				}
				else if(score >= 20 && score <= 39) {
					if(score == 20 && i == 3) {
						enemyVelocity = 8;
						batch.draw(bee1, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);
						batch.draw(bee2, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet2[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);
						batch.draw(bee3, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet3[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);
					} else {
						batch.draw(secondBee1, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);
						batch.draw(secondBee2, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet2[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);

						if(score > 29) {
							if(score == 30 && i == 1) {
								enemyVelocity = 9;
							} else {
								batch.draw(secondBee3, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet3[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);
								if(score == 39 && i == 3) {
									//do nothing
								} else {
									enemyCircles3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet3[i] + Gdx.graphics.getHeight() / 18, Gdx.graphics.getWidth() / 30);
								}
							}
						}

						if(score == 39 && i == 3) {
							//do nothing
						} else {
							enemyCircles[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet[i] + Gdx.graphics.getHeight() / 18, Gdx.graphics.getWidth() / 30);
							enemyCircles2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet2[i] + Gdx.graphics.getHeight() / 18, Gdx.graphics.getWidth() / 30);
						}
					}
				} else if(score >= 40) {
					if(score == 40 && i == 3) {
						enemyVelocity = 10;
						batch.draw(secondBee1, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);
						batch.draw(secondBee2, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet2[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);
						batch.draw(secondBee3, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet3[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);
					} else {
						batch.draw(ufo1, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);
						batch.draw(ufo2, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet2[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);

						if(score > 49) {
							if(score == 50 && i == 1) {
								enemyVelocity = 11;
							} else {
								batch.draw(ufo3, enemyX[i], Gdx.graphics.getHeight() / 2 + enemyOffSet3[i], Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);
								enemyCircles3[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet3[i] + Gdx.graphics.getHeight() / 18, Gdx.graphics.getWidth() / 30);
							}
						}

						enemyCircles[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet[i] + Gdx.graphics.getHeight() / 18, Gdx.graphics.getWidth() / 30);
						enemyCircles2[i] = new Circle(enemyX[i] + Gdx.graphics.getWidth() / 30, Gdx.graphics.getHeight() / 2 + enemyOffSet2[i] + Gdx.graphics.getHeight() / 18, Gdx.graphics.getWidth() / 30);
					}
				}

			}

			if(birdY > 0 && birdY < Gdx.graphics.getHeight()) {
				velocity += gravity;
				birdY -= velocity;
			} else {
				gameState = 2;
				gameLoop.stop();
				gameOver.play();
				gameWaiting.play();
			}
		} else if(gameState == 0) {

			font2.draw(batch, "Welcome To The Game!\nTap To Play.." , Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 3);

			if(Gdx.input.justTouched()) {
				gameState = 1;
			}

		} else if(gameState == 2) {

			font2.draw(batch, "Game Over!\nScore: " + String.valueOf(score) + "\nTap To Play Again..", Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 3);

			if(Gdx.input.justTouched()) {
				gameState = 1;
				gameLoop.play();
				birdY = Gdx.graphics.getHeight() / 3;

				for(int i = 0; i < numberOfEnemies; i++) {
					enemyOffSet[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet2[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);
					enemyOffSet3[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - 200);

					enemyX[i] = Gdx.graphics.getWidth() - bee1.getWidth() / 2 + i * distance;

					enemyCircles[i] = new Circle();
					enemyCircles2[i] = new Circle();
					enemyCircles3[i] = new Circle();
				}

				velocity = 0;
				scoredEnemy = 0;
				score = 0;
				enemyVelocity = 6;
			}
		}

		batch.draw(bird, birdX, birdY, Gdx.graphics.getWidth() / 15, Gdx.graphics.getHeight() / 9);

		font.draw(batch, String.valueOf(score), 100, 100);

		batch.end();

		birdCircle.set(birdX + Gdx.graphics.getWidth() / 30, birdY + Gdx.graphics.getHeight() / 18, Gdx.graphics.getWidth() / 35);

		for(int i = 0; i < numberOfEnemies; i++) {

			if(Intersector.overlaps(birdCircle, enemyCircles[i]) || Intersector.overlaps(birdCircle, enemyCircles2[i]) || Intersector.overlaps(birdCircle, enemyCircles3[i])) {
				if(gameState != 2) {
					gameLoop.stop();
					gameOver.play();
					gameWaiting.play();
				}
				gameState = 2;
			}

		}
	}
	
	@Override
	public void dispose () {
		fontGen.dispose();
		gameLoop.dispose();
		gameOver.dispose();
		gamePoint.dispose();
	}
}
