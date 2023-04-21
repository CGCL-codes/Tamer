package jat.alg.integrators;

/** Implements a Runge-Kutta-Fehlberg adaptive step size integrator
 * from Numerical Recipes. Modified to RK78 from the original RK45 in NR.
 * RK78 values from Erwin Fehlberg, NASA TR R-287
 * gets derivs via Derivatives interface
 */
public class RK78F_old implements Printable {

    private double minStepSize_;

    private double stepSize_;

    private double accuracy_;

    private double currentStepSize_;

    private boolean verbose;

    private boolean adaptive_;

    /** Default constructor.
     */
    public RK78F_old() {
        currentStepSize_ = stepSize_ = 1.0;
        minStepSize_ = 1.2E-10;
        accuracy_ = 1.0e-6;
        adaptive_ = true;
    }

    /** Construct a RungeKuttaFehlberg78 integrator with user specified accuracy.
     * @param accuracy Desired accuracy.
     */
    public RK78F_old(double accuracy) {
        currentStepSize_ = stepSize_ = 1.0;
        minStepSize_ = 1.2E-10;
        accuracy_ = 1.0e-6;
        adaptive_ = true;
    }

    /** Set the step size.
     * @param stepSize Step size.
     */
    public void setStepSize(double stepSize) {
        this.stepSize_ = stepSize;
    }

    /** Set the minimum step size.
     * @param stepSize Minimum step size.
     */
    public void setMinimumStepSize(double stepSize) {
        this.minStepSize_ = stepSize;
    }

    /** Set the integrator to adaptive step size mode.
     */
    public void setAdaptive() {
        adaptive_ = true;
    }

    /** Set the integrator to fixed step size mode.
     */
    public void setNonAdaptive() {
        adaptive_ = false;
    }

    /** Set the accuracy.
     * @param accuracy Desired accuracy.
     */
    public void setAccuracy(double accuracy) {
        this.accuracy_ = accuracy;
    }

    /** Set the verbose mode to true or false.
     * @param verbose Verbose flag.
     */
    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /** Set the integrator to verbose mode.
     */
    public void setVerbose() {
        verbose = !verbose;
    }

    private int[] nok = new int[1];

    private int[] nbad = new int[1];

    private int goodSteps() {
        return nok[0];
    }

    private int badSteps() {
        return nbad[0];
    }

    private int steps() {
        return nok[0] + nbad[0];
    }

    private double stepSize() {
        return currentStepSize_;
    }

    /** Integrate the equations of motion.
     * @param y Initial state values.
     * @param start Initial time.
     * @param end Final time.
     * @param dv Equations of Motion.
     * @param pr Printable.
     * @param print_switch Print flag. True = call the print method.
     * @return the final state.
     */
    public double[] integrate(double[] y, double start, double end, Derivatives dv, Printable pr, boolean print_switch) {
        double h = stepSize_;
        double hmin = minStepSize_;
        nok[0] = nbad[0] = 0;
        if (adaptive_) {
            odeint(y, start, end, accuracy_, h, hmin, nok, nbad, dv, pr, print_switch);
            if (verbose) {
                System.out.println("nok = " + nok[0] + "\tnbad = " + nbad[0]);
            }
        } else {
            rkdumb(y, start, end, h, dv, pr, print_switch);
        }
        return y;
    }

    /** Integrate the equations of motion. No printing/plotting interface provided.
     * @param y Initial state values.
     * @param start Initial time.
     * @param end Final time.
     * @param dv Equations of Motion.
     * @param pr Printable.
     * @param print_switch Print flag. True = call the print method.
     * @return the final state.
     */
    public double[] integrate(double[] y, double start, double end, Derivatives dv) {
        boolean print_switch = false;
        double h = stepSize_;
        double hmin = minStepSize_;
        nok[0] = nbad[0] = 0;
        if (adaptive_) {
            odeint(y, start, end, accuracy_, h, hmin, nok, nbad, dv, this, print_switch);
            if (verbose) {
                System.out.println("nok = " + nok[0] + "\tnbad = " + nbad[0]);
            }
        } else {
            rkdumb(y, start, end, h, dv, this, print_switch);
        }
        return y;
    }

    private void rkdumb(double[] ystart, double start, double end, double h, Derivatives dv, Printable pr, boolean print_switch) {
        int nvar = ystart.length;
        int nSteps = (int) Math.abs((end - start) / h);
        if (nSteps < 1) nSteps = 1;
        h = (end - start) / nSteps;
        double[] dydx = new double[nvar];
        double[] yend = new double[nvar];
        double[] yerr = new double[nvar];
        for (int step = 0; step < nSteps; step++) {
            double x = start + step * h;
            dydx = dv.derivs(x, ystart);
            rkck(ystart, dydx, x, h, yend, yerr, dv);
            for (int n = 0; n < nvar; n++) {
                ystart[n] = yend[n];
            }
            if (print_switch) {
                pr.print(x, ystart);
            }
        }
    }

    private static final int MAXSTP = 1000000;

    private static final double TINY = 1.0e-30;

    private int kmax;

    private int kount;

    private double[] xp;

    private double[][] yp;

    private double dxsav;

    private void odeint(double[] ystart, double x1, double x2, double eps, double h1, double hmin, int[] nok, int[] nbad, Derivatives dv, Printable pr, boolean print_switch) {
        int nvar = ystart.length;
        double[] x = new double[1];
        double[] hnext = new double[1];
        double[] hdid = new double[1];
        double[] yscal = new double[nvar];
        double[] y = new double[nvar];
        double[] dydx = new double[nvar];
        x[0] = x1;
        double h = Math.abs(h1);
        if (x2 < x1) h = -h;
        nok[0] = nbad[0] = kount = 0;
        for (int i = 0; i < nvar; i++) y[i] = ystart[i];
        double xsav = 0;
        if (kmax > 0) xsav = x[0] - dxsav * 2.0;
        for (int nstp = 1; nstp <= MAXSTP; nstp++) {
            dydx = dv.derivs(x[0], y);
            for (int i = 0; i < nvar; i++) yscal[i] = Math.abs(y[i]) + Math.abs(dydx[i] * h) + TINY;
            if (kmax > 0 && kount < kmax - 1 && Math.abs(x[0] - xsav) > Math.abs(dxsav)) {
                xp[++kount] = x[0];
                for (int i = 0; i < nvar; i++) yp[i][kount] = y[i];
                xsav = x[0];
            }
            if ((x[0] + h - x2) * (x[0] + h - x1) > 0.0) h = x2 - x[0];
            rkqs(y, dydx, x, h, eps, yscal, hdid, hnext, dv);
            if (hdid[0] == h) ++nok[0]; else ++nbad[0];
            if ((x[0] - x2) * (x2 - x1) >= 0.0) {
                for (int i = 0; i < nvar; i++) {
                    ystart[i] = y[i];
                }
                if (print_switch) {
                    pr.print(x[0], ystart);
                }
                if (kmax != 0) {
                    xp[++kount] = x[0];
                    for (int i = 0; i < nvar; i++) yp[i][kount] = y[i];
                }
                return;
            }
            if (Math.abs(hnext[0]) <= hmin) {
                error("Step size too small in odeint");
                System.out.println("h = " + hnext[0]);
            }
            h = hnext[0];
            currentStepSize_ = h;
        }
        error("Too many steps in routine odeint");
        System.out.println("step size = " + currentStepSize_);
    }

    private static final double SAFETY = 0.9;

    private static final double PGROW = -1.0 / 8.0;

    private static final double PSHRNK = -1.0 / 7.0;

    private static final double ERRCON = 2.56578451395034701E-8;

    private void rkqs(double[] y, double[] dydx, double[] x, double htry, double eps, double[] yscal, double[] hdid, double[] hnext, Derivatives dv) {
        int n = y.length;
        double errmax = 0;
        double[] yerr = new double[n];
        double[] ytemp = new double[n];
        double h = htry;
        for (; ; ) {
            rkck(y, dydx, x[0], h, ytemp, yerr, dv);
            errmax = 0;
            for (int i = 0; i < n; i++) errmax = Math.max(errmax, Math.abs(yerr[i] / yscal[i]));
            errmax /= eps;
            if (errmax <= 1.0) break;
            double htemp = SAFETY * h * Math.pow(errmax, PSHRNK);
            h = (h >= 0.0 ? Math.max(htemp, 0.1 * h) : Math.min(htemp, 0.1 * h));
            double xnew = x[0] + h;
            if (xnew == x[0]) error("stepsize underflow in rkqs");
        }
        if (errmax > ERRCON) hnext[0] = SAFETY * h * Math.pow(errmax, PGROW); else hnext[0] = 5.0 * h;
        x[0] += (hdid[0] = h);
        for (int i = 0; i < n; i++) y[i] = ytemp[i];
    }

    private static final double[] a = { 0.0, 2.0 / 27.0, 1.0 / 9.0, 1.0 / 6.0, 5.0 / 12.0, 0.5, 5.0 / 6.0, 1.0 / 6.0, 2.0 / 3.0, 1.0 / 3.0, 1.0, 0.0, 1.0 };

    private static final double[][] b = new double[13][12];

    static {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 12; j++) {
                b[i][j] = 0.0;
            }
        }
        b[1][0] = 2.0 / 27.0;
        b[2][0] = 1.0 / 36.0;
        b[2][1] = 1.0 / 12.0;
        b[3][0] = 1.0 / 24.0;
        b[3][2] = 1.0 / 8.0;
        b[4][0] = 5.0 / 12.0;
        b[4][2] = -25.0 / 16.0;
        b[4][3] = 25.0 / 16.0;
        b[5][0] = 1.0 / 20.0;
        b[5][3] = 0.25;
        b[5][4] = 0.2;
        b[6][0] = -25.0 / 108.0;
        b[6][3] = 125.0 / 108.0;
        b[6][4] = -65.0 / 27.0;
        b[6][5] = 125.0 / 54.0;
        b[7][0] = 31.0 / 300.0;
        b[7][4] = 61.0 / 225.0;
        b[7][5] = -2.0 / 9.0;
        b[7][6] = 13.0 / 900.0;
        b[8][0] = 2.0;
        b[8][3] = -53.0 / 6.0;
        b[8][4] = 704.0 / 45.0;
        b[8][5] = -107.0 / 9.0;
        b[8][6] = 67.0 / 90.0;
        b[8][7] = 3.0;
        b[9][0] = -91.0 / 108.0;
        b[9][3] = 23.0 / 108.0;
        b[9][4] = -976.0 / 135.0;
        b[9][5] = 311.0 / 54.0;
        b[9][6] = -19.0 / 60.0;
        b[9][7] = 17.0 / 6.0;
        b[9][8] = -1.0 / 12.0;
        b[10][0] = 2383.0 / 4100.0;
        b[10][3] = -341.0 / 164.0;
        b[10][4] = 4496.0 / 1025.0;
        b[10][5] = -301.0 / 82.0;
        b[10][6] = 2133.0 / 4100.0;
        b[10][7] = 45.0 / 82.0;
        b[10][8] = 45.0 / 164.0;
        b[10][9] = 18.0 / 41.0;
        b[11][0] = 3.0 / 205.0;
        b[11][5] = -6.0 / 41.0;
        b[11][6] = -3.0 / 205.0;
        b[11][7] = -3.0 / 41.0;
        b[11][8] = 3.0 / 41.0;
        b[11][9] = 6.0 / 41.0;
        b[12][0] = -1777.0 / 4100.0;
        b[12][3] = -341.0 / 164.0;
        b[12][4] = 4496.0 / 1025.0;
        b[12][5] = -289.0 / 82.0;
        b[12][6] = 2193.0 / 4100.0;
        b[12][7] = 51.0 / 82.0;
        b[12][8] = 33.0 / 164.0;
        b[12][9] = 12.0 / 41.0;
        b[12][11] = 1.0;
    }

    private static final double[] c = { 41.0 / 840.0, 0.0, 0.0, 0.0, 0.0, 34.0 / 105.0, 9.0 / 35.0, 9.0 / 35.0, 9.0 / 280.0, 9.0 / 280.0, 41.0 / 840.0, 0.0, 0.0 };

    private static final double[] chat = { 0.0, 0.0, 0.0, 0.0, 0.0, 34.0 / 105.0, 9.0 / 35.0, 9.0 / 35.0, 9.0 / 280.0, 9.0 / 280.0, 0.0, 41.0 / 840.0, 41.0 / 840.0 };

    private void rkck(double[] y, double[] dydx, double x, double h, double[] yout, double[] yerr, Derivatives dv) {
        int n = y.length;
        double f[][] = new double[13][n];
        double yt[][] = new double[13][n];
        double ytmp[] = new double[n];
        double sum[] = new double[n];
        double xeval[] = new double[13];
        for (int i = 0; i < 13; i++) {
            xeval[i] = x + a[i] * h;
        }
        f[0] = dydx;
        for (int i = 0; i < n; i++) {
            ytmp[i] = y[i] + h * b[1][0] * f[0][i];
        }
        f[1] = dv.derivs(xeval[1], ytmp);
        for (int i = 0; i < n; i++) {
            ytmp[i] = y[i] + h * (b[2][0] * f[0][i] + b[2][1] * f[1][i]);
        }
        f[2] = dv.derivs(xeval[2], ytmp);
        for (int i = 0; i < n; i++) {
            ytmp[i] = y[i] + h * (b[3][0] * f[0][i] + b[3][2] * f[2][i]);
        }
        f[3] = dv.derivs(xeval[3], ytmp);
        for (int i = 0; i < n; i++) {
            ytmp[i] = y[i] + h * (b[4][0] * f[0][i] + b[4][2] * f[2][i] + b[4][3] * f[3][i]);
        }
        f[4] = dv.derivs(xeval[4], ytmp);
        for (int i = 0; i < n; i++) {
            ytmp[i] = y[i] + h * (b[5][0] * f[0][i] + b[5][3] * f[3][i] + b[5][4] * f[4][i]);
        }
        f[5] = dv.derivs(xeval[5], ytmp);
        for (int i = 0; i < n; i++) {
            ytmp[i] = y[i] + h * (b[6][0] * f[0][i] + b[6][3] * f[3][i] + b[6][4] * f[4][i] + b[6][5] * f[5][i]);
        }
        f[6] = dv.derivs(xeval[6], ytmp);
        for (int i = 0; i < n; i++) {
            ytmp[i] = y[i] + h * (b[7][0] * f[0][i] + b[7][4] * f[4][i] + b[7][5] * f[5][i] + b[7][6] * f[6][i]);
        }
        f[7] = dv.derivs(xeval[7], ytmp);
        for (int i = 0; i < n; i++) {
            ytmp[i] = y[i] + h * (b[8][0] * f[0][i] + b[8][3] * f[3][i] + b[8][4] * f[4][i] + b[8][5] * f[5][i] + b[8][6] * f[6][i] + b[8][7] * f[7][i]);
        }
        f[8] = dv.derivs(xeval[8], ytmp);
        for (int i = 0; i < n; i++) {
            ytmp[i] = y[i] + h * (b[9][0] * f[0][i] + b[9][3] * f[3][i] + b[9][4] * f[4][i] + b[9][5] * f[5][i] + b[9][6] * f[6][i] + b[9][7] * f[7][i] + b[9][8] * f[8][i]);
        }
        f[9] = dv.derivs(xeval[9], ytmp);
        for (int i = 0; i < n; i++) {
            ytmp[i] = y[i] + h * (b[10][0] * f[0][i] + b[10][3] * f[3][i] + b[10][4] * f[4][i] + b[10][5] * f[5][i] + b[10][6] * f[6][i] + b[10][7] * f[7][i] + b[10][8] * f[8][i] + b[10][9] * f[9][i]);
        }
        f[10] = dv.derivs(xeval[10], ytmp);
        for (int i = 0; i < n; i++) {
            ytmp[i] = y[i] + h * (b[11][0] * f[0][i] + b[11][5] * f[5][i] + b[11][6] * f[6][i] + b[11][7] * f[7][i] + b[11][8] * f[8][i] + b[11][9] * f[9][i]);
        }
        f[11] = dv.derivs(xeval[11], ytmp);
        for (int i = 0; i < n; i++) {
            ytmp[i] = y[i] + h * (b[12][0] * f[0][i] + b[12][3] * f[3][i] + b[12][4] * f[4][i] + b[12][5] * f[5][i] + b[12][6] * f[6][i] + b[12][7] * f[7][i] + b[12][8] * f[8][i] + b[12][9] * f[9][i] + f[11][i]);
        }
        f[12] = dv.derivs(xeval[12], ytmp);
        for (int i = 0; i < n; i++) {
            yout[i] = y[i] + h * (chat[5] * f[5][i] + chat[6] * f[6][i] + chat[7] * f[7][i] + chat[8] * f[8][i] + chat[9] * f[9][i] + chat[11] * f[11][i] + chat[12] * f[12][i]);
            yerr[i] = h * c[0] * (f[11][i] + f[12][i] - f[0][i] - f[10][i]);
        }
    }

    public void print(double t, double[] y) {
    }

    private void error(String msg) {
        System.err.println("RungeKuttaFehlberg78: " + msg);
    }
}
