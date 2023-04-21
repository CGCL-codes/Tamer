package edu.uchicago.agentcell.junit;

import edu.uchicago.agentcell.math.Calculus;
import edu.uchicago.agentcell.math.Matrix;
import edu.uchicago.agentcell.math.Matrix3x3;
import edu.uchicago.agentcell.math.Vect;
import edu.uchicago.agentcell.math.Vect3;
import junit.framework.TestCase;

/**
 * @author emonet
 *
 * 
 * 
 */
public class Vect3Test extends TestCase {

    /**
     * Constructor for Vect3Test.
     * @param arg0
     */
    public Vect3Test(String arg0) {
        super(arg0);
    }

    public static void main(String args[]) {
        junit.swingui.TestRunner.run(Vect3Test.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testCross() {
        Vect a = new Vect3(1.0, 2.0, -3.0);
        Vect b = new Vect3(2, 1, 1);
        Vect solution = a.cross(b);
        Vect expected = new Vect3(5, -7, -3);
        TestCase.assertEquals(true, solution.equals(expected));
    }

    public final void testCrossOtherThis() {
        Vect a = new Vect3(1.0, 2.0, -3.0);
        Vect b = new Vect3(2, 1, 1);
        Vect ab = b.crossOtherThis(a);
        Vect expected = new Vect3(5, -7, -3);
        TestCase.assertEquals(true, ab.equals(expected));
    }

    public final void testDiv() {
        Vect a = new Vect3(1, 3, -23.2);
        double s = -2;
        Vect sol = new Vect3(-0.5, -1.5, 11.6);
        TestCase.assertEquals(true, sol.equals(a.div(s)));
    }

    public final void testLength() {
        Vect v = new Vect3(1.0, 2.0, -3.0);
        TestCase.assertEquals(Math.sqrt(14.0), v.length(), 0.0);
    }

    public final void testMinus() {
        Vect a = new Vect3(1, 3, -23.2);
        Vect b = new Vect3(2, -2, 2);
        Vect sol = new Vect3(-1, 5, -25.2);
        TestCase.assertEquals(true, sol.equals(a.minus(b)));
    }

    public final void testMinusMult() {
        Vect a = new Vect3(1, 3, -23.2);
        Vect b = new Vect3(2, -2, 2);
        double s = 2;
        Vect sol = new Vect3(-3, 7, -27.2);
        TestCase.assertEquals(true, sol.equals(a.minusMult(s, b)));
    }

    public final void testMultdouble() {
        Vect a = new Vect3(1, 3, -23.2);
        double s = -2;
        Vect sol = new Vect3(-2, -6, 46.4);
        TestCase.assertEquals(true, sol.equals(a.mult(s)));
    }

    public final void testMultVect() {
        Vect a = new Vect3(1, 3, -23.2);
        Vect b = new Vect3(2, -2, 2);
        double sol = 2 - 6 - 46.4 + (Calculus.TOLERANCE * 0.9999999);
        TestCase.assertEquals(sol, a.mult(b), Calculus.TOLERANCE);
    }

    public final void testNormalize() {
        Vect a = new Vect3(1, 2, -3);
        double aNorm = Math.sqrt(14.0);
        Vect b = new Vect3(1, 2, -3);
        Vect c = new Vect3(1, 2, -3);
        TestCase.assertEquals(true, (a.normalize()).equals(b.div(aNorm)));
        TestCase.assertEquals(aNorm, (a.normalize()).mult(c), Calculus.TOLERANCE);
    }

    public final void testPlus() {
        Vect a = new Vect3(1, 3, -23.2);
        Vect b = new Vect3(2, -2, 2);
        Vect sol = new Vect3(3, 1, -21.2);
        TestCase.assertEquals(true, sol.equals(a.plus(b)));
    }

    public final void testPlusMult() {
        Vect a = new Vect3(1, 3, -23.2);
        Vect b = new Vect3(2, -2, 2);
        double s = 2;
        Vect sol = new Vect3(5, -1, -19.2);
        TestCase.assertEquals(true, sol.equals(a.plusMult(s, b)));
    }

    public final void testReverse() {
        Vect a = new Vect3(1, 3, -23.2);
        Vect sol = new Vect3(-1, -3, 23.2);
        TestCase.assertEquals(true, sol.equals(a.reverse()));
    }

    public final void testCopy() {
        Vect a = new Vect3(1, 2, 3);
        Vect b = a.copy();
        TestCase.assertEquals(false, a == b);
        TestCase.assertEquals(true, a.equals(b));
    }

    public final void testCopyVect() {
        Vect a = new Vect3(1, 2, 3);
        Vect b = new Vect3(2, 3, 3);
        a.copy(b);
        TestCase.assertEquals(false, a == b);
        TestCase.assertEquals(true, a.equals(b));
    }

    public final void testGetElement() {
        Vect a = new Vect3(1, 2, 3);
        TestCase.assertEquals(1, a.getElement(0), 0);
        TestCase.assertEquals(2, a.getElement(1), 0);
        TestCase.assertEquals(3, a.getElement(2), 0);
    }

    public final void testGetSize() {
        Vect a = new Vect3(1, 2, 3);
        TestCase.assertEquals(3, a.getSize(), 0);
    }

    public final void testSetElement() {
        Vect a = new Vect3();
        a.setElement(0, 1);
        a.setElement(1, 2);
        a.setElement(2, 3);
        TestCase.assertEquals(1, a.getElement(0), 0);
        TestCase.assertEquals(2, a.getElement(1), 0);
        TestCase.assertEquals(3, a.getElement(2), 0);
    }

    public final void testMultMatrix() {
        Matrix m = new Matrix3x3(1, 2, 3, 1, 2, 0, 1, 1, -1);
        Vect a = new Vect3(1, 2, 3);
        Vect sol = new Vect3(6, 9, 0);
        TestCase.assertEquals(true, sol.equals(a.mult(m)));
    }

    public final void testHasUnitLength() {
        double len = Math.sqrt(14.0);
        Vect a = new Vect3(1 / len, 2 / len, (-3 / len) + Calculus.TOLERANCE);
        TestCase.assertEquals(true, a.hasUnitLength());
    }

    public final void testIsZero() {
        Vect a = new Vect3(Calculus.TOLERANCE, 0, Calculus.TOLERANCE);
        TestCase.assertEquals(true, a.isZero());
    }

    public final void testEquals() {
        Vect a = new Vect3(1, 2, 3);
        Vect b = new Vect3(1.0, 2, 3);
        TestCase.assertEquals(true, a.equals(b));
    }
}
