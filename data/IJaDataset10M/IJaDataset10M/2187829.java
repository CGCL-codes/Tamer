package com.walter.util;

public class Point2D {

    private double x, y;

    public Point2D() {
        this.setX(0);
        this.setY(0);
    }

    public Point2D(double arg) {
        this.setX(arg);
        this.setY(arg);
    }

    public Point2D(double x, double y) {
        this.setX(x);
        this.setY(y);
    }

    public Point2D mult(Point2D p, double a) {
        Point2D temp;
        temp = new Point2D();
        temp.setX(a * p.getX());
        temp.setY(a * p.getY());
        return temp;
    }

    public Point2D add(Point2D p) {
        Point2D temp;
        temp = new Point2D();
        temp.setX(p.getX() + this.getX());
        temp.setY(p.getY() + this.getY());
        return temp;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
