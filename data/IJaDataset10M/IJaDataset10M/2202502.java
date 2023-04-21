package net.sf.staccatocommons.check;

import static junit.framework.Assert.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import net.sf.staccatocommons.check.internal.SizeAwareTypes;
import net.sf.staccatocommons.defs.ContainsAware;
import net.sf.staccatocommons.defs.EmptyAware;
import net.sf.staccatocommons.defs.SizeAware;
import net.sf.staccatocommons.defs.type.NumberType;
import net.sf.staccatocommons.defs.type.SizeAwareType;
import net.sf.staccatocommons.testing.junit.jmock.JUnit4MockObjectTestCase;
import org.jmock.Expectations;
import org.junit.Test;

/**
 * Test for {@link Check}
 * 
 * @author flbulgarelli
 * 
 */
public class CheckUnitTest extends JUnit4MockObjectTestCase {

    private static final String VAR_NAME = "var";

    private Check<IllegalArgumentException> c = Ensure.that();

    /**
   * Test method for {@link Check#isTrue(String, boolean)}
   */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckIsTrue() {
        c.isTrue(VAR_NAME, false);
    }

    /**
   * Test method for {@link Check#matches(String, String, String)}
   */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckMatches() {
        c.matches(VAR_NAME, "hello", ".ola.");
    }

    /**
   * Test method for {@link Check#matches(String, String, Pattern)}
   */
    @Test(expected = IllegalArgumentException.class)
    public void testCheckMatchesPattern() {
        c.matches(VAR_NAME, "hello", Pattern.compile(".ola."));
    }

    /** Test for {@link Check#isNotNegative(String, Long)} */
    @Test(expected = IllegalArgumentException.class)
    public void testisNotNegativeLong() {
        c.isNotNegative(VAR_NAME, -5L);
    }

    /***/
    @Test(expected = IllegalArgumentException.class)
    public void testisNotNegativeInt() {
        c.isNotNegative(VAR_NAME, -5);
    }

    /***/
    @Test(expected = IllegalArgumentException.class)
    public void testisNotNegativeDouble() {
        c.isNotNegative(VAR_NAME, -5.9);
    }

    /***/
    @Test(expected = IllegalArgumentException.class)
    public void testisNotNegativeFloat() {
        c.isNotNegative(VAR_NAME, -5f);
    }

    /***/
    @Test(expected = IllegalArgumentException.class)
    public void testisNotNegativeBigDecimal() {
        c.isNotNegative(VAR_NAME, BigDecimal.valueOf(-9.69));
    }

    /***/
    @Test(expected = IllegalArgumentException.class)
    public void testisNotNegativeBigInteger() {
        c.isNotNegative(VAR_NAME, BigInteger.valueOf(-9));
    }

    /**
   * Test for {@link Check#isNotNegative(String, BigDecimal)} and similar
   * methods
   */
    @Test
    public void testisNotNegativeZero() {
        c.isNotNegative(VAR_NAME, 0L);
        c.isNotNegative(VAR_NAME, 0);
        c.isNotNegative(VAR_NAME, 0.0);
        c.isNotNegative(VAR_NAME, 0.0f);
        c.isNotNegative(VAR_NAME, BigDecimal.ZERO);
        c.isNotNegative(VAR_NAME, BigInteger.ZERO);
    }

    /**
   * test for {@link Check#isNotNegative(String, BigDecimal)} and similar
   */
    @Test
    public void testNonNegativePositive() {
        c.isNotNegative(VAR_NAME, 9).isNotNegative(VAR_NAME, 9L).isNotNegative(VAR_NAME, 9f).isNotNegative(VAR_NAME, 9.0).isNotNegative(VAR_NAME, BigDecimal.valueOf(69.62)).isNotNegative(VAR_NAME, BigInteger.valueOf(1200));
    }

    /**
   * Test method for {@link Check#isNotEmpty(String, Iterable)}
   */
    @Test
    public void testNotEmpty() {
        c.isNotEmpty(VAR_NAME, Arrays.asList("foo", "bar")).isNotEmpty(VAR_NAME, Collections.singleton(6)).isNotEmpty(VAR_NAME, "hola").isNotEmpty(VAR_NAME, (Iterable<?>) Collections.singletonList("sayounara")).isNotEmpty(VAR_NAME, Collections.singletonMap(10, 50)).isNotEmpty(VAR_NAME, new EmptyAware() {

            public boolean isEmpty() {
                return false;
            }
        });
    }

    /**
   * Test for {@link Check#isNotEmpty(String, CharSequence)}
   */
    @Test(expected = IllegalArgumentException.class)
    public void testisNotEmptyCharSequenceFail() {
        c.isNotEmpty(VAR_NAME, "");
    }

    /**
   * Test for {@link Check#isNotEmpty(String, Collection)}
   */
    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionFail() {
        c.isNotEmpty(VAR_NAME, Collections.emptyList());
    }

    /**
   * Test for {@link Check#isPositive(String, BigDecimal)} and others
   */
    @Test
    public void testPositive() {
        c.isPositive(VAR_NAME, 9).isPositive(VAR_NAME, 9L).isPositive(VAR_NAME, 9f).isPositive(VAR_NAME, 9.0).isPositive(VAR_NAME, BigDecimal.valueOf(69.62)).isPositive(VAR_NAME, BigInteger.valueOf(1200));
    }

    /**
   * Test for {@link Check#isPositive(String, int)}
   */
    @Test(expected = IllegalArgumentException.class)
    public void testPositiveNegative() {
        c.isPositive(VAR_NAME, -5);
    }

    /**
   * Test for {@link Check#isPositive(String, int)}
   */
    @Test(expected = IllegalArgumentException.class)
    public void testPositiveZero() {
        c.isPositive(VAR_NAME, 0);
    }

    /**
   * Test for {@link Check#isNotZero(String, Object, NumberType)} and
   * {@link Check#isZero(String, Object, NumberType)}
   */
    @Test
    public void testNotZero() {
        final NumberType mock = mock(NumberType.class);
        checking(new Expectations() {

            {
                one(mock).isZero(5);
                will(returnValue(false));
                one(mock).isZero(0);
                will(returnValue(true));
            }
        });
        c.isNotZero(VAR_NAME, 5, mock);
        c.isZero(VAR_NAME, 0, mock);
    }

    /**
   * Test for {@link Check#isPositive(String, int)} on failure
   */
    @Test(expected = IllegalArgumentException.class)
    public void testPositiveIntegerFail() {
        c.isPositive(VAR_NAME, -50);
    }

    /**
   * Test for {@link Check#isPositive(String, int)} on failure
   */
    @Test(expected = IllegalArgumentException.class)
    public void testPositiveNullFail() {
        c.isPositive(VAR_NAME, (BigDecimal) null);
    }

    /**
   * Test for {@link Check#isPositive(String, int)} on failure
   */
    @Test(expected = IllegalArgumentException.class)
    public void testPositiveBigIntegerFail() {
        c.isPositive(VAR_NAME, BigInteger.valueOf(-10));
    }

    /**
   * Test for the isSize family of methods
   */
    @Test
    public void testSize() {
        c.isSize(VAR_NAME, Arrays.asList(9, 96), 2).isSize(VAR_NAME, "Hello", 5).isSize(VAR_NAME, Collections.singletonMap("", 0), 1).isSize(VAR_NAME, new SizeAware() {

            public int size() {
                return 5;
            }

            public boolean isEmpty() {
                return false;
            }
        }, 5);
    }

    /** Test for {@link Check#isSize(String, Object, int, SizeAwareType)} */
    @Test(expected = IllegalArgumentException.class)
    public void testSizeFail() throws Exception {
        c.isSize(VAR_NAME, Arrays.asList(4, 5, 6), 2);
    }

    /**
   * Test for {@link Check#isMinSize(String, Object, int, SizeAwareType)} and
   * {@link Check#isMaxSize(String, Object, int, SizeAwareType)}
   * 
   * @throws Exception
   */
    @Test
    public void testMinMaxSize() throws Exception {
        List<Integer> l = Arrays.asList(10, 20, 30);
        c.isMinSize(VAR_NAME, l, 2, SizeAwareTypes.COLLECTION).isMinSize(VAR_NAME, l, 3, SizeAwareTypes.COLLECTION).isMaxSize(VAR_NAME, l, 4, SizeAwareTypes.COLLECTION).isMaxSize(VAR_NAME, l, 3, SizeAwareTypes.COLLECTION);
    }

    /**
   * Test for {@link Check#isMinSize(String, Object, int, SizeAwareType)}
   * 
   * @throws Exception
   */
    @Test(expected = IllegalArgumentException.class)
    public void testMinSizeFail() throws Exception {
        c.isMinSize(VAR_NAME, Arrays.asList(4), 2, SizeAwareTypes.COLLECTION);
    }

    /**
   * Test for {@link Check#isMinSize(String, Object, int, SizeAwareType)}
   * 
   * @throws Exception
   */
    @Test(expected = IllegalArgumentException.class)
    public void testMaxSizeFail() throws Exception {
        c.isMaxSize(VAR_NAME, Arrays.asList(4, 1, 3), 2, SizeAwareTypes.COLLECTION);
    }

    /**
   * Test for the isEmpty family of methods
   */
    @Test
    public void testEmpty() {
        c.isEmpty(VAR_NAME, Collections.emptyList()).isEmpty(VAR_NAME, Collections.<String>emptyList()).isEmpty(VAR_NAME, Collections.emptyMap()).isEmpty(VAR_NAME, (Iterable<?>) Collections.emptyList()).isEmpty(VAR_NAME, "").isEmpty(VAR_NAME, new EmptyAware() {

            public boolean isEmpty() {
                return true;
            }
        });
    }

    /**
   * Test for {@link Check#isInstanceOf(String, Object, Class)}
   */
    @Test
    public void testIsInstanceOf() {
        c.isInstanceOf(VAR_NAME, 5, Number.class);
    }

    /** Test for {@link Check#isTrue(String, boolean)} */
    @Test
    public void testIsTrue() {
        c.isTrue(VAR_NAME, true);
    }

    /**
   * Test for {@link Check#matches(String, String, Pattern)} and similar
   */
    @Test
    public void testMatches() {
        c.matches(VAR_NAME, "Hello", ".*ell.");
        c.matches(VAR_NAME, "Hello", Pattern.compile(".*ell."));
    }

    /**
   * test for {@link Check#isNotNull(String, Object)}
   */
    @Test
    public void testNotNull() {
        c.isNotNull(VAR_NAME, new Object());
    }

    /**
   * test for {@link Check#isNotNull(String, Object)}
   */
    @Test(expected = IllegalArgumentException.class)
    public void testNotNullFail() {
        c.isNotNull(VAR_NAME, null);
    }

    /**
   * Test for {@link Check#isNull(String, Object)} on failure
   */
    @Test(expected = IllegalArgumentException.class)
    public void testNullFail() {
        c.isNull(VAR_NAME, new Object());
    }

    /**
   * Test for {@link Check#isNull(String, Object)}
   */
    @Test
    public void testNull() {
        c.isNull(VAR_NAME, null);
    }

    /**
   * Test for {@link Check#fail(String, Object, String, Object...)}
   */
    @Test(expected = IllegalArgumentException.class)
    public void testFail() {
        c.fail(VAR_NAME, "Foo", "Should be palindromic");
    }

    /**
   * Test for
   * {@link Check#isBetween(String, Comparable, Comparable, Comparable)} method
   * on failure
   */
    @Test(expected = IllegalArgumentException.class)
    public void testIsBetweenFail() throws Exception {
        c.isBetween(VAR_NAME, 8, 10, 90);
    }

    /**
   * Test for
   * {@link Check#isBetween(String, Comparable, Comparable, Comparable)}
   */
    @Test(expected = IllegalArgumentException.class)
    public void testIsBetween() throws Exception {
        c.isBetween(VAR_NAME, "big", "bang", "!");
    }

    /**
   * Test for {@link Check#isLessThan(String, Comparable, Comparable)},
   * {@link Check#isLessThanOrEqualTo(String, Comparable, Comparable)},
   * {@link Check#isGreaterThan(String, Comparable, Comparable)} and
   * {@link Check#isGreaterThanOrEqualTo(String, Comparable, Comparable)}
   * 
   * @throws Exception
   */
    @Test
    public void testCompare() throws Exception {
        c.isGreaterThan(VAR_NAME, 90, 50).isGreaterThanOrEqualTo(VAR_NAME, 90, 90).isGreaterThanOrEqualTo(VAR_NAME, 90, 20).isLessThan(VAR_NAME, "foo", "zbar").isLessThanOrEqualTo(VAR_NAME, "baz", "baz").isLessThanOrEqualTo(VAR_NAME, "foobar", "zbaz");
    }

    /**
   * Test for {@link Check#isGreaterThan(String, Comparable, Comparable)}
   * 
   * @throws Exception
   */
    @Test(expected = IllegalArgumentException.class)
    public void testGtCompareFail() throws Exception {
        c.isGreaterThan(VAR_NAME, 50, 50);
    }

    /**
   * Test for {@link Check#isLessThan(String, Comparable, Comparable)}
   * 
   * @throws Exception
   */
    @Test(expected = IllegalArgumentException.class)
    public void testLtCompareFail() throws Exception {
        c.isLessThan(VAR_NAME, 50, 50);
    }

    /**
   * test for {@link Check#contains(String, ContainsAware, Object)}
   * 
   * @throws Exception
   */
    @Test
    public void testContains() throws Exception {
        c.contains(VAR_NAME, new ContainsAware<Integer>() {

            public boolean contains(Integer element) {
                assertEquals((Integer) 5, element);
                return true;
            }
        }, 5);
    }

    /** Test for {@link Check#isIn(String, Object, ContainsAware)} */
    @Test
    public void testIn() throws Exception {
        c.isIn(VAR_NAME, 10, new ContainsAware<Integer>() {

            public boolean contains(Integer element) {
                assertEquals((Integer) 10, element);
                return true;
            }
        });
    }

    /** Test for {@link Check#isIn(String, Object, ContainsAware)} */
    @Test(expected = IllegalArgumentException.class)
    public void testInFail() throws Exception {
        c.isIn(VAR_NAME, 10, new ContainsAware<Integer>() {

            public boolean contains(Integer element) {
                return false;
            }
        });
    }
}
