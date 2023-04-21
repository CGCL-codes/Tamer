package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;

/**
 * Implements the <a href="http://mathworld.wolfram.com/Bisection.html">
 * bisection algorithm</a> for finding zeros of univariate real functions. 
 * <p>
 * The function should be continuous but not necessarily smooth.</p>
 * 
 * @version $Revision: 735530 $ $Date: 2009-01-18 20:48:52 +0100 (So, 18 Jan 2009) $
 */
public class BisectionSolver extends UnivariateRealSolverImpl {

    /** Serializable version identifier */
    private static final long serialVersionUID = 5227509383222989438L;

    /**
     * Construct a solver for the given function.
     * 
     * @param f function to solve.
     * @deprecated as of 2.0 the function to solve is passed as an argument
     * to the {@link #solve(UnivariateRealFunction, double, double)} or
     * {@link UnivariateRealSolverImpl#solve(UnivariateRealFunction, double, double, double)}
     * method.
     */
    @Deprecated
    public BisectionSolver(UnivariateRealFunction f) {
        super(f, 100, 1E-6);
    }

    /**
     * Construct a solver.
     * 
     */
    public BisectionSolver() {
        super(100, 1E-6);
    }

    /** {@inheritDoc} */
    @Deprecated
    public double solve(double min, double max, double initial) throws MaxIterationsExceededException, FunctionEvaluationException {
        return solve(f, min, max);
    }

    /** {@inheritDoc} */
    @Deprecated
    public double solve(double min, double max) throws MaxIterationsExceededException, FunctionEvaluationException {
        return solve(f, min, max);
    }

    /** {@inheritDoc} */
    public double solve(final UnivariateRealFunction f, double min, double max, double initial) throws MaxIterationsExceededException, FunctionEvaluationException {
        return solve(min, max);
    }

    /** {@inheritDoc} */
    public double solve(final UnivariateRealFunction f, double min, double max) throws MaxIterationsExceededException, FunctionEvaluationException {
        clearResult();
        verifyInterval(min, max);
        double m;
        double fm;
        double fmin;
        int i = 0;
        while (i < maximalIterationCount) {
            m = UnivariateRealSolverUtils.midpoint(min, max);
            fmin = f.value(min);
            fm = f.value(m);
            if (fm * fmin > 0.0) {
                min = m;
            } else {
                max = m;
            }
            if (Math.abs(max - min) <= absoluteAccuracy) {
                m = UnivariateRealSolverUtils.midpoint(min, max);
                setResult(m, i);
                return m;
            }
            ++i;
        }
        throw new MaxIterationsExceededException(maximalIterationCount);
    }
}
