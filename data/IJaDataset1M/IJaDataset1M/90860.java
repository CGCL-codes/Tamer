package net.openalp.graph;

/**
 * Author: Adam Scarr
 * Date: 03/04/2009
 * Time: 8:05:49 PM
 */
public class Force {

    private float x;

    private float y;

    public Force(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void add(Force that) {
        this.x += that.x;
        this.y += that.y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
