package br.upe.dsc.poli.pacman.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class PacMan implements IItem, Moveble {

    public static final int PACMAN_TYPE_1 = -1;

    public static final int PACMAN_TYPE_2 = -2;

    public static final int PACMAN_TYPE_3 = -3;

    public static final int PACMAN_TYPE_4 = -4;

    public static final int STATE_PLAYING = -88;

    public static final int STATE_WIN = -77;

    public static final int STATE_GAME_OVER = -66;

    public static final int MOVE_LEFT = 200;

    public static final int MOVE_RIGHT = 20;

    public static final int MOVE_TOP = 110;

    public static final int MOVE_BOTTOM = 290;

    private int angle;

    private int x;

    private int y;

    private int dx;

    private int dy;

    private int circle = 320;

    private ArrayList<UnitWall> walls;

    private ArrayList<UnitWall> tablets;

    private int score = 0;

    private int state = STATE_PLAYING;

    private ImageIcon image;

    private Color pacManColor = Color.CYAN;

    private IGameState gameState;

    public PacMan(int type, IGameState gameState, int initialX, int initialY) {
        this.gameState = gameState;
        this.x = initialX;
        this.y = initialY;
        this.changeMovePosition(MOVE_RIGHT);
        if (type == PACMAN_TYPE_1) {
            image = new ImageIcon("res/julio1515.gif");
        } else if (type == PACMAN_TYPE_2) {
            image = new ImageIcon("res/cris1515.gif");
        } else if (type == PACMAN_TYPE_3) {
            image = new ImageIcon("res/nelson1515.gif");
        } else if (type == PACMAN_TYPE_4) {
            image = new ImageIcon("res/marcelo1515.gif");
        } else {
            throw new IllegalArgumentException("PacMan com tipo indefinido");
        }
    }

    public void paint(Graphics g) {
        this.image.paintIcon(null, g, this.x * POSITION_WIDTH, this.y * POSITION_HEIGHT);
    }

    public void changeMovePosition(int movePosition) {
        this.angle = movePosition;
        switch(movePosition) {
            case MOVE_LEFT:
                this.dx = -1;
                this.dy = 0;
                break;
            case MOVE_RIGHT:
                this.dx = 1;
                this.dy = 0;
                break;
            case MOVE_TOP:
                this.dx = 0;
                this.dy = -1;
                break;
            case MOVE_BOTTOM:
                this.dx = 0;
                this.dy = 1;
                break;
        }
    }

    public void notifyPulse() {
        if (this.circle == 360) {
            this.circle = 320;
        } else {
            this.circle = 360;
        }
        if (!verifyColision(this.x + this.dx, this.y + this.dy)) {
            this.x += this.dx;
            this.y += this.dy;
        }
        this.verifyEat(this.x, this.y);
        if (this.score == this.tablets.size()) {
        }
    }

    public void setWall(ArrayList<UnitWall> items) {
        this.walls = items;
    }

    public void setTablets(ArrayList<UnitWall> tablets) {
        this.tablets = tablets;
    }

    public int getScore() {
        return score;
    }

    private boolean verifyColision(int newX, int newY) {
        boolean result = false;
        for (int i = 0; i < this.walls.size(); i++) {
            UnitWall uw = this.walls.get(i);
            if (uw.hasColisionX(newX, ELEMENT_WIDTH) && uw.hasColisionY(newY, ELEMENT_HEIGHT)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean verifyEat(int newX, int newY) {
        boolean result = false;
        for (int i = 0; i < this.tablets.size(); i++) {
            Tablet t = (Tablet) this.tablets.get(i);
            if (t.hasColisionX(newX, ELEMENT_WIDTH) && t.hasColisionY(newY, ELEMENT_HEIGHT)) {
                if (t.eat()) {
                    this.score++;
                }
                this.tablets.remove(i);
                System.out.println("Size:= " + this.tablets.size());
                break;
            }
        }
        if (this.tablets.size() == 0) {
            this.gameState.win();
        }
        return result;
    }

    final int getState() {
        return state;
    }

    public boolean hasColisionX(int initialX, int width) {
        boolean result = false;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < ELEMENT_WIDTH; j++) {
                if ((i + initialX) == (j + this.x)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public boolean hasColisionY(int initialY, int height) {
        boolean result = false;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < ELEMENT_HEIGHT; j++) {
                if ((i + initialY) == (j + this.y)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
