package net.sourceforge.align.filter.aligner.align.hmm.util.calculator.length;

import static net.sourceforge.align.filter.aligner.align.hmm.util.calculator.length.PoissonDistributionCalculator.factorial;
import static net.sourceforge.align.filter.aligner.align.hmm.util.calculator.length.PoissonDistributionCalculator.poissonDistribution;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PoissonDistributionCalculatorTest {

    @Test
    public void testFactorial() {
        assertEquals(1.0f, Math.exp(factorial(1)), 0.01f);
        assertEquals(2.0f, Math.exp(factorial(2)), 0.01f);
        assertEquals(6.0f, Math.exp(factorial(3)), 0.01f);
        assertEquals(24.0f, Math.exp(factorial(4)), 0.01f);
    }

    @Test
    public void testPoissonDistribution() {
        assertEquals(0.6065f, Math.exp(-poissonDistribution(0.5f, 0)), 0.0001f);
        assertEquals(0.3679f, Math.exp(-poissonDistribution(1.0f, 0)), 0.0001f);
        assertEquals(0.3679f, Math.exp(-poissonDistribution(1.0f, 1)), 0.0001f);
        assertEquals(0.1839f, Math.exp(-poissonDistribution(1.0f, 2)), 0.0001f);
        assertEquals(0.2707f, Math.exp(-poissonDistribution(2.0f, 1)), 0.0001f);
        assertEquals(0.1805f, Math.exp(-poissonDistribution(2.0f, 3)), 0.0001f);
    }
}
