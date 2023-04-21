package org.deri.iris.terms.concrete;

import junit.framework.TestCase;

public class DoubleTest extends TestCase {

    public void testConstruct() {
        DoubleTerm dt = new DoubleTerm(0.123);
        assertEquals(0.123, dt.getValue().doubleValue());
    }

    public void testEquals() {
        checkEqual(+0.0, +0.0);
        checkEqual(-0.0, -0.0);
        checkEqual(1.1, 1.1);
        checkEqual(-1.1, -1.1);
        checkEqual(Double.NaN, Double.NaN);
        checkEqual(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        checkEqual(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        checkEqual(Double.MAX_VALUE, Double.MAX_VALUE);
        checkEqual(Double.MIN_VALUE, Double.MIN_VALUE);
    }

    public void testNotEqualPositiveAndNegativeZero() {
        checkNotEqual(+0.0, -0.0);
        checkNotEqual(-0.0, +0.0);
    }

    public void testNotEquals() {
        checkNotEqual(0.0, Double.MIN_VALUE);
        checkNotEqual(0.0, -Double.MIN_VALUE);
        checkNotEqual(Double.NaN, 0.0);
        checkNotEqual(Double.POSITIVE_INFINITY, 0.0);
        checkNotEqual(Double.NEGATIVE_INFINITY, 0.0);
        checkNotEqual(Double.NaN, 0.0);
    }

    public void testCompare() {
        checkLess(0.0, Double.MIN_VALUE);
        checkLess(-Double.MIN_VALUE, 0.0);
        checkLess(Double.NEGATIVE_INFINITY, 0.0);
        checkLess(0.0, Double.POSITIVE_INFINITY);
    }

    public void testHashCode() {
        checkSameHashCode(1.234, 1.234);
        checkSameHashCode(0.0, 0.0);
        checkSameHashCode(Double.NaN, Double.NaN);
    }

    private void checkSameHashCode(double f1, double f2) {
        DoubleTerm ft1 = new DoubleTerm(f1);
        DoubleTerm ft2 = new DoubleTerm(f2);
        assertEquals(ft1.hashCode(), ft2.hashCode());
    }

    private void checkEqual(double f1, double f2) {
        DoubleTerm ft1 = new DoubleTerm(f1);
        DoubleTerm ft2 = new DoubleTerm(f2);
        assertEquals(ft1, ft2);
        assertEquals(ft2, ft1);
        assertTrue(ft1.compareTo(ft2) == 0);
        assertTrue(ft2.compareTo(ft1) == 0);
    }

    private void checkNotEqual(double f1, double f2) {
        DoubleTerm ft1 = new DoubleTerm(f1);
        DoubleTerm ft2 = new DoubleTerm(f2);
        assertFalse(ft1.equals(ft2));
        assertFalse(ft2.equals(ft1));
        assertTrue(ft1.compareTo(ft2) != 0);
        assertTrue(ft2.compareTo(ft1) != 0);
    }

    private void checkLess(double f1, double f2) {
        DoubleTerm ft1 = new DoubleTerm(f1);
        DoubleTerm ft2 = new DoubleTerm(f2);
        assertTrue(ft1.compareTo(ft2) < 0);
        assertTrue(ft2.compareTo(ft1) > 0);
        assertFalse(ft1.equals(ft2));
        assertFalse(ft2.equals(ft1));
    }
}
