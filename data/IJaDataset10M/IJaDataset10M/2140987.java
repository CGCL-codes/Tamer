package org.apache.commons.math.optimization.direct;

import java.util.Comparator;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealPointValuePair;

/** 
 * This class implements the multi-directional direct search method.
 *
 * @version $Revision: 758054 $ $Date: 2009-03-24 23:13:27 +0100 (Di, 24 Mrz 2009) $
 * @see NelderMead
 * @since 1.2
 */
public class MultiDirectional extends DirectSearchOptimizer {

    /** Serializable version identifier. */
    private static final long serialVersionUID = -5347711305645019145L;

    /** Expansion coefficient. */
    private final double khi;

    /** Contraction coefficient. */
    private final double gamma;

    /** Build a multi-directional optimizer with default coefficients.
     * <p>The default values are 2.0 for khi and 0.5 for gamma.</p>
     */
    public MultiDirectional() {
        this.khi = 2.0;
        this.gamma = 0.5;
    }

    /** Build a multi-directional optimizer with specified coefficients.
     * @param khi expansion coefficient
     * @param gamma contraction coefficient
     */
    public MultiDirectional(final double khi, final double gamma) {
        this.khi = khi;
        this.gamma = gamma;
    }

    /** {@inheritDoc} */
    protected void iterateSimplex(final Comparator<RealPointValuePair> comparator) throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        while (true) {
            incrementIterationsCounter();
            final RealPointValuePair[] original = simplex;
            final RealPointValuePair best = original[0];
            final RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);
            if (comparator.compare(reflected, best) < 0) {
                final RealPointValuePair[] reflectedSimplex = simplex;
                final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
                if (comparator.compare(reflected, expanded) <= 0) {
                    simplex = reflectedSimplex;
                }
                return;
            }
            final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
            if (comparator.compare(contracted, best) < 0) {
                return;
            }
        }
    }

    /** Compute and evaluate a new simplex.
     * @param original original simplex (to be preserved)
     * @param coeff linear coefficient
     * @param comparator comparator to use to sort simplex vertices from best to poorest
     * @return best point in the transformed simplex
     * @exception FunctionEvaluationException if the function cannot be evaluated at
     * some point
     */
    private RealPointValuePair evaluateNewSimplex(final RealPointValuePair[] original, final double coeff, final Comparator<RealPointValuePair> comparator) throws FunctionEvaluationException {
        final double[] xSmallest = original[0].getPointRef();
        final int n = xSmallest.length;
        simplex = new RealPointValuePair[n + 1];
        simplex[0] = original[0];
        for (int i = 1; i <= n; ++i) {
            final double[] xOriginal = original[i].getPointRef();
            final double[] xTransformed = new double[n];
            for (int j = 0; j < n; ++j) {
                xTransformed[j] = xSmallest[j] + coeff * (xSmallest[j] - xOriginal[j]);
            }
            simplex[i] = new RealPointValuePair(xTransformed, Double.NaN, false);
        }
        evaluateSimplex(comparator);
        return simplex[0];
    }
}
