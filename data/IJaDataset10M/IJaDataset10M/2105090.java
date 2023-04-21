package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.util.FastMath;

/**
 * Implements the <a href="http://mathworld.wolfram.com/Bisection.html">
 * bisection algorithm</a> for finding zeros of univariate real functions.
 * <p>
 * The function should be continuous but not necessarily smooth.</p>
 *
 * @version $Id: BisectionSolver.java 1244107 2012-02-14 16:17:55Z erans $
 */
public class BisectionSolver extends AbstractUnivariateSolver {

    /** Default absolute accuracy. */
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1e-6;

    /**
     * Construct a solver with default accuracy (1e-6).
     */
    public BisectionSolver() {
        this(DEFAULT_ABSOLUTE_ACCURACY);
    }

    /**
     * Construct a solver.
     *
     * @param absoluteAccuracy Absolute accuracy.
     */
    public BisectionSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    /**
     * Construct a solver.
     *
     * @param relativeAccuracy Relative accuracy.
     * @param absoluteAccuracy Absolute accuracy.
     */
    public BisectionSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double doSolve() {
        double min = getMin();
        double max = getMax();
        verifyInterval(min, max);
        final double absoluteAccuracy = getAbsoluteAccuracy();
        double m;
        double fm;
        double fmin;
        while (true) {
            m = UnivariateSolverUtils.midpoint(min, max);
            fmin = computeObjectiveValue(min);
            fm = computeObjectiveValue(m);
            if (fm * fmin > 0) {
                min = m;
            } else {
                max = m;
            }
            if (FastMath.abs(max - min) <= absoluteAccuracy) {
                m = UnivariateSolverUtils.midpoint(min, max);
                return m;
            }
        }
    }
}
