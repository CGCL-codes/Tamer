package org.apache.commons.math.analysis;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

/**
 * Implements the <a href="http://mathworld.wolfram.com/RombergIntegration.html">
 * Romberg Algorithm</a> for integration of real univariate functions. For
 * reference, see <b>Introduction to Numerical Analysis</b>, ISBN 038795452X,
 * chapter 3.
 * <p>
 * Romberg integration employs k successvie refinements of the trapezoid
 * rule to remove error terms less than order O(N^(-2k)). Simpson's rule
 * is a special case of k = 2.</p>
 *  
 * @version $Revision: 620312 $ $Date: 2008-02-10 12:28:59 -0700 (Sun, 10 Feb 2008) $
 * @since 1.2
 */
public class RombergIntegrator extends UnivariateRealIntegratorImpl {

    /** serializable version identifier */
    private static final long serialVersionUID = -1058849527738180243L;

    /**
     * Construct an integrator for the given function.
     * 
     * @param f function to integrate
     */
    public RombergIntegrator(UnivariateRealFunction f) {
        super(f, 32);
    }

    /**
     * Integrate the function in the given interval.
     * 
     * @param min the lower bound for the interval
     * @param max the upper bound for the interval
     * @return the value of integral
     * @throws MaxIterationsExceededException if the maximum iteration count is exceeded
     * or the integrator detects convergence problems otherwise
     * @throws FunctionEvaluationException if an error occurs evaluating the
     * function
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public double integrate(double min, double max) throws MaxIterationsExceededException, FunctionEvaluationException, IllegalArgumentException {
        int i = 1, j, m = maximalIterationCount + 1;
        double r, t[][] = new double[m][m], s, olds;
        clearResult();
        verifyInterval(min, max);
        verifyIterationCount();
        TrapezoidIntegrator qtrap = new TrapezoidIntegrator(this.f);
        t[0][0] = qtrap.stage(min, max, 0);
        olds = t[0][0];
        while (i <= maximalIterationCount) {
            t[i][0] = qtrap.stage(min, max, i);
            for (j = 1; j <= i; j++) {
                r = (1L << (2 * j)) - 1;
                t[i][j] = t[i][j - 1] + (t[i][j - 1] - t[i - 1][j - 1]) / r;
            }
            s = t[i][i];
            if (i >= minimalIterationCount) {
                if (Math.abs(s - olds) <= Math.abs(relativeAccuracy * olds)) {
                    setResult(s, i);
                    return result;
                }
            }
            olds = s;
            i++;
        }
        throw new MaxIterationsExceededException(maximalIterationCount);
    }

    /**
     * Verifies that the iteration limits are valid and within the range.
     * 
     * @throws IllegalArgumentException if not
     */
    protected void verifyIterationCount() throws IllegalArgumentException {
        super.verifyIterationCount();
        if (maximalIterationCount > 32) {
            throw new IllegalArgumentException("Iteration upper limit out of [0, 32] range: " + maximalIterationCount);
        }
    }
}
