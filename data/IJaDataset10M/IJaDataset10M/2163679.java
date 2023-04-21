package es.iiia.shapegrammar.utils;

import java.awt.geom.Point2D;

public class Vector {

    public double x, y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(double x1, double y1, double x2, double y2) {
        x = x2 - x1;
        y = y2 - y1;
    }

    public Vector(Point2D pointA, Point2D pointB) {
        if (MathUtils.compare(pointA, pointB) == -1) {
            x = pointB.getX() - pointA.getX();
            y = pointB.getY() - pointA.getY();
        } else {
            x = pointA.getX() - pointB.getX();
            y = pointA.getY() - pointB.getY();
        }
    }

    public double dot(Vector vect) {
        return x * vect.x + y * vect.y;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }
}
