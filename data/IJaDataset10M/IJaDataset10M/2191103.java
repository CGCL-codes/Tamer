package org.grailrtls.util.delaunay;

public class Circle {

    threeDPoint center;

    double radius;

    Circle() {
        this.center = new threeDPoint();
        this.radius = 0;
    }

    Circle(threeDPoint c, double r) {
        this.center = c;
        this.radius = r;
    }

    public threeDPoint getCenter() {
        return this.center;
    }

    public double getRadius() {
        return this.radius;
    }

    public boolean isInside(threeDPoint p) {
        if (this.center.distanceSquare(p) < this.radius * this.radius) return true;
        return false;
    }

    public boolean computeCircumCircle(threeDPoint p1, threeDPoint p2, threeDPoint p3) {
        double cp;
        cp = Vector.crossProduct(p1, p2, p3);
        if (cp != 0) {
            double p1Sq, p2Sq, p3Sq;
            double num;
            double cx, cy;
            p1Sq = p1.getX() * p1.getX() + p1.getY() * p1.getY();
            p2Sq = p2.getX() * p2.getX() + p2.getY() * p2.getY();
            p3Sq = p3.getX() * p3.getX() + p3.getY() * p3.getY();
            num = p1Sq * (p2.getY() - p3.getY()) + p2Sq * (p3.getY() - p1.getY()) + p3Sq * (p1.getY() - p2.getY());
            cx = num / (2 * cp);
            num = p1Sq * (p3.getX() - p2.getX()) + p2Sq * (p1.getX() - p3.getX()) + p3Sq * (p2.getX() - p1.getX());
            cy = num / (2 * cp);
            this.center.setPoint(cx, cy, 0);
            this.radius = this.center.distance(p1);
            return true;
        }
        System.out.println("The three points are colinear");
        return false;
    }
}
