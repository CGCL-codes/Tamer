package gj.awt.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A 2D path - missing from the jawa.awt.geom.* stuff
 */
public class Path implements Shape, PathIteratorKnowHow {

    /** a general path we keep */
    private GeneralPath gp = new GeneralPath();

    /** the lastAngle we keep */
    private double lastAngle;

    /** the lastPoint we keep */
    private Point2D.Double lastPoint = new Point2D.Double();

    /** a preset bounds */
    private Rectangle2D overrideBounds = null;

    /**
   * Accessor - lastPoint
   */
    public synchronized Point2D getLastPoint() {
        return new Point2D.Double(lastPoint.x, lastPoint.y);
    }

    /**
   * Accessor - lastAngle
   */
    public synchronized double getLastAngle() {
        return lastAngle;
    }

    /**
   * Translate by Point
   */
    public synchronized void translate(Point2D delta) {
        translate(delta.getX(), delta.getY());
    }

    /**
   * Translate by dx,dy
   */
    public synchronized void translate(double dx, double dy) {
        gp.transform(AffineTransform.getTranslateInstance(dx, dy));
        lastPoint.setLocation(lastPoint.getX() + dx, lastPoint.getY() + dy);
    }

    /**
   * Resets the path
   */
    public synchronized void reset() {
        gp.reset();
        lastPoint.setLocation(0, 0);
        lastAngle = 0;
    }

    /**
   * Sets to given shape
   */
    public synchronized Path set(Shape shape) {
        gp.reset();
        Point2D.Double p = new Point2D.Double();
        PathIterator it = shape.getPathIterator(null);
        float[] vals = new float[6];
        while (!it.isDone()) {
            int type = it.currentSegment(vals);
            int i = -1;
            switch(type) {
                case SEG_CLOSE:
                    throw new IllegalArgumentException("Path cannot be closed");
                case SEG_CUBICTO:
                    gp.curveTo(vals[0], vals[1], vals[2], vals[3], vals[4], vals[5]);
                    i = 4;
                    break;
                case SEG_LINETO:
                    gp.lineTo(vals[0], vals[1]);
                    i = 0;
                    break;
                case SEG_MOVETO:
                    gp.moveTo(vals[0], vals[1]);
                    i = 0;
                    break;
                case SEG_QUADTO:
                    gp.quadTo(vals[0], vals[1], vals[2], vals[3]);
                    i = 2;
                    break;
            }
            lastPoint.setLocation(p.getX(), p.getY());
            p.setLocation(vals[i + 0], vals[i + 1]);
            it.next();
        }
        lastAngle = Geometry.getAngle(lastPoint, p);
        lastPoint.setLocation(p.getX(), p.getY());
        return this;
    }

    /**
   * @see java.awt.geom.GeneralPath#moveTo(float, float)
   */
    public synchronized Path moveTo(Point2D p) {
        gp.moveTo((float) p.getX(), (float) p.getY());
        lastAngle = Geometry.getAngle(lastPoint, p);
        lastPoint.setLocation(p.getX(), p.getY());
        return this;
    }

    /**
   * @see java.awt.geom.GeneralPath#lineTo(float, float)
   */
    public synchronized Path lineTo(Point2D p) {
        gp.lineTo((float) p.getX(), (float) p.getY());
        lastAngle = Geometry.getAngle(lastPoint, p);
        lastPoint.setLocation(p.getX(), p.getY());
        return this;
    }

    /**
   * @see java.awt.geom.GeneralPath#quadTo(float, float, float, float)
   */
    public synchronized Path quadTo(Point2D c, Point2D p) {
        gp.quadTo((float) c.getX(), (float) c.getY(), (float) p.getX(), (float) p.getY());
        lastPoint.setLocation(p.getX(), p.getY());
        return this;
    }

    /**
   * @see java.awt.geom.GeneralPath#curveTo(float, float, float, float, float, float)
   */
    public synchronized Path curveTo(Point2D c1, Point2D c2, Point2D p) {
        gp.curveTo((float) c1.getX(), (float) c1.getY(), (float) c2.getX(), (float) c2.getY(), (float) p.getX(), (float) p.getY());
        lastPoint.setLocation(p.getX(), p.getY());
        return this;
    }

    /**
   * Appends a shape   */
    public synchronized Path append(Shape s) {
        gp.append(s, false);
        return this;
    }

    /**
   * Appends a shape
   */
    public synchronized Path append(PathIterator pi) {
        gp.append(pi, false);
        return this;
    }

    /**
   * @see java.awt.Shape#contains(double, double, double, double)
   */
    public boolean contains(double x, double y, double w, double h) {
        return gp.contains(x, y, w, h);
    }

    /**
   * @see java.awt.Shape#contains(double, double)
   */
    public boolean contains(double x, double y) {
        return gp.contains(x, y);
    }

    /**
   * @see java.awt.Shape#contains(Point2D)
   */
    public boolean contains(Point2D p) {
        return gp.contains(p);
    }

    /**
   * @see java.awt.Shape#contains(Rectangle2D)
   */
    public boolean contains(Rectangle2D r) {
        return gp.contains(r);
    }

    /**
   * @see java.awt.Shape#getBounds()
   */
    public Rectangle getBounds() {
        if (overrideBounds != null) return overrideBounds.getBounds();
        return gp.getBounds();
    }

    /**
   * @see java.awt.Shape#getBounds2D()
   */
    public Rectangle2D getBounds2D() {
        if (overrideBounds != null) return overrideBounds;
        return gp.getBounds2D();
    }

    /**
   * Override bounds   */
    public void setBounds2D(Rectangle2D r) {
        overrideBounds = r;
    }

    /**
   * @see java.awt.Shape#getPathIterator(AffineTransform, double)
   */
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return gp.getPathIterator(at, flatness);
    }

    /**
   * @see java.awt.Shape#getPathIterator(AffineTransform)
   */
    public PathIterator getPathIterator(AffineTransform at) {
        return gp.getPathIterator(at);
    }

    /**
   * @see java.awt.Shape#intersects(double, double, double, double)
   */
    public boolean intersects(double x, double y, double w, double h) {
        return gp.intersects(x, y, w, h);
    }

    /**
   * @see java.awt.Shape#intersects(Rectangle2D)
   */
    public boolean intersects(Rectangle2D r) {
        return gp.intersects(r);
    }
}
