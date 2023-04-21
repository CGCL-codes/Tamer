package org.jazzteam.edu.lang.applet.roundIO;

import java.awt.Graphics;

public class Round {

    private int rx;

    private int ry;

    public int getRx() {
        return rx;
    }

    public void setRx(int rx) {
        this.rx = rx;
    }

    public int getRy() {
        return ry;
    }

    public void setRy(int ry) {
        this.ry = ry;
    }

    public void draw(Graphics gr, int x, int y) {
        rx = x;
        ry = y;
        gr.drawOval(x, y, 20, 20);
    }

    public void repaint(Graphics gr) {
        gr.drawOval(rx, ry, 20, 20);
    }
}
