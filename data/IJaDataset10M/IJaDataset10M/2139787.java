package jat.cm.eom;

import jat.alg.integrators.*;

/**
 * <P>
 * Compute the acceleration of a body of negligible mass due to the gravitational force
 * of the bodies whose position in time is given by the DE405 Ephemerides. 
 * Reference frame and which bodies gravitate can be selected.
 *
 * @author Tobias Berthold
 * @version 1.0
 */
public class DE405Body implements Derivatives {

    public DE405Body() {
    }

    public double[] derivs(double t, double[] x) {
        double dxdt[] = new double[18];
        return dxdt;
    }
}
