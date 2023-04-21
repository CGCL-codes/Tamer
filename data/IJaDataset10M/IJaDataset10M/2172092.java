package org.grailrtls.client.gui.swing.location;

import java.awt.Graphics;

public class RectangleLocation extends Location {

    protected double x2;

    protected double y2;

    protected double score;

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public void paint(Graphics g, int height, float objScale, float xScale, float yScale) {
    }
}
