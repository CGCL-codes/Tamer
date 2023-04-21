package org.josef.test.science.math;

import static org.josef.annotations.Status.Stage.DEVELOPMENT;
import static org.junit.Assert.assertTrue;
import org.josef.annotations.Review;
import org.josef.annotations.Status;
import org.josef.science.math.Bisection;
import org.josef.science.math.RegulaFalsi;
import org.josef.science.math.RootFinderContext;
import org.josef.science.math.SingleParameterFunction;
import org.junit.Test;

/**
 * JUnit test class for class {@link RegulaFalsi}.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 2840 $
 */
@Status(stage = DEVELOPMENT)
@Review(by = "Kees Schotanus", at = "2009-09-28")
public final class RegulaFalsiTest {

    /**
     * Function: x^2 - 2 = 0.
     * Has a root at square root of 2.
     */
    private final SingleParameterFunction function = new SingleParameterFunction() {

        /**
         * f(x) = x<sup>^2</sup> -2.
         * @param x Parameter x.
         * @return Result of f(x) for supplied x.
         */
        public double f(final double x) {
            return x * x - 2;
        }
    };

    /**
     * Tests {@link RegulaFalsi} finding a root using accuracy.
     */
    @Test
    public void testComputeRootWithAccuracy() {
        final double accuracy = 0.00000001D;
        RootFinderContext rootFinderContext = new RootFinderContext(new RegulaFalsi(accuracy));
        final double root = rootFinderContext.findRoot(function, -1, 2);
        assertTrue("Square root of 2", Math.abs(root - Math.sqrt(2)) <= accuracy);
    }

    /**
     * Tests {@link RegulaFalsi} finding a root using a maximum number of
     * iterations.
     */
    @Test
    public void testComputeRootWithIterations() {
        final int iterations = 20;
        RootFinderContext rootFinderContext = new RootFinderContext(new Bisection(iterations));
        final double root = rootFinderContext.findRoot(function, -1, 2);
        final double error = Math.abs(root - Math.sqrt(2));
        final double acceptedAccuracy = 0.000001D;
        assertTrue("Error is:" + error, error <= acceptedAccuracy);
    }
}
