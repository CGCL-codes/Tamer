package org.josef.test.science.math;

import static org.junit.Assert.assertEquals;
import org.josef.science.math.Complex;
import org.josef.science.math.QuadraticEquation;
import org.junit.Test;

/**
 * JUnit test class for class {@link QuadraticEquation}.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 3598 $
 */
public final class QuadraticEquationTest {

    /**
     * Acceptable error when comparing doubles.
     */
    public static final double DELTA = 1.0e-9;

    /**
     * Tests {@link QuadraticEquation#getDiscriminant()}.
     */
    @Test
    public void testGetDiscriminant() {
        final QuadraticEquation equation = new QuadraticEquation(1, 2, 1);
        assertEquals("Unexpected discriminants!", 0, equation.getDiscriminant(), 0);
    }

    /**
     * Tests {@link QuadraticEquation#getNumberOfRoots()}.
     */
    @Test
    public void testGetNumberOfRoots() {
        final QuadraticEquation positiveDiscriminantEquation = new QuadraticEquation(1, 3, 1.25);
        assertEquals("Unexpected number of roots!", 2, positiveDiscriminantEquation.getNumberOfRoots());
        final QuadraticEquation zeroDiscriminantEquation = new QuadraticEquation(1, 2, 1);
        assertEquals("Unexpected number of roots!", 1, zeroDiscriminantEquation.getNumberOfRoots());
        final QuadraticEquation negativeDiscriminantEquation = new QuadraticEquation(2, 2, 1);
        assertEquals("Unexpected number of roots!", 2, negativeDiscriminantEquation.getNumberOfRoots());
    }

    /**
     * Tests {@link QuadraticEquation#getRealRoots()}.
     */
    @Test
    public void testGetRealRoots() {
        final QuadraticEquation positiveDiscriminantEquation = new QuadraticEquation(1, 3, 1.25);
        final double[] expectedRoots = positiveDiscriminantEquation.getRealRoots();
        assertEquals(-0.5D, expectedRoots[0], DELTA);
        assertEquals(-2.5D, expectedRoots[1], DELTA);
        final QuadraticEquation zeroDiscriminantEquation = new QuadraticEquation(1, 2, 1);
        final double expectedRoot = zeroDiscriminantEquation.getRealRoots()[0];
        assertEquals(-1D, expectedRoot, DELTA);
    }

    /**
     * Tests {@link QuadraticEquation#getRealRoots()} when the
     * discriminant is negative and no real roots exist.
     */
    @Test(expected = ArithmeticException.class)
    public void testGetRealRootsWithNegativeDiscriminant() {
        final QuadraticEquation negativeDiscriminantEquation = new QuadraticEquation(2, 2, 1);
        negativeDiscriminantEquation.getRealRoots();
    }

    /**
     * Tests {@link QuadraticEquation#getComplexRoots()}.
     */
    @Test
    public void testGetComplexRoots() {
        final QuadraticEquation negativeDiscriminantEquation = new QuadraticEquation(1, -10, 34);
        final Complex[] expectedRoots = negativeDiscriminantEquation.getComplexRoots();
        assertEquals(new Complex(5, 3), expectedRoots[0]);
        assertEquals(new Complex(5, -3), expectedRoots[1]);
    }

    /**
     * Tests {@link QuadraticEquation#getComplexRoots()} when the
     * discriminant is positive and no complex roots exist.
     */
    @Test(expected = ArithmeticException.class)
    public void testGetComplexRootsWithPositiveDiscriminant() {
        final QuadraticEquation positiveDiscriminantEquation = new QuadraticEquation(1, 3, 1.25);
        positiveDiscriminantEquation.getComplexRoots();
    }
}
