package racer.client.gui.entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

/**
 *
 * @author Piet
 */
public abstract class Entity {

    protected double x;

    protected double y;

    protected double dx = 0;

    protected double dy = 0;

    protected Color color;

    public Image buf;

    public Graphics gbuf;

    private int id, angle;

    public Entity(int x, int y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        synchronized (this) {
            return x;
        }
    }

    public void setXY(double x, double y) {
        synchronized (this) {
            this.x = x;
            this.y = y;
        }
    }

    public void setX(double x) {
        synchronized (this) {
            this.x = x;
        }
    }

    public double getY() {
        synchronized (this) {
            return y;
        }
    }

    public void setY(double y) {
        synchronized (this) {
            this.y = y;
        }
    }

    public void move(long delta) {
        synchronized (this) {
            moveX(delta);
            moveY(delta);
        }
    }

    public void moveX(long delta) {
        x += (delta * dx) / 1000;
    }

    public void moveY(long delta) {
        y += (delta * dy) / 1000;
    }

    public void setHorizontalMovement(double dx) {
        synchronized (this) {
            this.dx = dx;
        }
    }

    public void setVerticalMovement(double dy) {
        synchronized (this) {
            this.dy = dy;
        }
    }

    public double getHorizontalMovement() {
        synchronized (this) {
            return dx;
        }
    }

    public double getVerticalMovement() {
        synchronized (this) {
            return dy;
        }
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    public int getAngle() {
        return angle;
    }

    /**
     * tekent de Entity op de meegegeven Graphics context
     */
    public void draw(Graphics g) {
        synchronized (this) {
            g.setColor(color);
            g.fillRect((int) x, (int) y, 5, 5);
        }
    }

    public void drawPic(Graphics g, BufferedImage carpic) {
        synchronized (this) {
            g.drawImage(carpic, (int) x, (int) y, null);
        }
    }
}
