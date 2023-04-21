package math;

public class Point2D {

    private double x, y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D() {
        x = y = 0;
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

    public Point2D add(Point2D p) {
        return new Point2D(x + p.x, y + p.y);
    }

    public Point2D dec(Point2D p) {
        return new Point2D(x - p.x, y - p.y);
    }

    public void incBy(double x, double y) {
        this.x += x;
        this.y += y;
    }

    public double distanceTo(Point2D p) {
        return Math.sqrt(Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2));
    }

    public Point2D scale(double t) {
        return new Point2D(x * t, y * t);
    }

    public Point2D unitTowards(Point2D p) {
        Point2D v = p.dec(this);
        return v.scale(1.0 / v.length());
    }

    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public double angle(Point2D p) {
        double dx = p.x - x, dy = p.y - y;
        if (dx != 0) return Math.atan(dy / dx); else return Math.atan(dy / Double.MIN_VALUE);
    }

    public Point2D negate() {
        Point2D r = new Point2D(-x, -y);
        return r;
    }
}
