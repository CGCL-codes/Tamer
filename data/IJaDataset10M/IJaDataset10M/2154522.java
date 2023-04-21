package ch.unizh.ini.jaer.projects.labyrinthkalman;

/**
 * base class for Kalman filters. implements bits and pieces of
 * predict and update equations.
 * 
 * @author Tobias Pietzsch
 */
public abstract class KalmanFilter {

    /**
	 * size of state vector.
	 */
    protected final int n;

    /**
	 * size of control vector.
	 */
    protected final int m;

    /**
	 * size of measurement vector.
	 */
    protected final int k;

    protected final double[] mu;

    protected final double[][] Sigma;

    protected final double[][] At;

    protected final double[][] Bt;

    protected final double[][] Ct;

    protected final double[][] Rt;

    protected final double[][] Qt;

    private double[][] S;

    private double[][] SInv;

    private double[][] Kt;

    private double[][] Mnn1;

    private double[][] Mnk1;

    private double[] vn1;

    private double[] vn2;

    private double[] vk1;

    private double[] vk2;

    public KalmanFilter(int stateDimension, int controlDimension, int measurementDimension) {
        n = stateDimension;
        m = controlDimension;
        k = measurementDimension;
        mu = new double[n];
        Sigma = new double[n][n];
        At = new double[n][n];
        Bt = new double[n][m];
        Ct = new double[k][n];
        Rt = new double[n][n];
        Qt = new double[k][k];
        SInv = new double[k][k];
        S = new double[k][k];
        Kt = new double[n][k];
        Mnn1 = new double[n][n];
        Mnk1 = new double[n][k];
        vn1 = new double[n];
        vn2 = new double[n];
        vk1 = new double[k];
        vk2 = new double[k];
    }

    /**
	 * @return state mean. do not modify (unless you know EXACTLY what you're
	 *         doing :-).
	 */
    public double[] getMu() {
        return mu;
    }

    /**
	 * @return state covariance. do not modify (unless you know EXACTLY what
	 *         you're doing :-).
	 */
    public double[][] getSigma() {
        return Sigma;
    }

    /**
	 * compute the mahalanobis distance between the predicted measurement and
	 * {@code meas}.
	 * 
	 * @param meas
	 *            the measurement (a k-vector)
	 * 
	 * @return the mahalanobis distance
	 */
    public double mahalanobisToMeasurement(final double[] meas) {
        assert meas.length == k;
        computeSInv();
        MatrixOps.mult(Ct, mu, vk1);
        MatrixOps.subtract(meas, vk1, vk2);
        MatrixOps.mult(SInv, vk2, vk1);
        return MatrixOps.dot(vk2, vk1);
    }

    /**
	 * Compute S and S^{-1}.
	 * set Qt to the correct value before calling this.
	 */
    protected void computeSInv() {
        MatrixOps.multABAT(Ct, Sigma, S);
        MatrixOps.increment(S, Qt);
        MatrixOps.invert2by2Matrix(S, SInv);
    }

    /**
	 * predict state mean.
	 * set At and Bt to the correct values before calling this.
	 * 
	 * @param act
	 *            control vector (a m-vector)
	 */
    protected void predictMu(final double[] act) {
        assert act.length == m;
        MatrixOps.mult(At, mu, vn1);
        MatrixOps.mult(Bt, act, vn2);
        MatrixOps.add(vn1, vn2, mu);
    }

    /**
	 * predict state covariance.
	 * set At and Rt to the correct values before calling this.
	 */
    protected void predictSigma() {
        MatrixOps.multABAT(At, Sigma, Mnn1);
        MatrixOps.add(Mnn1, Rt, Sigma);
    }

    /**
	 * compute the Kalman gain Kt.
	 * set Ct and Qt to the correct value before calling this.
	 */
    protected void updateKalmanGain() {
        computeSInv();
        MatrixOps.multABT(Sigma, Ct, Mnk1);
        MatrixOps.mult(Mnk1, SInv, Kt);
    }

    /**
	 * update the state mean.
	 * set Ct to the correct value and compute Kt before calling this.
	 * 
	 * @param meas
	 *            the measurement (a k-vector)
	 */
    protected void correctMu(final double[] meas) {
        assert meas.length == k;
        MatrixOps.mult(Ct, mu, vk1);
        MatrixOps.subtract(meas, vk1, vk2);
        MatrixOps.mult(Kt, vk2, vn1);
        MatrixOps.add(mu, vn1, mu);
    }

    /**
	 * update the state covariance.
	 * set Ct to the correct value and compute Kt before calling this.
	 * 
	 */
    protected void correctSigma() {
        MatrixOps.multABAT(Kt, S, Mnn1);
        MatrixOps.decrement(Sigma, Mnn1);
    }

    public abstract void predict(final double[] act, double dt);

    public abstract void correct(final double[] meas);
}
