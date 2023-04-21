package model;

import java.awt.Graphics;
import java.awt.geom.Point2D;

public abstract class Drawing2D {

    protected Point2D.Double origo2D;

    protected double scale2D;

    public Drawing2D(Point2D.Double origo2D, double scale2D) {
        this.origo2D = origo2D;
        this.scale2D = scale2D;
    }

    public void set2DScaling(Point2D.Double origo2D, double scale2D) {
        this.origo2D = origo2D;
        this.scale2D = scale2D;
    }

    public abstract void setPrintScaling2(double scale);

    public abstract void paint(Graphics g);
}
