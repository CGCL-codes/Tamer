package com.adamldavis.fluid3.drops;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.geom.Point2D.Float;
import java.util.Random;

/**
 * @author Adam Davis
 * @since Jan 23, 2007
 */
public class DropSponge extends DropCatcher {

    float fraction;

    float max;

    float negativity;

    Float position;

    float size;

    public DropSponge(Color kolor, Float position, float size, float negativity, float max) {
        super(createPoly(50, (int) size, 0), kolor, position);
        this.size = size;
        this.negativity = negativity;
        this.position = position;
        this.max = max;
    }

    public static Polygon createPoly(int n, int generalSize, int variation) {
        Random rnd = new Random();
        int[] xx = new int[n];
        int[] yy = new int[n];
        for (int i = 0; i < n; i++) {
            xx[i] = (int) (Math.sin(((double) i) * 2d * Math.PI / n) * generalSize) + (variation == 0 ? 0 : rnd.nextInt(variation));
            yy[i] = (int) (Math.cos(((double) i) * 2d * Math.PI / n) * generalSize) + (variation == 0 ? 0 : rnd.nextInt(variation));
        }
        return new Polygon(xx, yy, n);
    }

    public void catchDrop(Drop d) {
        if (d.color.equals(kolor)) {
            if (!isFull()) {
                count++;
            }
        } else {
            fraction -= negativity;
            int n = (int) fraction;
            if (count > 0) {
                count += n;
            }
            fraction -= n;
        }
    }

    public void draw(Graphics g) {
        int fill = (int) (size * (count + fraction) / max);
        g.setColor(Color.black);
        g.drawPolygon(polygon);
        g.setColor(drawKolor);
        g.fillOval((int) position.x - fill, (int) position.y - fill, 2 * fill, 2 * fill);
    }

    public boolean isFull() {
        return ((count + fraction) >= max);
    }
}
