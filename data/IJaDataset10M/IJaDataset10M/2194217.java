package DZBall;

import javax.microedition.lcdui.game.*;
import javax.microedition.lcdui.*;
import java.io.*;

public class GameC extends GameCanvas implements Runnable {

    private boolean done;

    private String state;

    private int test;

    private int padPosX, padPosY, ballPosX, ballPosY, direccion, brickPosY;

    private int frameTime, score, tries;

    private Sprite walls, ball, pad, bg;

    private Sprite[][] bricks;

    private LayerManager lm;

    private final int rightBorder = getWidth() / 2 + 22 - 5, topBorder = getHeight() - 110 + 5, leftBorder = getWidth() / 2 - 58 + 5, bottomBorder = getHeight();

    private final int deltaPad = 3, brickCol = 5, brickRow = 6;

    private int deltaX = 3, deltaY = 3;

    public GameC() throws IOException {
        super(true);
        done = true;
        frameTime = 80;
        state = "GAME OVER";
        score = 0;
        tries = 3;
        padPosX = getWidth() / 2;
        padPosY = getHeight() - 10;
        ballPosX = getWidth() / 2;
        ballPosY = getHeight() - 50;
        brickPosY = 0;
        direccion = 0;
        lm = new LayerManager();
        walls = createWalls();
        ball = createBall();
        pad = createPad();
        bricks = createBricks();
        bg = createBg();
        lm.append(walls);
        lm.append(pad);
        lm.append(ball);
        for (int i = 0; i < bricks.length; i++) for (int j = 0; j < bricks[i].length; j++) lm.append(bricks[i][j]);
        lm.append(bg);
    }

    public Sprite getWalls() {
        return walls;
    }

    private Sprite createBg() {
        Image img = null;
        Sprite sprite = null;
        try {
            img = Image.createImage("/bg.png");
        } catch (IOException ioe) {
        }
        sprite = new Sprite(img, img.getWidth(), img.getHeight());
        sprite.defineReferencePixel(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setRefPixelPosition(getWidth() / 2, getHeight() - img.getHeight() / 2);
        sprite.setVisible(true);
        return sprite;
    }

    private Sprite createWalls() {
        Image img = null;
        Sprite sprite = null;
        try {
            img = Image.createImage("/frame.png");
        } catch (IOException ioe) {
        }
        sprite = new Sprite(img, img.getWidth(), img.getHeight());
        sprite.defineReferencePixel(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setRefPixelPosition(getWidth() / 2, getHeight() - img.getHeight() / 2);
        sprite.setVisible(true);
        return sprite;
    }

    private Sprite createBall() {
        Image img = null;
        Sprite sprite = null;
        try {
            img = Image.createImage("/ball.png");
        } catch (IOException ioe) {
        }
        sprite = new Sprite(img, img.getWidth(), img.getHeight());
        sprite.defineReferencePixel(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setVisible(true);
        return sprite;
    }

    private Sprite createPad() {
        Image img = null;
        Sprite sprite = null;
        try {
            img = Image.createImage("/paddle.png");
        } catch (IOException ioe) {
        }
        sprite = new Sprite(img, img.getWidth(), img.getHeight());
        sprite.defineReferencePixel(sprite.getWidth() / 2, sprite.getHeight() / 2);
        sprite.setVisible(true);
        return sprite;
    }

    private Sprite[][] createBricks() {
        int n = 0, m = brickPosY;
        Image img = null;
        Sprite[][] sprite = null;
        try {
            img = Image.createImage("/brick.png");
        } catch (IOException ioe) {
        }
        sprite = new Sprite[brickRow][brickCol];
        for (int i = 0; i < sprite.length; i++) {
            for (int j = 0; j < sprite[i].length; j++) {
                sprite[i][j] = new Sprite(img, 16, 6);
                sprite[i][j].defineReferencePixel(sprite[i][j].getWidth(), sprite[i][j].getHeight());
                sprite[i][j].setRefPixelPosition(3 + leftBorder + sprite[i][j].getWidth() / 2 + n, topBorder + m);
                sprite[i][j].setVisible(true);
                n += sprite[i][j].getWidth();
            }
            n = 0;
            m += sprite[0][0].getHeight();
        }
        return sprite;
    }

    private void animateBall() {
        test = tries;
        int deltaBallPad;
        if (direccion == 1) {
            ballPosX += deltaX;
            ballPosY -= deltaY;
        } else if (direccion == 2) {
            ballPosX -= deltaX;
            ballPosY -= deltaY;
        } else if (direccion == 3) {
            ballPosX -= deltaX;
            ballPosY += deltaY;
        } else if (direccion == 4) {
            ballPosX += deltaX;
            ballPosY += deltaY;
        } else if (direccion == 0) {
            resetBall();
        }
        if (direccion == 1 && ball.getRefPixelX() >= rightBorder) {
            direccion = 2;
        } else if (direccion == 1 && ball.getRefPixelY() <= topBorder) {
            direccion = 4;
        } else if (direccion == 2 && ball.getRefPixelX() <= leftBorder) {
            direccion = 1;
        } else if (direccion == 2 && ball.getRefPixelY() <= topBorder) {
            direccion = 3;
        } else if (direccion == 3 && ball.getRefPixelX() <= leftBorder) {
            direccion = 4;
        } else if (direccion == 3 && ball.getRefPixelY() >= bottomBorder) {
            resetBall();
            tries--;
        } else if (direccion == 4 && ball.getRefPixelX() >= rightBorder) {
            direccion = 3;
        } else if (direccion == 4 && ball.getRefPixelY() >= bottomBorder) {
            resetBall();
            tries--;
        }
        if (ball.collidesWith(pad, false)) if (direccion == 3) {
            direccion = 2;
            deltaBallPad = pad.getRefPixelX() - ball.getRefPixelX();
            if (deltaBallPad < 4 || deltaBallPad > 14) {
                deltaX = 3;
                deltaY = 2;
            } else {
                if (deltaBallPad < 7 || deltaBallPad > 11) {
                    deltaX = 3;
                    deltaY = 3;
                } else {
                    deltaX = 2;
                    deltaY = 3;
                }
            }
        } else if (direccion == 4) {
            direccion = 1;
            deltaBallPad = pad.getRefPixelX() - ball.getRefPixelX();
            if (deltaBallPad < 4 || deltaBallPad > 14) {
                deltaX = 3;
                deltaY = 2;
            } else {
                if (deltaBallPad < 7 || deltaBallPad > 11) {
                    deltaX = 3;
                    deltaY = 3;
                } else {
                    deltaX = 2;
                    deltaY = 3;
                }
            }
        }
        for (int i = 0; i < bricks.length; i++) for (int j = 0; j < bricks[i].length; j++) if (ball.collidesWith(bricks[i][j], true)) {
            if (direccion == 1) direccion = 4; else if (direccion == 2) direccion = 3; else if (direccion == 3) direccion = 2; else if (direccion == 4) direccion = 1;
            bricks[i][j].setVisible(false);
            score++;
            break;
        }
    }

    private void resetBall() {
        ballPosX = padPosX;
        ballPosY = padPosY - pad.getHeight() / 2 - ball.getHeight() / 2;
        direccion = 0;
    }

    void start() {
        done = false;
        new Thread(this).start();
    }

    public void run() {
        long start, end;
        int duration;
        Graphics g = getGraphics();
        while (!done) {
            start = System.currentTimeMillis();
            input();
            animateBall();
            ball.setRefPixelPosition(ballPosX, ballPosY);
            render(g);
            end = System.currentTimeMillis();
            duration = (int) (end - start);
            if (duration < frameTime) {
                try {
                    Thread.sleep(frameTime - duration);
                } catch (InterruptedException ie) {
                    done = true;
                }
            }
        }
    }

    public void input() {
        int keyStates = getKeyStates();
        if ((keyStates & LEFT_PRESSED) != 0) {
            if ((pad.getRefPixelX() - pad.getWidth() / 2) >= leftBorder) padPosX -= deltaPad;
        }
        if ((keyStates & RIGHT_PRESSED) != 0) {
            if ((pad.getRefPixelX() + pad.getWidth() / 2) <= rightBorder) padPosX += deltaPad;
        }
        if ((keyStates & UP_PRESSED) != 0) {
            if (direccion == 0) direccion = 1;
        }
    }

    public void render(Graphics g) {
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, getWidth(), getHeight());
        lm.paint(g, 0, 0);
        g.setColor(255, 255, 0);
        g.drawString("" + score, rightBorder + 18, bottomBorder - 25, 0);
        pad.setRefPixelPosition(padPosX, padPosY);
        if (tries <= 0 || score >= 30) {
            if (score >= 30) state = "YOU'VE WON!!";
            g.setColor(0, 0, 0);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.drawString(" " + tries, walls.getWidth() - 5, 3, 0);
            g.setColor(255, 255, 255);
            g.drawString(state, getWidth() / 4, getHeight() / 2, 0);
            g.drawString("Your score is:" + score, getWidth() / 4, getHeight() / 2 + 15, 0);
        }
        flushGraphics();
    }
}
