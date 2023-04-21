package org.opencarto.algo.deformation.constraint;

import java.util.logging.Logger;
import org.opencarto.algo.deformation.GPoint;
import org.opencarto.algo.deformation.GSimpleConstraint;
import org.opencarto.algo.deformation.submicro.GTriangle;
import com.vividsolutions.jts.geom.Coordinate;

public class TriangleArea extends GSimpleConstraint {

    private static Logger logger = Logger.getLogger(TriangleArea.class.getName());

    private double goal;

    public TriangleArea(GTriangle tri, double imp) {
        this(tri, imp, tri.getInitialArea());
    }

    public TriangleArea(GTriangle tri, double imp, double goalArea) {
        super(tri, imp);
        this.goal = goalArea;
    }

    @Override
    public Coordinate getDisplacement(GPoint pt, double alpha) {
        GTriangle tri = (GTriangle) getSubmicro();
        GPoint pt1 = null, pt2 = null;
        if (pt == tri.getPt1()) {
            pt1 = tri.getPt2();
            pt2 = tri.getPt3();
        } else if (pt == tri.getPt2()) {
            pt1 = tri.getPt3();
            pt2 = tri.getPt1();
        } else if (pt == tri.getPt3()) {
            pt1 = tri.getPt1();
            pt2 = tri.getPt2();
        } else {
            logger.severe("Error in triangle area: the point do not belong to the triangle");
            return null;
        }
        double dx = pt2.getX() - pt1.getX();
        double dy = pt2.getY() - pt1.getY();
        double a = 2 * alpha * (this.goal - tri.getArea()) / (3 * (dx * dx + dy * dy));
        return new Coordinate(-a * dy, a * dx);
    }
}
