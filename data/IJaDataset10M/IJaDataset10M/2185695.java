package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;

/**
 * Inverse function.
 *
 * @version $Id$
 * @since 3.0
 */
public class Inverse implements DifferentiableUnivariateFunction {

    /** {@inheritDoc} */
    public double value(double x) {
        return 1 / x;
    }

    /** {@inheritDoc} */
    public UnivariateFunction derivative() {
        return new UnivariateFunction() {

            /** {@inheritDoc} */
            public double value(double x) {
                return -1 / (x * x);
            }
        };
    }
}
