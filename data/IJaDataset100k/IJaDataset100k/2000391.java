package jat.sensors;

import jat.matvec.data.*;
import jat.math.*;

/**
 * <P>
 * The AccelerometerErrorModel Class models a generic accelerometer. It does
 * not include g-sensitive or non-linearity error effects.
 *
 * @author
 * @version 1.0
 */
public class AccelerometerErrorModel {

    public VectorN scaleFactors;

    public VectorN misalignments;

    public VectorN noiseSigmas;

    public VectorN initialBiases;

    public VectorN biasRWsigmas;

    public GaussianVector noise;

    public GaussianVector randwalk;

    public Matrix sf;

    /** Default constructor. Models a perfect accelerometer.
     */
    public AccelerometerErrorModel() {
        this.scaleFactors = new VectorN(3);
        this.misalignments = new VectorN(6);
        this.noiseSigmas = new VectorN(3);
        this.initialBiases = new VectorN(3);
        this.biasRWsigmas = new VectorN(3);
    }

    /** Construct an accelerometer error model.
     * @param sf Vector3 containing scale factor errors in ppm.
     * @param ma Vector6 containing accelerometer misalignments in arcsec.
     * @param ns Vector3 containing the accelerometer measurement noise sigmas in micro-g/rt-Hz.
     * @param ib Vector3 containing the initial accel bias sigma in mg. This is the random constant part.
     * @param bs Vector3 containing the sigmas for accel random walk in m/rt s.
     */
    public AccelerometerErrorModel(VectorN sf, VectorN ma, VectorN ns, VectorN ib, VectorN bs) {
        sf.checkVectorDimensions(3);
        ma.checkVectorDimensions(6);
        ns.checkVectorDimensions(3);
        ib.checkVectorDimensions(3);
        bs.checkVectorDimensions(3);
        this.scaleFactors = sf.divide(1.0E+06);
        this.misalignments = ma.times(MathUtils.ARCSEC2RAD);
        double mg = 9.81E-06;
        this.noiseSigmas = ns.times(mg * 1.0E-03);
        this.initialBiases = ib.times(mg);
        this.biasRWsigmas = bs.times(mg);
        VectorN zeroMean = new VectorN(3);
        this.noise = new GaussianVector(zeroMean, this.noiseSigmas);
        this.randwalk = new GaussianVector(zeroMean, this.biasRWsigmas);
        this.sf = this.SFmatrix();
    }

    private Matrix SFmatrix() {
        Matrix out = new Matrix(this.scaleFactors);
        out.A[0][1] = -1.0 * this.misalignments.x[0];
        out.A[0][2] = this.misalignments.x[1];
        out.A[1][0] = this.misalignments.x[2];
        out.A[1][2] = -1.0 * this.misalignments.x[3];
        out.A[2][0] = -1.0 * this.misalignments.x[4];
        out.A[2][1] = this.misalignments.x[5];
        return out;
    }

    /** Outputs the accelerometer's contribution to the INS velocity error (del-f in body frame).
     * @return Velocity due to accelerometer errors.
     * @param qref reference quaternion.
     * @param f Specific force vector.
     * @param ba Vector3 containing the current accel bias state.
     */
    public VectorN computeErrors(VectorN f, VectorN ba) {
        this.noise.nextSet();
        VectorN sfma = this.sf.times(f);
        VectorN sum = sfma.plus(ba.plus(this.noise));
        return sum;
    }
}
