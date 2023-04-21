package jat.ins;

import jat.matvec.data.*;
import jat.math.*;

/**
 * <P>
 * The SIGIGyro Class models the SIGI gyro triad.
 * Only gyro bias and measurement noise are included.
 *
 * @author 
 * @version 1.0
 */
public class SIGIGyroFilterModel {

    /** accelerometer bias correlation time in seconds */
    public static final double correlationTime = 3600.0;

    /** gyro bias noise strength */
    private double qbias;

    private double q;

    /** Default constructor. Hardcoded with SIGI gyro numbers. */
    public SIGIGyroFilterModel() {
        VectorN zeroMean = new VectorN(3);
        double biasSigma = 0.0035 * MathUtils.DEG2RAD / 3600.0;
        double dt = 1.0;
        double exponent = -2.0 * dt / correlationTime;
        this.qbias = biasSigma * biasSigma * (1.0 - Math.exp(exponent));
        this.q = 2.0 * biasSigma * biasSigma / correlationTime;
    }

    /** 
     * Compute the derivatives for the gyro bias state.
     * The gyro bias is modeled as a first order Gauss-Markov process.
     * Used by GPS_INS Process Model.
     * @param bg gyro bias vector
     * @return the time derivative of the gyro bias
     */
    public VectorN biasProcess(VectorN bg) {
        double coef = -1.0 / correlationTime;
        VectorN out = bg.times(coef);
        return out;
    }

    /**
     * Return the gyro bias noise strength to be used in
     * the process noise matrix Q.
     * @return gyro bias noise strength
     */
    public double Q() {
        return this.q;
    }
}
