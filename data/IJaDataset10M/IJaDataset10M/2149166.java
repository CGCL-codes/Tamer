package jat.cm.eom;

import jat.alg.integrators.*;

/**
 * <P>
 * The N-Body Class provides the force model of an n-body gravity simulation.
 * The reference frame is sun-inertial, the planet positions are taken from DE405 Ephemeris data.
 *
 * @author Tobias Berthold
 * @version 1.0
 */
public class NBody implements Derivatives {

    /** Construct an N-Body
    */
    public NBody() {
    }

    /** Compute the derivatives for numerical integration of N-body equations
     * of motion.
     * @return double [] containing the derivatives.
     * @param t Time (not used).
     * @param y State vector.
     */
    public double[] derivs(double t, double[] y) {
        double out[] = new double[6];
        return out;
    }
}
