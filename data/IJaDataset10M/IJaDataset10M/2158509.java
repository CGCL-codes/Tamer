package com.googlecode.charts4j;

import static com.googlecode.charts4j.collect.Preconditions.*;

/**
 * Static factory class for {@link Fill} hierarchy. Construct
 * {@link LinearGradientFill}, {@link LinearStripesFill}, {@link Fill}
 *
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 *
 */
public final class Fills {

    /**
     * Static factory class can never be instantiated.
     */
    private Fills() {
        throw new AssertionError();
    }

    /**
     * Create linear gradient fill.
     *
     * @param angle
     *            specifies the angle of the gradient between 0 (horizontal) and
     *            90 (vertical).
     * @param color
     *            color for gradient.Cannot be null.
     * @param offset
     *            specify at what point the color is pure where: 0 specifies the
     *            left-most chart position and 100 the right-most.
     *
     * @return a newly constructed linear gradient fill
     *
     */
    public static LinearGradientFill newLinearGradientFill(final int angle, final Color color, final double offset) {
        checkNotNull(color, "color cannot be null");
        checkArgument(offset >= Data.MIN_VALUE && offset <= Data.MAX_VALUE, "offset must be between 0 and 100");
        return new LinearGradientFill(angle, color, offset);
    }

    /**
     * Create linear stripes fill.
     *
     * @param angle
     *            specifies the angle of the gradient between 0 (horizontal) and
     *            90 (vertical)
     * @param color
     *            color of stripe.
     * @param width
     *            must be between 0 and 100 where 100 is the full width of the
     *            chart. Stripes are repeated until the chart is filled.
     *
     * @return a newly constructed linear stripes fill
     */
    public static LinearStripesFill newLinearStripesFill(final int angle, final Color color, final double width) {
        checkNotNull(color, "color cannot be null");
        checkArgument(width >= Data.MIN_VALUE && width <= Data.MAX_VALUE, "width must be between 0 and 100");
        return new LinearStripesFill(angle, color, width);
    }

    /**
     * Create a solid fill.
     *
     * @param color
     *            color of solid fill.
     * @return a newly constructed solid fill
     */
    public static Fill newSolidFill(final Color color) {
        return new SolidFill(color);
    }
}
