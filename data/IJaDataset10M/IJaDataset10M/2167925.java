package com.gallsoft.invaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.view.KeyEvent;

public class GameThread implements Runnable {

    static final int LEFT = 1;

    static final int RIGHT = 2;

    private static final float SHIP_VELOCITY = 100.0f;

    private final int PLAYER1_READY = 0;

    private final int PLAYER2_READY = 1;

    private final int PLAYER1_GAMEOVER_MSG = 2;

    private final int PLAYER2_GAMEOVER_MSG = 3;

    private final int GAME_OVER_MSG = 4;

    private final int PLAYING = 1;

    private final int MESSAGING = 2;

    private final int DIED = 4;

    private final int GAMEOVER = 8;

    private final int NEW_LEVEL = 16;

    private InvaderRow[] invaderRow;

    private Sprite bullet, saucer, shipExp, invExp, bulletExp, saucerExp, bombExp, bulletExp2;

    private Sprite background;

    private Message messages, saucerMsg;

    private Ship ship;

    private LivesSprite lives;

    private Score scoreP1, scoreP2, highScore, score;

    private Sprite[] bomb;

    private Base[] bases;

    private long mLastTime;

    private int mViewWidth;

    private int mViewHeight;

    private float invaderTime, saucerTime, explTime, shipExplTime, bombExplTime, readyTime, msgTime;

    private float invaderSpeed, invSpeedP1, invSpeedP2, startDelay;

    private boolean invaderBomb;

    private int bombType = 0;

    private int shipDir = 0;

    private int saucerDir = 0;

    private ScoreSystem invScore;

    private boolean gameOver, gameOverPlayer1, gameOverPlayer2, showMessage, showReady, invaded, doDelay;

    private int lastScore, currentPlayer, saucerScore;

    private GameState player1State, player2State;

    private GameActivity mActivity;

    private int currentMessage, numPlayers;

    private byte mState;

    private SoundManager soundEffects;

    private int invSoundIndex, saucerIndex, numInvaders;

    private boolean soundOn;

    public GameThread(Context activity, int players, int high) {
        numPlayers = players;
        invScore = new ScoreSystem(numPlayers, high);
        player1State = new GameState();
        player2State = new GameState();
        mActivity = (GameActivity) activity;
    }

    public void init(SoundManager sounds, int sndChoice) {
        player2State.save(invaderRow, bases);
        if (sndChoice == 1) soundOn = true; else soundOn = false;
        score = scoreP1;
        initGame();
        gameOver = false;
        gameOverPlayer1 = false;
        gameOverPlayer2 = false;
        invaderBomb = false;
        showMessage = false;
        showReady = false;
        currentPlayer = 1;
        invSoundIndex = 1;
        invaderSpeed = 1.0f;
        invSpeedP1 = 1.0f;
        invSpeedP2 = 1.0f;
        numInvaders = 50;
        if (numPlayers == 2) {
            showReadyMessage();
            showMessage = true;
            mState = MESSAGING;
        } else mState = PLAYING;
        soundEffects = sounds;
        highScore.setScore(invScore.getHigh());
    }

    public void run() {
        final long time = SystemClock.uptimeMillis();
        final long timeDelta = time - mLastTime;
        final float timeDeltaSeconds = mLastTime > 0.0f ? timeDelta / 1000.0f : 0.0f;
        mLastTime = time;
        saucerTime += timeDeltaSeconds;
        if (mState == MESSAGING) {
            if (showMessage) {
                readyTime += timeDeltaSeconds;
                if (readyTime > 3.0f) {
                    readyTime = 0.0f;
                    hideMessage(currentMessage);
                }
            } else if (gameOver) mState = GAMEOVER; else if (invScore.gameOver) {
                messages.showMessage(GAME_OVER_MSG, 100, 80);
                showMessage = true;
                gameOver = true;
            } else if (!showReady) {
                restartGame();
                showReadyMessage();
            } else {
                mState = PLAYING;
                showReady = false;
            }
        } else if (mState == NEW_LEVEL) {
            newLevel();
            mState = PLAYING;
        } else if (mState == GAMEOVER) {
            finishGame();
        } else if (mState == DIED) {
            shipExplTime += timeDeltaSeconds;
            if (shipExplTime > 1.5f) {
                shipExp.isAlive = false;
                shipExplTime = 0.0f;
                for (int i = 1; i <= 8; i++) soundEffects.stopSound(i);
                updateGameState();
                mState = MESSAGING;
            }
        } else if (mState == PLAYING) {
            if (doDelay) {
                startDelay += timeDeltaSeconds;
                if (startDelay > 2.0f) {
                    ship.isAlive = true;
                    invaderBomb = false;
                    doDelay = false;
                } else {
                    ship.isAlive = false;
                    invaderBomb = true;
                }
            }
            if (ship.isAlive) {
                moveShip(timeDeltaSeconds);
            }
            if (invExp.isAlive) {
                explTime += timeDeltaSeconds;
                if (explTime > 0.2f) {
                    invExp.isAlive = false;
                    explTime = 0.0f;
                }
            } else if (saucerExp.isAlive) {
                explTime += timeDeltaSeconds;
                if (explTime > 0.2f) {
                    saucerExp.isAlive = false;
                    explTime = 0.0f;
                    saucerMsg.showMessage(saucerIndex, saucer.x, saucer.y);
                }
            } else if (saucerMsg.isAlive) {
                msgTime += timeDeltaSeconds;
                if (msgTime > 0.5f) {
                    msgTime = 0.0f;
                    saucerMsg.hideMessage(saucerIndex);
                }
            } else if (bulletExp.isAlive) {
                explTime += timeDeltaSeconds;
                if (explTime > 0.2f) {
                    bulletExp.isAlive = false;
                    explTime = 0.0f;
                }
            } else if (bulletExp2.isAlive) {
                explTime += timeDeltaSeconds;
                if (explTime > 0.2f) {
                    bulletExp2.isAlive = false;
                    explTime = 0.0f;
                }
            }
            if (bombExp.isAlive) {
                bombExplTime += timeDeltaSeconds;
                if (bombExplTime > 0.2f) {
                    bombExp.isAlive = false;
                    bombExplTime = 0.0f;
                }
            }
            moveInvaders(timeDeltaSeconds);
            moveSaucer(timeDeltaSeconds);
            if (invScore.getScore() != lastScore) {
                score.setScore(invScore.getScore());
                lastScore = invScore.getScore();
                if (invScore.isHighScore()) highScore.setScore(invScore.getScore());
            }
        }
    }

    private void showReadyMessage() {
        background.isAlive = true;
        if (currentPlayer == 1) currentMessage = PLAYER1_READY; else currentMessage = PLAYER2_READY;
        ;
        messages.showMessage(currentMessage, 80, 180);
        showMessage = true;
        showReady = true;
    }

    private void hideMessage(int messageNum) {
        background.isAlive = false;
        messages.hideMessage(messageNum);
        showMessage = false;
    }

    private void moveShip(float timeDeltaSeconds) {
        Sprite hitInvader = null;
        int i = 0;
        int points = 0;
        if ((shipDir == LEFT) && (ship.x > 0)) {
            ship.velocityX = -SHIP_VELOCITY;
        } else if ((shipDir == RIGHT) && (ship.x) < (mViewWidth - ship.width)) {
            ship.velocityX = SHIP_VELOCITY;
        } else {
            ship.velocityX = 0;
        }
        ship.x = ship.x + (ship.velocityX * timeDeltaSeconds);
        if (ship.x < 0) ship.x = 0; else if (ship.x > (mViewWidth - ship.width)) ship.x = (mViewWidth - ship.width);
        if (bullet.isAlive) {
            bullet.y = bullet.y + (bullet.velocityY * timeDeltaSeconds);
            if (bullet.hasCollided(bases)) {
                Base base = (Base) bullet.getCollisionSprite();
                if (base.addDamage(bullet)) {
                    bullet.isAlive = false;
                }
            } else if (bullet.hasCollided(bomb[bombType])) {
                bullet.isAlive = false;
                bomb[bombType].isAlive = false;
                invaderBomb = false;
                bulletExp2.isAlive = true;
                bulletExp2.x = bullet.x;
                bulletExp2.y = bullet.y;
            } else {
                while ((hitInvader == null) && (i < invaderRow.length)) {
                    hitInvader = invaderRow[i++].checkCollision(bullet);
                }
                if (i == 1) points = 30; else if (i < 4) points = 20; else points = 10;
                if (hitInvader != null) {
                    hitInvader.isAlive = false;
                    bullet.isAlive = false;
                    invExp.isAlive = true;
                    invExp.x = hitInvader.x;
                    invExp.y = hitInvader.y;
                    invScore.updateScore(points);
                    if (soundOn) soundEffects.playSound(2);
                    invaderSpeed -= 0.02f;
                    --numInvaders;
                    if (numInvaders == 0) mState = NEW_LEVEL;
                } else if (saucer.isAlive && bullet.hasCollided(saucer)) {
                    soundEffects.stopSound(4);
                    bullet.isAlive = false;
                    saucer.isAlive = false;
                    saucerTime = 0;
                    saucerExp.isAlive = true;
                    saucerExp.x = saucer.x;
                    saucerExp.y = saucer.y;
                    invScore.updateScore(saucerScore);
                } else if (bullet.y <= 50) {
                    bullet.isAlive = false;
                    bulletExp.isAlive = true;
                    bulletExp.x = bullet.x;
                    bulletExp.y = bullet.y;
                }
            }
        }
    }

    private void moveSaucer(float timeDeltaSeconds) {
        if (saucerTime < 30) return;
        if (!saucer.isAlive) {
            saucerIndex = (int) (Math.random() * 6);
            saucerScore = (saucerIndex + 1) * 50;
            saucerDir = (int) (Math.random() * 2);
            if (saucerDir == 0) {
                saucer.x = 0;
                saucer.y = 60;
                saucer.velocityX = 75;
            } else {
                saucer.x = mViewWidth;
                saucer.y = 60;
                saucer.velocityX = -75;
            }
            saucer.isAlive = true;
            if (soundOn) soundEffects.playSound(4);
        }
        saucer.x = saucer.x + (saucer.velocityX * timeDeltaSeconds);
        if ((saucerDir == 0 && saucer.x > mViewWidth) || (saucerDir == 1 && saucer.x + saucer.width < 0)) {
            saucer.isAlive = false;
            saucerTime = 0;
        }
    }

    /**
     * Moves the invaders. Called before drawing
     */
    private void moveInvaders(float timeDeltaSeconds) {
        Sprite theInvader;
        float highestXPos = 0;
        float lowestXPos = 50000;
        float currentXPos = 0;
        int invBomber, invColIndex, invRowIndex, numBombers, bomberIndex;
        int[] bottomInvader = new int[8];
        boolean changeDirection = false;
        Sprite[] bombInvader;
        if (!invaderBomb) {
            invaderBomb = true;
            numBombers = 0;
            for (invColIndex = 0; invColIndex < 8; invColIndex++) {
                for (invRowIndex = 4; invRowIndex >= 0; --invRowIndex) {
                    if (invaderRow[invRowIndex].getInvader(invColIndex).isAlive) {
                        bottomInvader[invColIndex] = invRowIndex;
                        numBombers++;
                        break;
                    } else bottomInvader[invColIndex] = -1;
                }
            }
            if (numBombers == 0) return;
            bombInvader = new Sprite[numBombers];
            bomberIndex = 0;
            for (int i = 0; i < 8; i++) {
                if (bottomInvader[i] != -1) bombInvader[bomberIndex++] = invaderRow[bottomInvader[i]].getInvader(i);
            }
            invBomber = (int) (Math.random() * numBombers);
            bombType = (int) (Math.random() * 3);
            bomb[bombType].x = bombInvader[invBomber].x + 10;
            bomb[bombType].y = bombInvader[invBomber].y + 10;
            bomb[bombType].velocityY = 150;
            bomb[bombType].isAlive = true;
        } else {
            bomb[bombType].y = bomb[bombType].y + (bomb[bombType].velocityY * timeDeltaSeconds);
            if (bomb[bombType].hasCollided(ship) && ship.isAlive) {
                ship.isAlive = false;
                bomb[bombType].isAlive = false;
                invaderBomb = false;
                shipExp.isAlive = true;
                shipExp.x = ship.x;
                shipExp.y = ship.y;
                mState = DIED;
                if (soundOn) soundEffects.playSound(3);
            } else if (bomb[bombType].hasCollided(bases)) {
                Base base = (Base) bomb[bombType].getCollisionSprite();
                if (base.addDamage(bomb[bombType])) {
                    bomb[bombType].isAlive = false;
                    invaderBomb = false;
                    bombExp.x = bomb[bombType].x;
                    bombExp.y = bomb[bombType].y;
                    bombExp.isAlive = true;
                }
            }
            if (bomb[bombType].isAlive) {
                if (bomb[bombType].y >= mViewHeight - 82) {
                    bomb[bombType].y = mViewHeight - 82;
                    bomb[bombType].isAlive = false;
                    invaderBomb = false;
                    bombExp.x = bomb[bombType].x;
                    bombExp.y = bomb[bombType].y;
                    bombExp.isAlive = true;
                }
            }
        }
        invaderTime += timeDeltaSeconds;
        if ((invaderTime < invaderSpeed) || mState == DIED) {
            animationOff();
            return;
        }
        invaderTime = 0;
        if (invaderRow[0].movingRight) {
            for (int i = 0; i < invaderRow.length; i++) {
                invaderRow[i].setAnimationOn();
                theInvader = invaderRow[i].getLastInvader();
                currentXPos = theInvader.x;
                if (currentXPos > highestXPos) highestXPos = currentXPos;
            }
            if ((highestXPos + 20) >= mViewWidth) changeDirection = true;
        } else {
            for (int i = 0; i < invaderRow.length; i++) {
                invaderRow[i].setAnimationOn();
                theInvader = invaderRow[i].getFirstInvader();
                currentXPos = theInvader.x;
                if (currentXPos < lowestXPos) lowestXPos = currentXPos;
            }
            if (lowestXPos <= 5) changeDirection = true;
        }
        for (int i = 0; i < invaderRow.length; i++) {
            invaded = invaderRow[i].moveRow(changeDirection);
        }
        if (invaded) {
            ship.isAlive = false;
            bomb[bombType].isAlive = false;
            invaderBomb = false;
            shipExp.isAlive = true;
            shipExp.x = ship.x;
            shipExp.y = ship.y;
            mState = DIED;
            if (soundOn) soundEffects.playSound(3);
            animationOff();
            return;
        }
        if (soundOn) soundEffects.playSound(invSoundIndex + 4);
        if (++invSoundIndex > 4) invSoundIndex = 1;
    }

    public void addInvaders(InvaderRow[] invRow, Sprite bomb1Sprite, Sprite bomb2Sprite, Sprite bomb3Sprite, Sprite invExpSprite) {
        invaderRow = invRow;
        bomb = new Sprite[3];
        bomb[0] = bomb1Sprite;
        bomb[1] = bomb2Sprite;
        bomb[2] = bomb3Sprite;
        invExp = invExpSprite;
    }

    public void addShip(Ship shipSprite, Sprite bulletSprite, Sprite shipExpSprite, Sprite bulletExpSprite, Sprite bulletExp2Sprite) {
        ship = shipSprite;
        bullet = bulletSprite;
        shipExp = shipExpSprite;
        bulletExp = bulletExpSprite;
        bulletExp2 = bulletExp2Sprite;
    }

    public void addSaucer(Sprite saucerSprite, Sprite saucerExpSprite, Message saucerScores) {
        saucer = saucerSprite;
        saucerExp = saucerExpSprite;
        saucerMsg = saucerScores;
    }

    public void addBases(Base[] baseSprites, Sprite bombExpSprite) {
        bases = new Base[baseSprites.length];
        for (int i = 0; i < baseSprites.length; i++) {
            bases[i] = baseSprites[i];
        }
        bombExp = bombExpSprite;
    }

    public void addScore(Score scoreP1Sprite, Score scoreP2Sprite, Score highSprite, Bitmap[] numbersImage) {
        scoreP1 = scoreP1Sprite;
        scoreP2 = scoreP2Sprite;
        highScore = highSprite;
    }

    public void addMessages(Sprite bgSprite, Message messageSprite, LivesSprite livesSprite) {
        background = bgSprite;
        messages = messageSprite;
        lives = livesSprite;
    }

    public void setViewSize(int width, int height) {
        mViewHeight = height;
        mViewWidth = width;
    }

    public boolean doKeyDown(int keyCode, KeyEvent msg) {
        boolean handled = false;
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_Q) {
            shipDir = LEFT;
            handled = true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_W) {
            shipDir = RIGHT;
            handled = true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (!bullet.isAlive) {
                bullet.isAlive = true;
                bullet.x = ship.getBulletX();
                bullet.y = ship.getBulletY();
                bullet.velocityY = -200;
                handled = true;
                if (soundOn) soundEffects.playSound(1);
            }
        }
        return handled;
    }

    /**
     * Handles a key-up event.
     * 
     * @param keyCode the key that was pressed
     * @param msg the original event object
     * @return true if the key was handled and consumed, or else false
     */
    boolean doKeyUp(int keyCode, KeyEvent msg) {
        boolean handled = false;
        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_Q || keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_W) {
            shipDir = 0;
            handled = true;
        }
        return handled;
    }

    private void updateGameState() {
        if (currentPlayer == 1) {
            player1State.save(invaderRow, bases);
            invSpeedP1 = invaderSpeed;
        } else if (currentPlayer == 2) {
            player2State.save(invaderRow, bases);
            invSpeedP2 = invaderSpeed;
        }
        currentPlayer = invScore.playerDied(invaded);
        if (invScore.gameOverPlayer1 && !gameOverPlayer1) {
            currentMessage = PLAYER1_GAMEOVER_MSG;
            gameOverPlayer1 = true;
            messages.showMessage(currentMessage, 50, 80);
            showMessage = true;
        } else if (invScore.gameOverPlayer2 && !gameOverPlayer2) {
            currentMessage = PLAYER2_GAMEOVER_MSG;
            gameOverPlayer2 = true;
            messages.showMessage(currentMessage, 50, 80);
            showMessage = true;
        } else if (invScore.gameOver) {
            return;
        }
    }

    private void restartGame() {
        if (currentPlayer == 1) {
            player1State.getState(invaderRow, bases);
            score = scoreP1;
            invaderSpeed = invSpeedP1;
        } else if (currentPlayer == 2) {
            player2State.getState(invaderRow, bases);
            score = scoreP2;
            invaderSpeed = invSpeedP2;
        }
        lives.setLives(invScore.getLives());
        initGame();
    }

    private void newLevel() {
        for (int i = 1; i <= 8; i++) soundEffects.stopSound(i);
        invScore.updateLevel();
        float advance = invScore.getLevel() * 20.0f;
        if (advance > 60) advance = 60;
        for (int i = 0; i < invaderRow.length; i++) {
            invaderRow[i].resetRow(advance);
        }
        for (int i = 0; i < bases.length; i++) {
            bases[i].resetBase();
        }
        invaderSpeed = 1.0f;
        numInvaders = 50;
        initGame();
    }

    private void initGame() {
        invaderBomb = false;
        bullet.isAlive = false;
        saucer.isAlive = false;
        for (int i = 0; i < 3; i++) {
            bomb[i].isAlive = false;
        }
        shipExplTime = 0.0f;
        readyTime = 0.0f;
        explTime = 0.0f;
        bombExplTime = 0.0f;
        saucerTime = 0.0f;
        invaderTime = 0.0f;
        msgTime = 0.0f;
        startDelay = 0.0f;
        invaderBomb = false;
        bullet.isAlive = false;
        ship.isAlive = true;
        ship.x = ship.startX;
        ship.y = ship.startY;
        invSoundIndex = 1;
        doDelay = true;
    }

    private void animationOff() {
        for (int i = 0; i < invaderRow.length; i++) {
            invaderRow[i].setAnimationOff();
        }
    }

    private void finishGame() {
        mActivity.gameOver(invScore.getHigh());
        mActivity.finish();
    }
}
