package org.zkforge.canvas;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * A wrapper of java.awt.Shape that implements Drawable
 * @author simon
 *
 */
public abstract class Shape extends Drawable implements java.awt.Shape {

    protected java.awt.Shape _internalShape;

    public Shape() {
        super();
    }

    public Rectangle getBounds() {
        return _internalShape.getBounds();
    }

    public Rectangle2D getBounds2D() {
        return _internalShape.getBounds2D();
    }

    public boolean contains(double x, double y) {
        return _internalShape.contains(x, y);
    }

    public boolean contains(Point2D p) {
        return _internalShape.contains(p);
    }

    public boolean intersects(double x, double y, double w, double h) {
        return _internalShape.intersects(x, y, w, h);
    }

    public boolean intersects(Rectangle2D r) {
        return _internalShape.intersects(r);
    }

    public boolean contains(double x, double y, double w, double h) {
        return _internalShape.contains(x, y, w, h);
    }

    public boolean contains(Rectangle2D r) {
        return _internalShape.contains(r);
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return _internalShape.getPathIterator(at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return _internalShape.getPathIterator(at, flatness);
    }
}
