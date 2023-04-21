package org.bissa.weatherMonitor.ui.monitor.locationManager;

import java.awt.*;

public class Location {

    public Location(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public Location() {
    }

    private int x;

    private int y;

    private int number;

    private Color color;

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

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
