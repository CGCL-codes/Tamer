package com.arajuuri.catinvaders;

import com.arajuuri.catinvaders.fileoperations.Images;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;

public abstract class GraphicalObjectAdapter implements GraphicalObject {

    protected Image img;

    protected int x = 0;

    protected int y = 0;

    protected int startX = 0;

    protected int startY = 0;

    protected int speed = 0;

    protected int gravity = 0;

    protected boolean hidden = false;

    protected boolean left = false;

    protected boolean right = false;

    protected boolean up = false;

    protected boolean down = false;

    protected final Images images = new Images();

    public GraphicalObjectAdapter() {
    }

    public void draw(Graphics2D g2d) {
    }

    public void addSpeed(int s) {
        speed += s;
    }

    public void addGravity(int g) {
        gravity += g;
    }

    public void setToStart() {
        this.x = startX;
        this.y = startY;
    }

    public void move() {
    }

    public void hide() {
        hidden = true;
    }

    public void show() {
        hidden = false;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getHeight() {
        return this.img.getHeight(null);
    }

    public boolean detectCollision(int x, int y) {
        return false;
    }

    public void keyPressEvent(KeyEvent e) {
    }

    public void keyReleaseEvent(KeyEvent e) {
    }
}
