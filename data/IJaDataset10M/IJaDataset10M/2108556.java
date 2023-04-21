package org.zkoss.zss.engine.fn;

import junit.framework.TestCase;
import org.zkoss.zss.engine.fn.MathFns;

/**
 * 
 * @author Jeff
 *
 */
public class MathFnsTest extends TestCase {

    public MathFnsTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAbs() {
        Double[] d = { new Double(-1.00) };
        assertEquals(1.00, ((Number) MathFns.mathAbs(d, null)).doubleValue(), 0d);
    }

    public void testAcos() {
        Double[] d = { new Double(-0.5) };
        double e = Math.acos(-0.5);
        assertEquals(e, ((Number) MathFns.mathAcos(d, null)).doubleValue(), 0d);
    }

    public void testAcosh() {
        Double[] d = { new Double(1) };
        double e = 0.0;
        assertEquals(e, ((Number) MathFns.mathAcosh(d, null)).doubleValue(), 0d);
    }

    public void testAsin() {
        Double[] d = { new Double(-0.5) };
        double e = Math.asin(-0.5);
        assertEquals(e, ((Number) MathFns.mathAsin(d, null)).doubleValue(), 0d);
    }

    public void testAsinh() {
        Double[] d = { new Double(-2.5) };
        double e = -1.64723;
        assertEquals(e, ((Number) MathFns.mathAsinh(d, null)).doubleValue(), 0.000005d);
    }

    public void testAtan() {
        Double[] d = { new Double(1) };
        double e = Math.PI / 4;
        assertEquals(e, ((Number) MathFns.mathAtan(d, null)).doubleValue(), 0d);
    }

    public void testAtan2() {
        Double[] d = { new Double(1), new Double(1) };
        double e = Math.PI / 4;
        assertEquals(e, ((Number) MathFns.mathAtan2(d, null)).doubleValue(), 0d);
    }

    public void testAtanh() {
        Double[] d = { new Double(-0.1) };
        double e = -0.10034;
        assertEquals(e, ((Number) MathFns.mathAtanh(d, null)).doubleValue(), 0.000005d);
    }

    public void testCeiling() {
        Double[] d = { new Double(2.5), new Double(1) };
        double e = 3;
        assertEquals(e, ((Number) MathFns.mathCeiling(d, null)).doubleValue(), 0d);
        Double[] d1 = { new Double(-2.5), new Double(-2) };
        double e1 = -4;
        assertEquals(e1, ((Number) MathFns.mathCeiling(d1, null)).doubleValue(), 0d);
        Double[] d2 = { new Double(1.5), new Double(0.1) };
        double e2 = 1.5;
        assertEquals(e2, ((Number) MathFns.mathCeiling(d2, null)).doubleValue(), 0d);
        Double[] d3 = { new Double(0.234), new Double(0.01) };
        double e3 = 0.24;
        assertEquals(e3, ((Number) MathFns.mathCeiling(d3, null)).doubleValue(), 0d);
    }

    public void testCombin() {
        Double[] d = { new Double(8), new Double(2) };
        int e = 28;
        assertEquals(e, ((Number) MathFns.mathCombin(d, null)).doubleValue(), 0d);
    }

    public void testCos() {
        Double[] d = { new Double(60 * Math.PI / 180) };
        double e = Math.cos(60 * Math.PI / 180);
        assertEquals(e, ((Number) MathFns.mathCos(d, null)).doubleValue(), 0d);
    }

    public void testCosh() {
        Double[] d = { new Double(60 * Math.PI / 180) };
        double e = Math.cosh(60 * Math.PI / 180);
        assertEquals(e, ((Number) MathFns.mathCosh(d, null)).doubleValue(), 0d);
    }

    public void testDegrees() {
        Double[] d = { new Double(Math.PI) };
        double e = 180;
        assertEquals(e, ((Number) MathFns.mathDegrees(d, null)).doubleValue(), 0d);
    }

    public void testEven() {
        Double[] d = { new Double(1.5) };
        int e = 2;
        assertEquals(e, ((Number) MathFns.mathEven(d, null)).doubleValue(), 0d);
    }

    public void testExp() {
        Double[] d = { new Double(1) };
        double e = Math.exp(1);
        assertEquals(e, ((Number) MathFns.mathExp(d, null)).doubleValue(), 0d);
    }

    public void testFact() {
        Double[] d = { new Double(5) };
        double e = 120.0;
        assertEquals(e, ((Number) MathFns.mathFact(d, null)).doubleValue(), 0d);
    }

    public void testFactdouble() {
        Double[] d = { new Double(6) };
        double e = 48.0;
        assertEquals(e, ((Number) MathFns.mathFactdouble(d, null)).doubleValue(), 0d);
        Double[] d1 = { new Double(7) };
        double e1 = 105.0;
        assertEquals(e1, ((Number) MathFns.mathFactdouble(d1, null)).doubleValue(), 0d);
    }

    public void testFloor() {
        Double[] d = { new Double(2.5), new Double(1) };
        double e = 2;
        assertEquals(e, ((Number) MathFns.mathFloor(d, null)).doubleValue(), 0d);
        Double[] d1 = { new Double(-2.5), new Double(-2) };
        double e1 = -2;
        assertEquals(e1, ((Number) MathFns.mathFloor(d1, null)).doubleValue(), 0d);
        Double[] d2 = { new Double(1.5), new Double(0.1) };
        double e2 = 1.5;
        assertEquals(e2, ((Number) MathFns.mathFloor(d2, null)).doubleValue(), 0d);
        Double[] d3 = { new Double(0.234), new Double(0.01) };
        double e3 = 0.23;
        assertEquals(e3, ((Number) MathFns.mathFloor(d3, null)).doubleValue(), 0d);
    }

    public void testGcd() {
        Double[] d = { new Double(5), new Double(2) };
        double e = 1;
        assertEquals(e, ((Number) MathFns.mathGcd(d, null)).doubleValue(), 0d);
        Double[] d1 = { new Double(24.999999), new Double(36.89) };
        double e1 = 12;
        assertEquals(e1, ((Number) MathFns.mathGcd(d1, null)).doubleValue(), 0d);
        Double[] d2 = { new Double(7), new Double(1) };
        double e2 = 1;
        assertEquals(e2, ((Number) MathFns.mathGcd(d2, null)).doubleValue(), 0d);
        Double[] d3 = { new Double(5), new Double(0) };
        double e3 = 5;
        assertEquals(e3, ((Number) MathFns.mathGcd(d3, null)).doubleValue(), 0d);
    }

    public void testInt() {
        Double[] d = { new Double(3.4) };
        double e = 3;
        assertEquals(e, ((Number) MathFns.mathInt(d, null)).doubleValue(), 0d);
    }

    public void testLcm() {
        Double[] d = { new Double(5), new Double(2) };
        double e = 10;
        assertEquals(e, ((Number) MathFns.mathLcm(d, null)).doubleValue(), 0d);
        Double[] d1 = { new Double(24.999999), new Double(36.89) };
        double e1 = 72;
        assertEquals(e1, ((Number) MathFns.mathLcm(d1, null)).doubleValue(), 0d);
    }

    public void testLn() {
        Double[] d = { new Double(2) };
        double e = 0.693147;
        assertEquals(e, ((Number) MathFns.mathLn(d, null)).doubleValue(), 0.0000005d);
    }

    public void testLog() {
        Double[] d = { new Double(2) };
        double e = 0.30103;
        assertEquals(e, ((Number) MathFns.mathLog(d, null)).doubleValue(), 0.000005d);
    }

    public void testLog10() {
        Double[] d = { new Double(2) };
        double e = 0.30103;
        assertEquals(e, ((Number) MathFns.mathLog10(d, null)).doubleValue(), 0.000005d);
    }

    public void testMdeterm() {
        Object matrix22[][] = { { new Double(0), new Double(1) }, { new Double(2), new Double(3) } };
        double result = -2;
        assertEquals(result, ((Number) MathFns.mathMdeterm(matrix22, null)).doubleValue(), 0d);
        Object matrix33[][] = { { new Double(9), new Double(8), new Double(7) }, { new Double(6), new Double(5), new Double(4) }, { new Double(2), new Double(2), new Double(1) } };
        result = 3;
        assertEquals(result, ((Number) MathFns.mathMdeterm(matrix33, null)).doubleValue(), 0d);
        Object matrix33_1[][] = { { new Double(9), new Double(8), new Double(7) }, { new Double(6), new Double(5), new Double(4.3) }, { new Double(2), new Double(2), new Double(1) } };
        result = 2.4;
        assertEquals(result, ((Number) MathFns.mathMdeterm(matrix33_1, null)).doubleValue(), 0d);
        Object matrix33_1_1[][] = { { new Double(9.1), new Double(8.2), new Double(7.3) }, { new Double(6.4), new Double(5.5), new Double(4.6) }, { new Double(3), new Double(2.8), new Double(1.9) } };
        result = 1.701;
        assertEquals(result, ((Number) MathFns.mathMdeterm(matrix33_1_1, null)).doubleValue(), 0d);
    }

    private boolean compareMatrix(Object[][] m1, Object[][] m2, double delta) {
        if (m1.length != m2.length || m1[0].length != m2[0].length) {
            return false;
        } else {
            for (int row = 0; row < m1.length; row++) for (int column = 0; column < m1[0].length; column++) if (java.lang.Math.abs(((Double) m1[row][column]).doubleValue() - ((Double) m2[row][column]).doubleValue()) > delta) return false;
            return true;
        }
    }

    public void testMinverse() {
        Object matrix33[][] = { { new Double(1), new Double(0), new Double(0) }, { new Double(0), new Double(1), new Double(0) }, { new Double(0), new Double(0), new Double(1) } };
        Object result33[][] = { { new Double(1), new Double(0), new Double(0) }, { new Double(0), new Double(1), new Double(0) }, { new Double(0), new Double(0), new Double(1) } };
        Object result = MathFns.mathMinverse(matrix33, null);
        assertTrue(compareMatrix(result33, (Object[][]) result, 0d));
        Object matrix33withDecimal[][] = { { new Double(1), new Double(1), new Double(1) }, { new Double(4), new Double(0), new Double(6) }, { new Double(7), new Double(8), new Double(9) } };
        Object result33withDecimal[][] = { { new Double(4.8), new Double(0.1), new Double(-0.6) }, { new Double(-0.6), new Double(-0.2), new Double(0.2) }, { new Double(-3.2), new Double(0.1), new Double(0.4) } };
        result = MathFns.mathMinverse(matrix33withDecimal, null);
        assertTrue(compareMatrix(result33withDecimal, (Object[][]) result, 0d));
    }

    public void testMmult() {
        Object faciendMatrix33[][] = { { new Double(1), new Double(0), new Double(0) }, { new Double(0), new Double(1), new Double(0) }, { new Double(0), new Double(0), new Double(1) } };
        Object multiplierMatrix33[][] = { { new Double(1), new Double(0), new Double(0) }, { new Double(0), new Double(1), new Double(0) }, { new Double(0), new Double(0), new Double(1) } };
        Object result[][] = { { new Double(1), new Double(0), new Double(0) }, { new Double(0), new Double(1), new Double(0) }, { new Double(0), new Double(0), new Double(1) } };
        Object args[] = new Object[2];
        args[0] = faciendMatrix33;
        args[1] = multiplierMatrix33;
        Object productM = MathFns.mathMmult(args, null);
        assertTrue(compareMatrix((Object[][]) productM, (Object[][]) result, 0d));
        Object faciendMatrix33withDecimal[][] = { { new Double(1), new Double(3), new Double(7) }, { new Double(9), new Double(1), new Double(0) }, { new Double(0.3), new Double(5), new Double(1) } };
        Object multiplierMatrix33withDecimal[][] = { { new Double(1), new Double(4), new Double(8) }, { new Double(0.1), new Double(1), new Double(0.2) }, { new Double(0.4), new Double(6), new Double(1) } };
        Object resultWithDecimal[][] = { { new Double(4.1), new Double(49), new Double(15.6) }, { new Double(9.1), new Double(37), new Double(72.2) }, { new Double(1.2), new Double(12.2), new Double(4.4) } };
        args[0] = faciendMatrix33withDecimal;
        args[1] = multiplierMatrix33withDecimal;
        productM = MathFns.mathMmult(args, null);
        assertTrue(compareMatrix((Object[][]) productM, (Object[][]) resultWithDecimal, 0d));
    }

    public void testMod() {
        Double[] d = { new Double(3), new Double(2) };
        double e = 1;
        assertEquals(e, ((Number) MathFns.mathMod(d, null)).doubleValue(), 0d);
    }

    public void testMround() {
        Double[] d = { new Double(10), new Double(3) };
        double e = 9;
        assertEquals(e, ((Number) MathFns.mathMround(d, null)).doubleValue(), 0d);
        Double[] d1 = { new Double(-10), new Double(-3) };
        double e1 = -9;
        assertEquals(e1, ((Number) MathFns.mathMround(d1, null)).doubleValue(), 0d);
        Double[] d2 = { new Double(1.3), new Double(0.2) };
        double e2 = 1.4;
        assertEquals(e2, ((Number) MathFns.mathMround(d2, null)).doubleValue(), 0.000000000000001d);
        Double[] d3 = { new Double(0.234), new Double(0.01) };
        double e3 = 0.23;
        assertEquals(e3, ((Number) MathFns.mathMround(d3, null)).doubleValue(), 0d);
        Double[] d4 = { new Double(Math.PI), new Double(0) };
        double e4 = 0;
        assertEquals(e4, ((Number) MathFns.mathMround(d4, null)).doubleValue(), 0d);
        Double[] d5 = { new Double(3.14), new Double(0.33) };
        double e5 = 3.3;
        assertEquals(e5, ((Number) MathFns.mathMround(d5, null)).doubleValue(), 0.000000000001d);
        Double[] d6 = { new Double(Math.PI), new Double((double) 1 / 3) };
        double e6 = 3;
        assertEquals(e6, ((Number) MathFns.mathMround(d6, null)).doubleValue(), 0d);
    }

    public void testMultinomial() {
        Double[] d = { new Double(0), new Double(3.9), new Double(4.9) };
        double e = 35;
        assertEquals(e, ((Number) MathFns.mathMultinomial(d, null)).doubleValue(), 0d);
    }

    public void testOdd() {
        Double[] d = { new Double(2.5) };
        double e = 3;
        assertEquals(e, ((Number) MathFns.mathOdd(d, null)).doubleValue(), 0d);
    }

    public void testPi() {
        Double[] d = {};
        double e = Math.PI;
        assertEquals(e, ((Number) MathFns.mathPi(d, null)).doubleValue(), 0d);
    }

    public void testPower() {
        Double[] d = { new Double(4.0), new Double(2.0) };
        double e = 16;
        assertEquals(e, ((Number) MathFns.mathPower(d, null)).doubleValue(), 0d);
    }

    public void testProduct() {
        Double[] d = { new Double(1.0), new Double(2.0), new Double(3.0), new Double(4.0), new Double(5.0), new Double(6.0), new Double(7.0), new Double(8.0), new Double(9.0) };
        double e = 362880;
        assertEquals(e, ((Number) MathFns.mathProduct(d, null)).doubleValue(), 0d);
    }

    public void testQuotient() {
        Double[] d = { new Double(5), new Double(2) };
        double e = 2;
        assertEquals(e, ((Number) MathFns.mathQuotient(d, null)).doubleValue(), 0d);
        Double[] d1 = { new Double(4.5), new Double(3.1) };
        double e1 = 1;
        assertEquals(e1, ((Number) MathFns.mathQuotient(d1, null)).doubleValue(), 0d);
        Double[] d2 = { new Double(-10), new Double(3) };
        double e2 = -3;
        assertEquals(e2, ((Number) MathFns.mathQuotient(d2, null)).doubleValue(), 0d);
    }

    public void testRadians() {
        Double[] d = { new Double(270.0) };
        double e = 3 * Math.PI / 2;
        assertEquals(e, ((Number) MathFns.mathRadians(d, null)).doubleValue(), 0d);
    }

    public void testRoman() {
        Integer arNum[] = { new Integer(499) };
        String roNum = new String("CDXCIX");
        assertEquals("success!", MathFns.mathRoman(arNum, null), roNum);
        Object arNum0[] = { new Integer(499), new Integer(0) };
        String roNum0 = new String("CDXCIX");
        assertEquals("success!", MathFns.mathRoman(arNum0, null), roNum0);
        Object arNum1[] = { new Integer(499), new Integer(1) };
        String roNum1 = new String("LDVLIV");
        assertEquals("success!", MathFns.mathRoman(arNum1, null), roNum1);
        Object arNum2[] = { new Integer(499), new Integer(2) };
        String roNum2 = new String("XDIX");
        assertEquals("success!", MathFns.mathRoman(arNum2, null), roNum2);
        Object arNum3[] = { new Integer(499), new Integer(3) };
        String roNum3 = new String("VDIV");
        assertEquals("success!", MathFns.mathRoman(arNum3, null), roNum3);
        Object arNum4[] = { new Integer(499), new Integer(4) };
        String roNum4 = new String("ID");
        assertEquals("success!", MathFns.mathRoman(arNum4, null), roNum4);
        Object arNumTrue[] = { new Integer(499), new Boolean(true) };
        String roNumTrue = new String("CDXCIX");
        assertEquals("success!", MathFns.mathRoman(arNumTrue, null), roNumTrue);
        Object arNumFalse[] = { new Integer(499), new Boolean(false) };
        String roNumFalse = new String("ID");
        assertEquals("success!", MathFns.mathRoman(arNumFalse, null), roNumFalse);
    }

    public void testRound() {
        Double[] d = { new Double(3.1415), new Double(2.0) };
        double e = 3.14;
        assertEquals(e, ((Number) MathFns.mathRound(d, null)).doubleValue(), 0d);
    }

    public void testRounddown() {
        Double[] d = { new Double(3.1415), new Double(2.0) };
        double e = 3.14;
        assertEquals(e, ((Number) MathFns.mathRounddown(d, null)).doubleValue(), 0d);
    }

    public void testRoundup() {
        Double[] d = { new Double(3.1415), new Double(2.0) };
        double e = 3.15;
        assertEquals(e, ((Number) MathFns.mathRoundup(d, null)).doubleValue(), 0d);
    }

    public void testSeriessum() {
        Double cof[] = { new Double(1), new Double(2), new Double(3), new Double(4) };
        Object arg[] = { new Double(2), new Double(0), new Double(1), (Object) cof };
        double result = 49;
        assertEquals(result, ((Number) MathFns.mathSerissum(arg, null)).doubleValue(), 0d);
    }

    public void testSign() {
        Double[] d = { new Double(-1000.0) };
        double e = -1;
        assertEquals(e, ((Number) MathFns.mathSign(d, null)).doubleValue(), 0d);
    }

    public void testSin() {
        Double[] d = { new Double(Math.PI / 2) };
        double e = Math.sin(Math.PI / 2);
        assertEquals(e, ((Number) MathFns.mathSin(d, null)).doubleValue(), 0d);
    }

    public void testSinh() {
        Double[] d = { new Double(1.0) };
        double e = Math.sinh(1);
        assertEquals(e, ((Number) MathFns.mathSinh(d, null)).doubleValue(), 0d);
    }

    public void testSqrt() {
        Double[] d = { new Double(16.0) };
        double e = Math.sqrt(16);
        assertEquals(e, ((Number) MathFns.mathSqrt(d, null)).doubleValue(), 0d);
    }

    public void testSqrtpi() {
        Double[] d = { new Double(1) };
        double e = Math.sqrt(Math.PI);
        assertEquals(e, ((Number) MathFns.mathSqrtpi(d, null)).doubleValue(), 0d);
        Double[] d1 = { new Double(2) };
        double e1 = Math.sqrt(Math.PI * 2);
        assertEquals(e1, ((Number) MathFns.mathSqrtpi(d1, null)).doubleValue(), 0d);
    }

    public void testSubtotal() {
    }

    public void testSum() {
        Double[] d = { new Double(1.0), new Double(2.0), new Double(3.0), new Double(4.0), new Double(5.0), new Double(6.0), new Double(7.0), new Double(8.0), new Double(9.0) };
        double e = 45;
        assertEquals(e, ((Number) MathFns.mathSum(d, null)).doubleValue(), 0d);
    }

    public void testSumif() {
    }

    public void testSumproduct() {
    }

    public void testSimsq() {
        Double[] d = { new Double(1.0), new Double(2.0), new Double(3.0), new Double(4.0), new Double(5.0), new Double(6.0), new Double(7.0), new Double(8.0), new Double(9.0) };
        double e = 285;
        assertEquals(e, ((Number) MathFns.mathSumsq(d, null)).doubleValue(), 0d);
    }

    public void testSumx2my2() {
        Double cof1[] = { new Double(2), new Double(3), new Double(9), new Double(1), new Double(8), new Double(7), new Double(5) };
        Double cof2[] = { new Double(6), new Double(5), new Double(11), new Double(7), new Double(5), new Double(4), new Double(4) };
        Object arg[] = { (Object) cof1, (Object) cof2 };
        double result = -55;
        assertEquals(result, ((Number) MathFns.mathSumx2my2(arg, null)).doubleValue(), 0d);
    }

    public void testSumx2py2() {
        Double cof1[] = { new Double(2), new Double(3), new Double(9), new Double(1), new Double(8), new Double(7), new Double(5) };
        Double cof2[] = { new Double(6), new Double(5), new Double(11), new Double(7), new Double(5), new Double(4), new Double(4) };
        Object arg[] = { (Object) cof1, (Object) cof2 };
        double result = 521;
        assertEquals(result, ((Number) MathFns.mathSumx2py2(arg, null)).doubleValue(), 0d);
    }

    public void testSumxmy2() {
        Double cof1[] = { new Double(2), new Double(3), new Double(9), new Double(1), new Double(8), new Double(7), new Double(5) };
        Double cof2[] = { new Double(6), new Double(5), new Double(11), new Double(7), new Double(5), new Double(4), new Double(4) };
        Object arg[] = { (Object) cof1, (Object) cof2 };
        double result = 79;
        assertEquals(result, ((Number) MathFns.mathSumxmy2(arg, null)).doubleValue(), 0d);
    }

    public void tesTan() {
        Double[] d = { new Double(0.785) };
        double e = Math.tan(0.785);
        assertEquals(e, ((Number) MathFns.mathTan(d, null)).doubleValue(), 0d);
    }

    public void tesTanh() {
        Double[] d = { new Double(0.5) };
        double e = Math.tanh(0.5);
        assertEquals(e, ((Number) MathFns.mathTanh(d, null)).doubleValue(), 0d);
    }

    public void testTrunc() {
        Double[] d = { new Double(8.9) };
        double e = 8.0;
        assertEquals(e, ((Number) MathFns.mathTrunc(d, null)).doubleValue(), 0d);
    }
}
