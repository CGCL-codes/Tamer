package test.org.mandarax.lib.math;

import org.mandarax.kernel.Fact;
import org.mandarax.lib.math.DoubleArithmetic;

/**
 * Test case for double arithmetic.<br>
 * <em>tested predicate(s): </em>DoubleArithmetic.NOT_EQUAL<br>
 * <em>tested function(s): </em>-<br>
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 1.6
 */
public class DoubleArithmeticTest2 extends DoubleArithmeticTest {

    /**
     * Constructor.
     */
    public DoubleArithmeticTest2() {
        super();
    }

    /**
     * Get the expected number of generated facts.
     * @return the number of facts expected
     */
    protected int getExpected() {
        return 20;
    }

    /**
     * Get the query fact.
     * @return a query fact.
     */
    protected Fact getQueryFact() {
        return buildFact(DoubleArithmetic.NOT_EQUAL, "x", "y");
    }

    /**
     * Get the set of numbers used for testing.
     * @return an array of doubles
     */
    protected double[] getTestSet() {
        double[] array = { -1, -0.5, 0, 0.5, 1 };
        return array;
    }
}
