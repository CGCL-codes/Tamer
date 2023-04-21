package com.mapki.netdraw.gui.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import com.mapki.netdraw.Drawer;
import com.mapki.netdraw.gui.NetDrawable;
import com.mapki.netdraw.gui.NetDrawableInterface;

public class Resistor extends NetDrawable implements NetDrawableInterface {

    private int x1;

    private int y1;

    private int x2;

    private int y2;

    private int weight;

    private Color color;

    public Resistor() {
        this.x1 = 0;
        this.x2 = 0;
        this.y1 = 0;
        this.y2 = 0;
        this.weight = 0;
        this.setStroke(new BasicStroke(1.0f));
    }

    public Resistor(Color owner, Point point1, Point point2, int weight) {
        this.color = owner;
        this.x1 = point1.x;
        this.y1 = point1.y;
        this.x2 = point2.x;
        this.y2 = point2.y;
        this.weight = weight;
    }

    public String serialize() {
        return "RESISTOR{" + x1 + "," + y1 + "," + x2 + "," + y2 + "} WEIGHT{" + this.weight + "}";
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Stroke orig = g2d.getStroke();
        g2d.setStroke(this.stroke);
        double distance = Point.distance(x1, y1, x2, y2);
        double slope = 0;
        try {
            slope = (y2 - y1) / (x2 - x1);
        } catch (ArithmeticException ae) {
        }
        if (distance > 20) {
            Point resistorSpotOnLine = new Point((x2 - x1) / 2, (y2 - y1) / 2);
            g2d.drawLine(x1, y1, x1 + resistorSpotOnLine.x, y1 + resistorSpotOnLine.y);
        }
        g2d.setStroke(orig);
    }

    public String getName() {
        return "Resistor";
    }
}
