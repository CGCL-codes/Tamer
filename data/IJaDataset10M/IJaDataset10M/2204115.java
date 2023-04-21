package net.sourceforge.retroweaver.tests;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigDecimalTest extends AbstractTest {

    public void testInt() {
        int i = Integer.MAX_VALUE;
        BigDecimal bd = new BigDecimal(i);
        assertEquals("testInt", bd.toString(), Integer.toString(i));
    }

    private BigDecimal identity(BigDecimal d) {
        return d;
    }

    public void testIntInCall() {
        int i = Integer.MAX_VALUE;
        BigDecimal bd = identity(new BigDecimal(i));
        assertEquals("testInt", bd.toString(), Integer.toString(i));
    }

    public void testLong() {
        long l = Long.MAX_VALUE;
        BigDecimal bd = new BigDecimal(l);
        assertEquals("testLong", bd.toString(), Long.toString(l));
    }

    public void testStaticFields() {
        assertEquals("testStaticFields", "0", BigDecimal.ZERO.toString());
        assertEquals("testStaticFields", "1", BigDecimal.ONE.toString());
        assertEquals("testStaticFields", "10", BigDecimal.TEN.toString());
        assertEquals("testStaticFields", "10", BigInteger.TEN.toString());
    }

    public void testValueOf() {
        double d = 1.23456d;
        BigDecimal bd = BigDecimal.valueOf(d);
        assertEquals("testValueOf", Double.toString(d), bd.toString());
    }

    public void testLog10() {
        assertEquals("log10 10", 1.0d, Math.log10(10.0d));
        assertEquals("log10 100", 2.0d, Math.log10(100.0d));
    }

    public void testDivide() {
        BigDecimal d1 = new BigDecimal("5");
        BigDecimal d2 = new BigDecimal("2");
        assertEquals("testDivide", new BigDecimal("2.5"), d1.divide(d2));
    }

    public void testDivideException() {
        try {
            BigDecimal d1 = new BigDecimal("5");
            d1 = d1.divide(BigDecimal.ZERO);
            fail("ArithmeticException expected");
        } catch (ArithmeticException e) {
        }
    }

    public void testDivideAndRemainder() {
        BigDecimal d1 = new BigDecimal("5");
        BigDecimal d2 = new BigDecimal("2");
        BigDecimal r[] = d1.divideAndRemainder(d2);
        assertEquals("testDivideAndRemainder size", 2, r.length);
        assertEquals("testDivideAndRemainder result", new BigDecimal("2"), r[0]);
        assertEquals("testDivideAndRemainder remainder", new BigDecimal("1"), r[1]);
    }

    public void testDivideToIntegralValue() {
        BigDecimal d1 = new BigDecimal("5");
        BigDecimal d2 = new BigDecimal("2");
        assertEquals("testDivideToIntegralValue", new BigDecimal("2"), d1.divideToIntegralValue(d2));
    }
}
