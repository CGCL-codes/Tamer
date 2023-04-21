package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;

/**
 * Constant function.
 *
 * @version $Id$
 * @since 3.0
 */
public class Constant implements DifferentiableUnivariateFunction {

    /** Constant. */
    private final double c;

    /**
     * @param c Constant.
     */
    public Constant(double c) {
        this.c = c;
    }

    /** {@inheritDoc} */
    public double value(double x) {
        return c;
    }

    /** {@inheritDoc} */
    public DifferentiableUnivariateFunction derivative() {
        return new Constant(0);
    }
}
