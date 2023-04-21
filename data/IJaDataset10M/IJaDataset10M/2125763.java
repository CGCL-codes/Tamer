package org.josef.test.science.math;

import static org.josef.annotations.Status.Stage.PRODUCTION;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.math.BigDecimal;
import org.josef.annotations.Review;
import org.josef.annotations.Status;
import org.josef.science.math.CBigDecimal;
import org.junit.Test;

/**
 * JUnit test class for class {@link CBigDecimalTest}.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 2840 $
 */
@Status(stage = PRODUCTION)
@Review(by = "Kees Schotanus", at = "2009-09-28")
public final class CBigDecimalTest {

    /**
     * Tests {@link CBigDecimal#squareRoot(BigDecimal, int)}.
     */
    @Test
    public void testSquareRootSmallNumber() {
        final int iterations = 1000;
        final int accuracy = 10;
        final double allowedError = Math.pow(10, -accuracy);
        for (int i = 0; i < iterations; ++i) {
            final double squareRoot = CBigDecimal.squareRoot(new CBigDecimal(i), accuracy).doubleValue();
            final double absoluteError = Math.abs(squareRoot - Math.sqrt(i));
            if (absoluteError > allowedError) {
                fail(String.format("squareRoot(%d)=%1.15g not within expected accuracy", i, squareRoot));
            }
        }
    }

    /**
     * Tests {@link CBigDecimal#squareRoot(int)}.
     * <br>Calculates the root of a large number with a lot of decimals.
     */
    @Test
    public void testSquareRootLargeNumber() {
        final String largeNumberAsString = "112233445566778899";
        final BigDecimal largeNumber = new CBigDecimal(largeNumberAsString + "." + largeNumberAsString);
        final BigDecimal largeNumberSquared = largeNumber.multiply(largeNumber);
        final BigDecimal root = CBigDecimal.squareRoot(largeNumberSquared, largeNumberAsString.length());
        final double allowedError = Math.pow(10, -largeNumberAsString.length());
        final double absoluteError = root.subtract(largeNumber).abs().doubleValue();
        assertTrue(absoluteError < allowedError);
    }
}
