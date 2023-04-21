package com.nepxion.cots.twaver.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class TGraphGridBackground extends TGraphBackground {

    private int width = 650;

    private int height = 450;

    private int widthCount = 30;

    private int heightCount = 40;

    private int startX = 105;

    private int startY = 295;

    private double angle = 30 * Math.PI / 180;

    public TGraphGridBackground(Color color, Color gradientColor) {
        super(color, gradientColor);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidthCount() {
        return widthCount;
    }

    public void setWidthCount(int widthCount) {
        this.widthCount = widthCount;
    }

    public int getHeightCount() {
        return heightCount;
    }

    public void setHeightCount(int heightCount) {
        this.heightCount = heightCount;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public void paintContent(Graphics2D g, double zoom, Rectangle viewportRect) {
        super.paintContent(g, zoom, viewportRect);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(getGradientColor());
        double xGap = width / (widthCount - 1.0);
        double xHSpace = xGap * Math.cos(angle);
        double xVSpace = xGap * Math.sin(angle);
        double xEndX = startX + height * Math.cos(angle);
        double xEndY = startY - height * Math.sin(angle);
        double out = 10;
        double outx = out * Math.cos(angle);
        double outy = out * Math.sin(angle);
        for (int i = 0; i < widthCount; i++) {
            double sx = startX + xHSpace * i - outx;
            double sy = startY + xVSpace * i + outy;
            double ex = xEndX + xHSpace * i + outx;
            double ey = xEndY + xVSpace * i - outy;
            Line2D.Double line = new Line2D.Double(sx, sy, ex, ey);
            g2d.draw(line);
        }
        double yGap = height / (heightCount - 1.0);
        double yHSpace = yGap * Math.cos(angle);
        double yVSpace = yGap * Math.sin(angle);
        double yEndX = startX + width * Math.cos(angle);
        double yEndY = startY + width * Math.sin(angle);
        for (int i = 0; i < heightCount; i++) {
            double sx = startX + yHSpace * i - outx;
            double sy = startY - yVSpace * i - outy;
            double ex = yEndX + +yHSpace * i + outx;
            double ey = yEndY - yVSpace * i + outy;
            Line2D.Double line = new Line2D.Double(sx, sy, ex, ey);
            g2d.draw(line);
        }
    }
}
