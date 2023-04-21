package org.apache.commons.math3.optimization;

import org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction;

/**
 * This interface represents an optimization algorithm for
 * {@link DifferentiableMultivariateVectorFunction vectorial differentiable
 * objective functions}.
 *
 * @version $Id: DifferentiableMultivariateVectorOptimizer.java 1244107 2012-02-14 16:17:55Z erans $
 * @since 3.0
 */
public interface DifferentiableMultivariateVectorOptimizer extends BaseMultivariateVectorOptimizer<DifferentiableMultivariateVectorFunction> {
}
