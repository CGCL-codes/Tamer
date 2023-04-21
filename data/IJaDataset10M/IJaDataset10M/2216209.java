package org.dishevelled.interpolate;

/**
 * Ease-in quartic interpolation function.
 * <p><img src="../../../../images/ease-in-quartic.png" alt="ease-in quartic graph" /></p>
 *
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public final class EaseInQuartic implements EasingFunction {

    /** {@inheritDoc} */
    public String toString() {
        return "ease-in-quartic";
    }

    /** {@inheritDoc} */
    public Double evaluate(final Double value) {
        return Math.pow(value, 4.0d);
    }
}
