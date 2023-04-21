package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.util.FastMath;

/**
 * Arc-sine function.
 *
 * @version $Id$
 * @since 3.0
 */
public class Asin implements DifferentiableUnivariateFunction {

    /** {@inheritDoc} */
    public double value(double x) {
        return FastMath.asin(x);
    }

    /** {@inheritDoc} */
    public UnivariateFunction derivative() {
        return new UnivariateFunction() {

            /** {@inheritDoc} */
            public double value(double x) {
                return 1 / FastMath.sqrt(1 - x * x);
            }
        };
    }
}
