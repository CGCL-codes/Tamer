package com.jah0wl.catan.utils;

import javax.microedition.lcdui.Graphics;
import java.lang.InterruptedException;
import com.jah0wl.utils.*;

public class Dado {

    private static final int[] VALUE_MASKS = { 0, 8, 20, 28, 85, 93, 119 };

    private static final int[] POINT_MASKS = { 0, 1, 2, 4, 8, 16, 32, 64 };

    private Point location;

    private int[] valor = new int[2];

    public Dado(Point p) {
        this(p.x, p.y);
    }

    public Dado(int x, int y) {
        location = new Point(x, y);
        valor[0] = 0;
        valor[1] = 0;
    }

    public Point getLocation() {
        return location.getLocation();
    }

    public int getValor() {
        return valor[0] + valor[1];
    }

    public void setLocation(Point p) {
        setLocation(p.x, p.y);
    }

    public void setLocation(int x, int y) {
        location.setLocation(x, y);
    }

    public void changeValor() {
        valor[0] = Random.random(6);
        valor[1] = Random.random(6);
    }

    public void paintOne(Graphics g, int x, int y, int n) {
        Point[] POINT_COORS = { null, new Point(x + 2, y + 2), new Point(x + 2, y + 4), new Point(x + 2, y + 6), new Point(x + 4, y + 4), new Point(x + 6, y + 2), new Point(x + 6, y + 4), new Point(x + 6, y + 6) };
        g.setColor(255, 255, 255);
        g.fillRect(x + 1, y + 1, 7, 7);
        g.setColor(0, 0, 0);
        g.drawRect(x, y, 8, 8);
        for (int i = 1; i <= 7; i++) if ((VALUE_MASKS[valor[n]] & POINT_MASKS[i]) == POINT_MASKS[i]) g.drawLine(POINT_COORS[i].x, POINT_COORS[i].y, POINT_COORS[i].x, POINT_COORS[i].y);
    }

    public void paint(Graphics g) {
        int x = getLocation().x;
        int y = getLocation().y;
        paintOne(g, x, y, 0);
        paintOne(g, x + 10, y, 1);
    }
}
