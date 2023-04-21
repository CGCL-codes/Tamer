package org.evertree.themolition.physics;

public class Rectangle {

    public double x;

    public double y;

    public double width;

    public double height;

    public double x_;

    public double y_;

    public double centerX;

    public double centerY;

    public Rectangle(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.x_ = x + width - 1;
        this.y_ = y + height - 1;
        this.centerX = x + width / 2;
        this.centerY = y + height / 2;
    }

    public Rectangle(Point upperLeft, double width, double height) {
        this(upperLeft.x, upperLeft.y, width, height);
    }

    public boolean intersects(Rectangle other) {
        double tw = this.width;
        double th = this.height;
        double ow = other.width;
        double oh = other.height;
        if (ow <= 0 || oh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        double tx = this.x;
        double ty = this.y;
        double ox = other.x;
        double oy = other.y;
        ow += ox;
        oh += oy;
        tw += tx;
        th += ty;
        return ((ow < ox || ow > tx) && (oh < oy || oh > ty) && (tw < tx || tw > ox) && (th < ty || th > oy));
    }

    @Override
    public String toString() {
        return "[x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + "]";
    }
}
