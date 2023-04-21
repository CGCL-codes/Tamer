package net.sf.unofficialjunit.matcher;

import static net.sf.unofficialjunit.matcher.UnofficialMatcher.isAlmostEqual;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

/**
 * @author georg.thimm
 * 
 */
public class AlmostEqualTest {

    /**
     * test that for some examples the isAlmostEqual-matcher passes.
     **/
    @Test
    public void testPasses() {
        assertThat(1d, isAlmostEqual(1d, 0.0));
        assertThat(1, isAlmostEqual(1, 0));
        assertThat(1, isAlmostEqual(-1, 2));
        assertThat(-3, isAlmostEqual(-1, 2));
        assertThat(-3.5, isAlmostEqual(-3, 1));
        assertThat(9110.2, isAlmostEqual(9108.8, 3));
        assertThat(0.001, isAlmostEqual(1.0, 1));
        assertThat(1, isAlmostEqual(1, 0.1));
        assertThat(1, not(isAlmostEqual(2, 0.999)));
        assertThat(1, not(isAlmostEqual(0, 0.999)));
        assertThat(1, isAlmostEqual(2, 1.0));
    }

    /**
     * test that the assertion fails if the tolerance is insufficient wide.
     **/
    @Test(expected = AssertionError.class)
    public final void test1IsNotAlmost2_a() {
        assertThat(1, isAlmostEqual(2, 0));
    }

    /**
     * test that the assertion fails if the tolerance is insufficient wide.
     **/
    @Test(expected = AssertionError.class)
    public final void test1IsNotAlmost2_b() {
        assertThat(1.1, isAlmostEqual(1.2, 0.05));
    }

    /**
     * test that the assertion fails if the tolerance is insufficient wide.
     **/
    @Test(expected = AssertionError.class)
    public final void test1IsNotAlmost2_c() {
        assertThat(1.1, isAlmostEqual(1.0, 0.05));
    }

    /**
     * test that the assertion fails if the tolerance is insufficient wide.
     **/
    @Test(expected = AssertionError.class)
    public final void test_a() {
        assertThat(100L, isAlmostEqual(110, 9));
    }

    /**
     * test that the assertion fails if the tolerance is insufficient wide.
     **/
    @Test(expected = AssertionError.class)
    public final void test_b() {
        assertThat(100L, isAlmostEqual(110, 9.1));
    }

    /**
     * test an assertion error is thrown if the tested value is NaN.
     */
    @Test(expected = AssertionError.class)
    public final void testForRefValueIsNaN() {
        assertThat("NaN as ref value", Double.NaN, isAlmostEqual(0.0, 1.0));
    }

    /**
     * test an assertion error is thrown if the reference value is NaN.
     */
    @Test(expected = AssertionError.class)
    public final void testForTestValueIsNaN() {
        assertThat("NaN as Test value", 0.0, isAlmostEqual(Double.NaN, 1.0));
    }

    /**
     * test an assertion error is thrown if the tolerance value is NaN.
     */
    @Test(expected = AssertionError.class)
    public final void testForToleranceValueIsNaN() {
        assertThat("NaN as Tolerance value", 0.0, isAlmostEqual(0.0, Double.NaN));
    }

    /**
     * test an assertion error is thrown if the tested value is infinity.
     */
    @Test(expected = AssertionError.class)
    public void testForRefValueIsInfinity() {
        assertThat("NaN as ref value", Double.NEGATIVE_INFINITY, isAlmostEqual(0.0, 1.0));
    }

    /**
     * test an assertion error is thrown if the reference value is infinity.
     */
    @Test(expected = AssertionError.class)
    public void testForTestValueIsInfinity() {
        assertThat("NaN as Test value", 0.0, isAlmostEqual(Double.NEGATIVE_INFINITY, 1.0));
    }

    /**
     * test an assertion error is thrown if the tolerance value is infinity.
     */
    @Test(expected = AssertionError.class)
    public void testForToleranceValueIsInfinity() {
        assertThat("NaN as Tolerance value", 0.0, isAlmostEqual(0.0, Double.NEGATIVE_INFINITY));
    }

    /**
     * test {@link AtomicInteger}.
     */
    @Test
    public final void testAtomicInteger() {
        AtomicInteger zero = new AtomicInteger();
        AtomicInteger a = new AtomicInteger();
        a.addAndGet(1);
        assertThat(a, isAlmostEqual(zero, 1));
        a.addAndGet(5);
        assertThat(a, not(isAlmostEqual(zero, 1)));
    }

    /**
     * BigIntegers are not handled by the test - therefore an
     * {@link AssertionError} is thrown.
     */
    @Test(expected = AssertionError.class)
    public final void testBigInteger() {
        assertThat(new BigInteger("1111111"), isAlmostEqual(new BigInteger("1111111"), 1));
    }

    /**
     * ensure that if the tolerance is negative, an {@link AssertionError} is
     * thrown.
     */
    @Test(expected = AssertionError.class)
    public final void testToleranceNotNegative() {
        isAlmostEqual(1, -1);
    }

    /**
     * the matcher may only be build if the parameters to isAlmostEqual() are of
     * certain types.
     */
    @Test
    public final void testRejectInvalidArgumentTypes() {
        final String error = "isAlmostEqual should not accept ";
        for (Number n : new Number[] { BigInteger.ONE, BigDecimal.ONE }) {
            try {
                isAlmostEqual(n, 0);
                fail(error + n.getClass().getCanonicalName() + " as first parameter");
            } catch (AssertionError e) {
                if (e.getMessage().startsWith(error)) {
                    throw new AssertionError(e);
                }
            }
            try {
                isAlmostEqual(0, n);
                fail(error + n.getClass().getCanonicalName() + " as second parameter");
            } catch (AssertionError e) {
                if (e.getMessage().startsWith(error)) {
                    throw new AssertionError(e);
                }
            }
            try {
                assertThat(n, isAlmostEqual(1, 1));
                fail(error + n.getClass().getCanonicalName() + " in the matcher");
            } catch (AssertionError e) {
                if (e.getMessage().startsWith(error)) {
                    throw new AssertionError(e);
                }
            }
        }
    }
}
