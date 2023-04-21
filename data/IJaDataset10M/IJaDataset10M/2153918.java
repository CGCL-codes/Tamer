package org.apache.lucene.spatial.geometry.shape;

/**
 * Rectangle shape.  
 */
public class Rectangle implements Geometry2D {

    private Point2D ptMin, ptMax;

    public Rectangle() {
        ptMin = new Point2D(-1, 1);
        ptMax = new Point2D(1, 1);
    }

    public Rectangle(Point2D ptMin, Point2D ptMax) {
        this.ptMin = new Point2D(ptMin);
        this.ptMax = new Point2D(ptMax);
    }

    public Rectangle(double x1, double y1, double x2, double y2) {
        set(x1, y1, x2, y2);
    }

    @Override
    public String toString() {
        return "[" + ptMin + "," + ptMax + "]";
    }

    private void set(double x1, double y1, double x2, double y2) {
        this.ptMin = new Point2D(Math.min(x1, x2), Math.min(y1, y2));
        this.ptMax = new Point2D(Math.max(x1, x2), Math.max(y1, y2));
    }

    public boolean equals(Rectangle other) {
        return other.ptMin.equals(ptMin) && other.ptMax.equals(ptMax);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Rectangle)) return false;
        return equals((Rectangle) other);
    }

    public double area() {
        return (ptMax.getX() - ptMin.getX()) * (ptMax.getY() - ptMin.getY());
    }

    public Point2D centroid() {
        return new Point2D((ptMin.getX() + ptMax.getX()) / 2, (ptMin.getY() + ptMax.getY()) / 2);
    }

    public boolean contains(Point2D p) {
        return p.getX() >= ptMin.getX() && p.getX() <= ptMax.getX() && p.getY() >= ptMin.getY() && p.getY() <= ptMax.getY();
    }

    public void translate(Vector2D v) {
        ptMin.add(v);
        ptMax.add(v);
    }

    Point2D MinPt() {
        return ptMin;
    }

    Point2D MaxPt() {
        return ptMax;
    }

    public IntersectCase intersect(Rectangle r) {
        throw new UnsupportedOperationException();
    }

    public Point2D getMaxPoint() {
        return ptMax;
    }

    public Point2D getMinPoint() {
        return ptMin;
    }
}
