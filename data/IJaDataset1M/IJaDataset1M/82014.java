package com.iver.utiles.console;

import java.awt.Color;

public class ConsoleStyle {

    private int start = 0;

    private int stop = 0;

    private Color color = Color.black;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getStop() {
        return stop;
    }

    public void setStop(int stop) {
        this.stop = stop;
    }
}
