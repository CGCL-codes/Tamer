package com.calefay.exodusDefence;

public class EDScreenMode {

    private int width = 0;

    private int height = 0;

    public EDScreenMode(int w, int h) {
        width = w;
        height = h;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String toString() {
        String s = width + " x " + height;
        return s;
    }
}
