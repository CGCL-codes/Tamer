package org.apache.batik.ext.awt.geom;

import java.util.Arrays;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.PrintStream;
import java.io.FileOutputStream;

/**
 * A class representing a linear path segment.
 *
 * @version $Id: Linear.java,v 1.1 2005/11/21 09:51:24 dev Exp $
 */
public class Linear implements Segment {

    public Point2D.Double p1, p2;

    public Linear() {
        p1 = new Point2D.Double();
        p2 = new Point2D.Double();
    }

    public Linear(double x1, double y1, double x2, double y2) {
        p1 = new Point2D.Double(x1, y1);
        p2 = new Point2D.Double(x2, y2);
    }

    public Linear(Point2D.Double p1, Point2D.Double p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public Object clone() {
        return new Linear(new Point2D.Double(p1.x, p1.y), new Point2D.Double(p2.x, p2.y));
    }

    public Segment reverse() {
        return new Linear(new Point2D.Double(p2.x, p2.y), new Point2D.Double(p1.x, p1.y));
    }

    public double minX() {
        if (p1.x < p2.x) return p1.x;
        return p2.x;
    }

    public double maxX() {
        if (p1.x > p2.x) return p1.x;
        return p2.x;
    }

    public double minY() {
        if (p1.y < p2.y) return p1.y;
        return p2.y;
    }

    public double maxY() {
        if (p1.y > p2.y) return p2.y;
        return p1.y;
    }

    public Rectangle2D getBounds2D() {
        double x, y, w, h;
        if (p1.x < p2.x) {
            x = p1.x;
            w = p2.x - p1.x;
        } else {
            x = p2.x;
            w = p1.x - p2.x;
        }
        if (p1.y < p2.y) {
            y = p1.y;
            h = p2.y - p1.y;
        } else {
            y = p2.y;
            h = p1.y - p2.y;
        }
        return new Rectangle2D.Double(x, y, w, h);
    }

    public Point2D.Double evalDt(double t) {
        double x = (p2.x - p1.x);
        double y = (p2.y - p1.y);
        return new Point2D.Double(x, y);
    }

    public Point2D.Double eval(double t) {
        double x = p1.x + t * (p2.x - p1.x);
        double y = p1.y + t * (p2.y - p1.y);
        return new Point2D.Double(x, y);
    }

    public Segment.SplitResults split(double y) {
        if ((y == p1.y) || (y == p2.y)) return null;
        if ((y <= p1.y) && (y <= p2.y)) return null;
        if ((y >= p1.y) && (y >= p2.y)) return null;
        double t = (y - p1.y) / (p2.y - p1.y);
        Segment[] t0 = { getSegment(0, t) };
        Segment[] t1 = { getSegment(t, 1) };
        if (p2.y < y) return new Segment.SplitResults(t0, t1);
        return new Segment.SplitResults(t1, t0);
    }

    public Segment getSegment(double t0, double t1) {
        Point2D.Double np1 = eval(t0);
        Point2D.Double np2 = eval(t1);
        return new Linear(np1, np2);
    }

    public Segment splitBefore(double t) {
        return new Linear(p1, eval(t));
    }

    public Segment splitAfter(double t) {
        return new Linear(eval(t), p2);
    }

    /**
     * Subdivides this Linear segment into two segments at t = 0.5.
     * can be done with getSegment but this is more efficent.
     * @param s0 if non-null contains portion of curve from  0->.5
     * @param s1 if non-null contains portion of curve from .5->1
     */
    public void subdivide(Segment s0, Segment s1) {
        Linear l0 = null, l1 = null;
        if (s0 instanceof Linear) l0 = (Linear) s0;
        if (s1 instanceof Linear) l1 = (Linear) s1;
        subdivide(l0, l1);
    }

    /**
     * Subdivides this Linear segment into two segments at given t.
     * @param s0 if non-null contains portion of curve from 0->t.
     * @param s1 if non-null contains portion of curve from t->1.
     */
    public void subdivide(double t, Segment s0, Segment s1) {
        Linear l0 = null, l1 = null;
        if (s0 instanceof Linear) l0 = (Linear) s0;
        if (s1 instanceof Linear) l1 = (Linear) s1;
        subdivide(t, l0, l1);
    }

    /**
     * Subdivides this Cubic curve into two curves at t = 0.5.
     * Can be done with getSegment but this is more efficent.
     * @param l0 if non-null contains portion of curve from  0->.5
     * @param l1 if non-null contains portion of curve from .5->1
     */
    public void subdivide(Linear l0, Linear l1) {
        if ((l0 == null) && (l1 == null)) return;
        double x = (p1.x + p2.x) * .5;
        double y = (p1.y + p2.y) * .5;
        if (l0 != null) {
            l0.p1.x = p1.x;
            l0.p1.y = p1.y;
            l0.p2.x = x;
            l0.p2.y = y;
        }
        if (l1 != null) {
            l1.p1.x = x;
            l1.p1.y = y;
            l1.p2.x = p2.x;
            l1.p2.y = p2.y;
        }
    }

    /**
     * Subdivides this Cubic curve into two curves.
     * Can be done with getSegment but this is more efficent.
     * @param t position to split the curve
     * @param l0 if non-null contains portion of curve from  0->t
     * @param l1 if non-null contains portion of curve from t->1
     */
    public void subdivide(double t, Linear l0, Linear l1) {
        if ((l0 == null) && (l1 == null)) return;
        double x = p1.x + t * (p2.x - p1.x);
        double y = p1.y + t * (p2.y - p1.y);
        if (l0 != null) {
            l0.p1.x = p1.x;
            l0.p1.y = p1.y;
            l0.p2.x = x;
            l0.p2.y = y;
        }
        if (l1 != null) {
            l1.p1.x = x;
            l1.p1.y = y;
            l1.p2.x = p2.x;
            l1.p2.y = p2.y;
        }
    }

    public double getLength() {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double getLength(double maxErr) {
        return getLength();
    }

    public String toString() {
        return ("M" + p1.x + "," + p1.y + "L" + p2.x + "," + p2.y);
    }
}
