package com.bluebrim.layoutmanager;

/**
 * calculateDistance from p1 to p2 with dist=x+y.
 * Creation date: (2000-07-13 10:17:51)
 * @author: Arvid Berg & Masod Jalalian 
 */
public class CoDistTriangle implements com.bluebrim.layout.impl.shared.layoutmanager.CoDistTriangleIF {

    /**
 * CoDistTriangle constructor comment.
 */
    public CoDistTriangle() {
        super();
    }

    /**
 * calculateDistance from p1 to p2.
 */
    public double calculateDistance(java.awt.geom.Point2D p1, java.awt.geom.Point2D p2) {
        return Math.abs(p1.getX() - p2.getX()) + Math.abs(p1.getY() - p2.getY());
    }

    /**
 * getName method comment.
 */
    public java.lang.String getName() {
        return TRIANGLE_DIST_CALC;
    }
}
