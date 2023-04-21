package org.josef.math;

import org.josef.util.CDebug;

/**
 * Finds the roots of a function within a defined interval, using the Regula
 * Falsi (False Position) method.
 * <br>To find the root of a function f , f should at least be continuous.
 * <br>More information on this topic can be found in the WikiPedia by clicking
 * <a href="http://en.wikipedia.org/wiki/False_position">here</a>.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 74 $
*/
public final class RegulaFalsi extends RootFinder {

    /**
     * Maximum number of iterations to perform before bailing out.
     */
    public static final int MAX_ITERATIONS = 1000;

    /**
     * Contains the sides of an interval.
     */
    private static enum intervalSide {

        LEFT, RIGHT
    }

    ;

    /**
     * Private constructor prevents creation of an instance outside this class.
     */
    private RegulaFalsi() {
    }

    /**
     * Computes the root of a function within the supplied interval that is
     * bounded by the supplied left and right values for x.
     * <br>This method will not perform more than {@link #MAX_ITERATIONS}
     * iterations to prevent this method from blocking, for example when the
     * supplied accuracy can never be reached.
     * @param left x value of the left side of the interval.
     *  <br>The y value, that is the value of function(left) should have a sign
     *  that is opposite to that of function(right).
     * @param right x value of the right side of the interval.
     *  <br>The y value, that is the value of function(right) should have a sign
     *  that is opposite to that of function(left).
     * @param function The function for which the root should be computed.
     * @param accuracy The accuracy of the calculated root.
     * @return The root of the supplied method that lies between the supplied
     *  interval.
     *  <br>The returned root is guaranteed to be accurate to the supplied
     *  accuracy.
     * @throws IllegalArgumentException when the accuracy is less than or equal
     *  to zero or when left equals right or when both f(left) and f(right) are
     *  of equal sign or when either f(left) or f(right) is "Not a Number".
     * @throws NullPointerException when the supplied function is null.
     * @throws ArithmeticException When more than {@link #MAX_ITERATIONS} are
     *  performed without finding the root.
     */
    public static double computeRoot(final double left, final double right, final SingleParameterFunction function, final double accuracy) {
        CDebug.checkParameterNotNull(function, "function");
        CDebug.checkParameterTrue(accuracy > 0, "Accuracy must be positive but is: " + accuracy);
        double a = left;
        double b = right;
        double fa = function.f(a);
        double fb = function.f(b);
        checkIntervalForRoot(a, fa, b, fb);
        double c = (fb * a - fa * b) / (fb - fa);
        double fc = function.f(c);
        intervalSide adjustedIntervalSide = null;
        int iterations = 1;
        while (Math.abs(fc) > accuracy) {
            if (fa * fc > 0.0) {
                a = c;
                fa = fc;
                if (adjustedIntervalSide == intervalSide.RIGHT) {
                    fb /= 2;
                }
                adjustedIntervalSide = intervalSide.RIGHT;
            } else {
                b = c;
                fb = fc;
                if (adjustedIntervalSide == intervalSide.LEFT) {
                    fa /= 2;
                }
                adjustedIntervalSide = intervalSide.LEFT;
            }
            c = (fb * a - fa * b) / (fb - fa);
            fc = function.f(c);
            if (++iterations > MAX_ITERATIONS) {
                throw new ArithmeticException("Could not find root using Regula Falsi within " + MAX_ITERATIONS + " iterations!");
            }
        }
        return c;
    }

    /**
     * Computes the root of a function within the supplied interval that is
     * bounded by the supplied left and right values for x.
     * @param left x value of the left side of the interval.
     *  <br>The y value, that is the value of function(left) should have a sign
     *  that is opposite to that of function(right).
     * @param right x value of the right side of the interval.
     *  <br>The y value, that is the value of function(right) should have a sign
     *  that is opposite to that of function(left).
     * @param function The function for which the root should be computed.
     * @param iterations The number of iterations to perform.
     *  <br>When the root has accurately be found this method will bail out
     *  before the supplied number of iterations have been performed.
     * @return The root of the supplied method that lies between the supplied
     *  interval.
     * @throws IllegalArgumentException when the number of iterations is less
     *  than one or when left equals right or when both f(left) and f(right) are
     *  of equal sign or when either f(left) or f(right) is "Not a Number".
     * @throws NullPointerException when the supplied function is null.
     */
    public static double computeRoot(final double left, final double right, final SingleParameterFunction function, final int iterations) {
        CDebug.checkParameterNotNull(function, "function");
        CDebug.checkParameterTrue(iterations > 0, "The number of iterations must be positive but is: " + iterations);
        double a = left;
        double b = right;
        double fa = function.f(a);
        double fb = function.f(b);
        checkIntervalForRoot(a, fa, b, fb);
        double c = (fb * a - fa * b) / (fb - fa);
        double fc = function.f(c);
        intervalSide adjustedIntervalSide = null;
        for (int i = 1; i < iterations && fc != 0.0; ++i) {
            if (fa * fc > 0.0) {
                a = c;
                fa = fc;
                if (adjustedIntervalSide == intervalSide.RIGHT) {
                    fb /= 2;
                }
                adjustedIntervalSide = intervalSide.RIGHT;
            } else {
                b = c;
                fb = fc;
                if (adjustedIntervalSide == intervalSide.LEFT) {
                    fa /= 2;
                }
                adjustedIntervalSide = intervalSide.LEFT;
            }
            c = (fb * a - fa * b) / (fb - fa);
            fc = function.f(c);
        }
        return c;
    }

    /**
     * For test purposes only.
     * @param args Not used.
     */
    public static void main(final String[] args) {
        final double a = Math.PI * 0.5;
        final double b = Math.PI * -0.75;
        final double accuracy = 0.00000000001D;
        double root = RegulaFalsi.computeRoot(a, b, new SingleParameterFunction() {

            public double f(final double x) {
                return Math.sin(x);
            }
        }, accuracy);
        System.out.println("Root:" + root);
    }
}
