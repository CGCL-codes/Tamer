package org.apache.commons.math3.optimization;

import org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.random.RandomVectorGenerator;

/**
 * Special implementation of the {@link DifferentiableMultivariateVectorOptimizer}
 * interface addind multi-start features to an existing optimizer.
 *
 * This class wraps a classical optimizer to use it several times in
 * turn with different starting points in order to avoid being trapped
 * into a local extremum when looking for a global one.
 *
 * @version $Id: DifferentiableMultivariateVectorMultiStartOptimizer.java 1244107 2012-02-14 16:17:55Z erans $
 * @since 2.0
 */
public class DifferentiableMultivariateVectorMultiStartOptimizer extends BaseMultivariateVectorMultiStartOptimizer<DifferentiableMultivariateVectorFunction> implements DifferentiableMultivariateVectorOptimizer {

    /**
     * Create a multi-start optimizer from a single-start optimizer.
     *
     * @param optimizer Single-start optimizer to wrap.
     * @param starts Number of starts to perform (including the
     * first one), multi-start is disabled if value is less than or
     * equal to 1.
     * @param generator Random vector generator to use for restarts.
     */
    public DifferentiableMultivariateVectorMultiStartOptimizer(final DifferentiableMultivariateVectorOptimizer optimizer, final int starts, final RandomVectorGenerator generator) {
        super(optimizer, starts, generator);
    }
}
