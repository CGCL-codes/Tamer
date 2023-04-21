package com.jlect.swebing.ui.client;

/**
 * Defines location of the component
 *
 * @author Sergey Kozmin
 * @since 06.12.2007 23:13:08
 */
public class Location {

    private int x;

    private int y;

    public Location(int x, int y) {
        setLocation(x, y);
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String toString() {
        return "Location{" + "x=" + x + ", y=" + y + '}';
    }
}
