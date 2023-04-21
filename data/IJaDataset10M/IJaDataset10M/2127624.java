package com.tiny.core.game;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * An object of the game
 * 
 * @author Erick Zanardo
 * 
 */
public abstract class GameObject {

    public static final int COLLIDING_ABOVE = 0;

    public static final int COLLIDING_RIGHT = 1;

    public static final int COLLIDING_BELOW = 2;

    public static final int COLLIDING_LEFT = 3;

    protected Rectangle2D.Double pos;

    private int energy;

    public GameObject[] collidingEntities;

    public GameObject(int x, int y, int width, int height) {
        pos = new Rectangle2D.Double(x, y, width, height);
        this.energy = 1;
        collidingEntities = new GameObject[4];
    }

    public Rectangle2D.Double getPos() {
        return pos;
    }

    public void setPos(Rectangle2D.Double pos) {
        this.pos = pos;
    }

    /**
     * "Flag" para representar o estado do objeto
     * 
     * @return
     */
    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void fixCollidingPosition() {
        GameObject o;
        if ((o = collidingEntities[COLLIDING_BELOW]) != null) {
            pos.y = (o.pos.y - pos.height) + 1;
        }
        if ((o = collidingEntities[COLLIDING_RIGHT]) != null) {
            pos.x = (o.pos.x - pos.width) + 1;
        }
        if ((o = collidingEntities[COLLIDING_ABOVE]) != null) {
            pos.y = (o.pos.y + o.pos.height) + 1;
        }
    }

    public GameObject getCollingRight() {
        return collidingEntities[COLLIDING_RIGHT];
    }

    public GameObject getCollingLeft() {
        return collidingEntities[COLLIDING_LEFT];
    }

    public GameObject getCollingAbove() {
        return collidingEntities[COLLIDING_ABOVE];
    }

    public GameObject getCollingBelow() {
        return collidingEntities[COLLIDING_BELOW];
    }

    public boolean isCollingRight() {
        return collidingEntities[COLLIDING_RIGHT] != null;
    }

    public boolean isCollingLeft() {
        return collidingEntities[COLLIDING_LEFT] != null;
    }

    public boolean isCollingAbove() {
        return collidingEntities[COLLIDING_ABOVE] != null;
    }

    public boolean isCollingBelow() {
        return collidingEntities[COLLIDING_BELOW] != null;
    }

    /**
     * Retorna true quando algum lado est� colidindo
     * 
     * @return
     */
    public boolean isColling() {
        return isCollingAbove() || isCollingBelow() || isCollingLeft() || isCollingRight();
    }

    /**
     * Retorno o primeiro objeto que achar colidindo, s� � util quando se tem certeza que apenas um objeto esta colidindo
     * 
     * @return
     */
    public GameObject getColling() {
        for (GameObject o : collidingEntities) {
            if (o != null) {
                return o;
            }
        }
        return null;
    }

    public abstract void update(int currentFrame);

    public abstract void render(Graphics2D g);
}
