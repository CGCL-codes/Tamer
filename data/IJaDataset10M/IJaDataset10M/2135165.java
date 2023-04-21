package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.util.FastMath;

/**
 * Minimum function.
 *
 * @version $Id$
 * @since 3.0
 */
public class Min implements BivariateFunction {

    /** {@inheritDoc} */
    public double value(double x, double y) {
        return FastMath.min(x, y);
    }
}
