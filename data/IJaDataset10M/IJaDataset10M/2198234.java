package org.dishevelled.interpolate;

/**
 * Ease-in-out back interpolation function.
 * <p><img src="../../../../images/ease-in-out-back.png" alt="ease-in-out back graph" /></p>
 *
 * @author  Michael Heuer
 * @version $Revision$ $Date$
 */
public final class EaseInOutBack implements EasingFunction {

    /** {@inheritDoc} */
    public String toString() {
        return "ease-in-out-back";
    }

    /** {@inheritDoc} */
    public Double evaluate(final Double value) {
        double v = 2.0d * value;
        double s = 1.70158d * 1.525d;
        if (v < 1.0d) {
            return 0.5d * (v * v * ((s + 1.0d) * v - s));
        }
        v -= 2.0d;
        return 0.5d * (v * v * ((s + 1.0d) * v + s) + 2.0d);
    }
}
