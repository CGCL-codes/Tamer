package edu.gsbme.geometrykernel.data.dim2;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import edu.gsbme.geometrykernel.data.Idata;

/**
 * BSpline surface geometric class
 * @author David
 *
 */
public class BSplineSurface extends RationalBSplineSurface {

    public BSplineSurface(String id, int degree1, int degree2, int ctrl_pt_1, int ctrl_pt_2) {
        super(id, degree1, degree2, ctrl_pt_1, ctrl_pt_2);
    }

    @Override
    public Point3d evaluate(double s, double t) {
        return null;
    }

    @Override
    public Vector3d normal(double s, double t) {
        return null;
    }

    @Override
    public boolean equals(Idata object) {
        return false;
    }

    @Override
    public Idata clone() {
        BSplineSurface clone = new BSplineSurface(this.getID(), this.getDegree1(), this.getDegree2(), this.control_points.length, this.control_points[0].length);
        return null;
    }
}
