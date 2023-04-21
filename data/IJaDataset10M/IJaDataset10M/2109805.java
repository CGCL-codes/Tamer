package net.sf.doodleproject.numerics4j.random;

import net.sf.doodleproject.numerics4j.NumericTestCase;

public class NegativeBinomialRandomVariableTest extends NumericTestCase {

    public void testConstructor() {
        testConstructorFailure(-1, 0.5);
        testConstructorFailure(0, 0.5);
        testConstructorFailure(10, Double.NaN);
        testConstructorFailure(10, -1.0);
        testConstructorFailure(10, 0.0);
        testConstructorSuccess(10, 0.5);
        testConstructorFailure(10, 1.0);
        testConstructorFailure(10, 2.0);
    }

    private void testConstructorFailure(int n, double p) {
        try {
            new NegativeBinomialRandomVariable(n, p);
            fail();
        } catch (IllegalArgumentException ex) {
        }
    }

    private void testConstructorSuccess(int n, double p) {
        try {
            new NegativeBinomialRandomVariable(n, p);
        } catch (IllegalArgumentException ex) {
            fail();
        }
    }

    public void testNextRandomVariable() {
        int[] a = new int[100];
        int[] b = new int[a.length];
        int x;
        NegativeBinomialRandomVariable rv = new NegativeBinomialRandomVariable();
        for (int i = 0; i < a.length; ++i) {
            a[i] = x = rv.nextRandomVariable();
            assertTrue(0 <= x);
            assertTrue(x <= Integer.MAX_VALUE);
        }
        for (int i = 0; i < a.length; ++i) {
            b[i] = x = rv.nextRandomVariable();
            assertTrue(0 <= x);
            assertTrue(x <= Integer.MAX_VALUE);
        }
        assertNotRelativelyEquals(a, b);
    }
}
