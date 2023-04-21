package edu.udo.cs.bioinfo.jprobdist;

import edu.udo.cs.bioinfo.jprobdist.MathFunctions.RealFunction;
import static edu.udo.cs.bioinfo.jprobdist.MathFunctions.*;

/**
 * This class represents an abstract implementation of a
 * univariate (i.e., 1-dimensional) probability distribution.
 *
 * It contains default implementations of frequently used convenience
 * methods.
 *
 * For concrete probability distribution, more efficient methods
 * may exist, so the methods in this class should be overrided where desired.
 *
 * @author Sven Rahmann
 */
public abstract class AbstractDistribution implements UVDistribution {

    public double f(final double x) {
        return Math.exp(lnf(x));
    }

    public double P(final double x) {
        return Math.exp(lnP(x));
    }

    public double P(final Interval ab) {
        return Math.exp(lnP(ab));
    }

    public double cdf(final double x) {
        return Math.exp(lncdf(x));
    }

    public double ucdf(final double x) {
        return Math.exp(lnucdf(x));
    }

    public double E() {
        return moment(1.0);
    }

    public double Var() {
        return cmoment(2.0);
    }

    public double std() {
        return Math.sqrt(Var());
    }

    public double skewness() {
        return cmoment(3.0) / Math.pow(Var(), 1.5);
    }

    public double kurtosisExcess() {
        final double v = Var();
        return cmoment(4.0) / (v * v) - 3.0;
    }

    public double kurtosisProper() {
        return kurtosisExcess() + 3.0;
    }

    public double median() {
        return (qf(0.5));
    }

    public double iqr() {
        return (qf(0.75) - qf(0.25));
    }

    public final double[] boxPlotStatistics() {
        double med = median();
        double lq = qf(0.25);
        double uq = qf(0.75);
        double lw = lq - 1.5 * iqr();
        double uw = uq + 1.5 * iqr();
        return new double[] { lw, lq, med, uw, uq };
    }

    public double qf(final double p) {
        return qfNumeric(p, 0.0);
    }

    /** quantile function, computed by a numerical root finding method.
   * The result is accurate up to the given tolerance tol, i.e.,
   * if qf(p) = inf {x : cdf(x)>=p}, and the function returns y,
   * then |y=qf(p)|<=tol.
   * The special value tol=0 computes up to best possible numerical accuracy.
   *@param p probability 0<=p<=1 for which to compute the quantile.
   *@param tol numerical absolute tolerance for the result
   *@return quantile of p
   */
    public final double qfNumeric(final double p, final double tol) {
        if (p < 0 || p > 1) throw new IllegalArgumentException("qf(p): p must be in [0,1], is " + Double.toString(p));
        if (p == 0.0) return Double.NEGATIVE_INFINITY;
        if (p == 1.0) return max();
        final double m = min();
        if (m != Double.NEGATIVE_INFINITY && cdf(m) >= p) return m;
        RealFunction fun = new RealFunction() {

            public final double valueAt(final double x) {
                return (cdf(x) - p);
            }
        };
        Interval i0 = findRootInterval(fun, m, max());
        return findRootBisection(fun, i0.a, i0.b, tol);
    }

    public double random() {
        final double p = Math.random();
        if (p == 0.0) return max();
        return qf(p);
    }

    public double[] random(final int n) {
        final double[] r = new double[n];
        for (int i = 0; i < n; i++) r[i] = random();
        return r;
    }

    public final double f(final double... x) {
        if (x.length != 1) throw new DimensionMismatchException();
        return f(x[0]);
    }

    public final double lnf(final double... x) {
        if (x.length != 1) throw new DimensionMismatchException();
        return lnf(x[0]);
    }

    public final double P(final double... x) {
        if (x.length != 1) throw new DimensionMismatchException();
        return P(x[0]);
    }

    public final double lnP(final double... x) {
        if (x.length != 1) throw new DimensionMismatchException();
        return lnP(x[0]);
    }

    public final double cdf(final double... x) {
        if (x.length != 1) throw new DimensionMismatchException();
        return cdf(x[0]);
    }

    public final double lncdf(final double... x) {
        if (x.length != 1) throw new DimensionMismatchException();
        return lncdf(x[0]);
    }

    public final double ucdf(final double... x) {
        if (x.length != 1) throw new DimensionMismatchException();
        return ucdf(x[0]);
    }

    public final double lnucdf(final double... x) {
        if (x.length != 1) throw new DimensionMismatchException();
        return lnucdf(x[0]);
    }

    public final boolean isAtom(final double... x) {
        if (x.length != 1) throw new DimensionMismatchException();
        return isAtom(x[0]);
    }

    public final double closestAtom(final double... x) {
        if (x.length != 1) throw new DimensionMismatchException();
        return closestAtom(x[0]);
    }
}
