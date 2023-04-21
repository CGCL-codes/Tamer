package org.josef.math;

/**
 * Superclass for classes that find the roots of a mathematical function.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 237 $
 */
public abstract class RootFinder {

    /**
     * Default constructor with protected access prevents creation of an
     * instance except by sub classes.
     */
    protected RootFinder() {
    }

    /**
     * Checks the supplied parameters so they can be used by a root finding
     * method like the {@link Bisection} method or {@link RegulaFalsi} method.
     * <br>This method checks that
     * <ul>
     *   <li>a unequal to b</li>
     *   <li>fa and fb are actual numbers (not NaN)</li>
     *   <li>fa and fb have opposite signs</li>
     * </ul>
     * @param a Left x value of the interval.
     * @param fa Left y value corresponding to the supplied a.
     * @param b Right x value of the interval.
     * @param fb Right y value corresponding to the supplied b.
     */
    protected static void checkIntervalForRoot(final double a, final double fa, final double b, final double fb) {
        if (a == b) {
            throw new IllegalArgumentException("Interval can't have zero size!");
        }
        if (Double.isNaN(fa)) {
            throw new IllegalArgumentException(String.format("f(%g) is Not a Number!", a));
        }
        if (Double.isNaN(fb)) {
            throw new IllegalArgumentException(String.format("f(%g) is Not a Number!", b));
        }
        if (Math.signum(fa) * Math.signum(fb) > 0) {
            throw new IllegalArgumentException(String.format("f(%g)=%g and f(%g)=%g. They should have opposite signs!", a, fa, b, fb));
        }
    }
}
