package edu.gsbme.geometrykernel.data.dim1;

import edu.gsbme.geometrykernel.data.Idata;

/**
 * BSpline curve geometric class
 * @author David
 *
 */
public class BSplineCurve extends RationalBSplineCurve {

    public BSplineCurve(String id, int control_point, int degree) {
        super(id, control_point, degree);
    }

    @Override
    public Idata clone() {
        BSplineCurve clone = new BSplineCurve(this.getID(), degree, control_points.length);
        return clone;
    }
}
