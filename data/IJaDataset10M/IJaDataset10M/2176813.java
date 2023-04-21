package com.sun.spot.spotworld.gridview;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Polygon;

/**
 *
 * @author randy
 */
public class GVUtils {

    public GVUtils() {
    }

    public static void drawBezel(Graphics2D g2, AlphaComposite ca, int x, int y, int w, int h, int inset, Color cMiddle, Color cTop, Color cLeft, Color cRight, Color cBottom) {
        Composite c = g2.getComposite();
        g2.setComposite(ca);
        int[] xs = new int[4];
        int[] ys = new int[4];
        g2.setColor(cTop);
        xs[0] = x;
        ys[0] = y;
        xs[1] = x + w;
        ys[1] = y;
        xs[2] = x + w - inset;
        ys[2] = y + inset;
        xs[3] = x + inset;
        ys[3] = y + inset;
        g2.fill(new Polygon(xs, ys, 4));
        g2.setColor(cRight);
        xs[0] = x + w;
        ys[0] = y;
        xs[1] = x + w;
        ys[1] = y + h;
        xs[2] = x + w - inset;
        ys[2] = y + h - inset;
        xs[3] = x + w - inset;
        ys[3] = y + inset;
        g2.fill(new Polygon(xs, ys, 4));
        g2.setColor(cBottom);
        xs[0] = x;
        ys[0] = y + h;
        xs[1] = x + w;
        ys[1] = y + h;
        xs[2] = x + w - inset;
        ys[2] = y + h - inset;
        xs[3] = x + inset;
        ys[3] = y + h - inset;
        g2.fill(new Polygon(xs, ys, 4));
        g2.setColor(cLeft);
        xs[0] = x;
        ys[0] = y;
        xs[1] = x;
        ys[1] = y + h;
        xs[2] = x + inset;
        ys[2] = y + h - inset;
        xs[3] = x + inset;
        ys[3] = y + inset;
        g2.fill(new Polygon(xs, ys, 4));
        g2.setColor(cMiddle);
        g2.fillRect(x + inset, y + inset, w - (2 * inset), h - (2 * inset));
        g2.setComposite(c);
    }
}
