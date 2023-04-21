package edu.uchicago.agentcell.junit;

import edu.uchicago.agentcell.math.Calculus;
import edu.uchicago.agentcell.math.RotationMatrix;
import edu.uchicago.agentcell.math.RotationMatrix3x3;
import edu.uchicago.agentcell.math.Vect;
import edu.uchicago.agentcell.math.Vect3;
import junit.framework.TestCase;

/**
 * @author emonet
 *
 * 
 * 
 */
public class RotationMatrix3x3Test extends TestCase {

    /**
     * Constructor for RotationMatrix3x3Test.
     * @param arg0
     */
    public RotationMatrix3x3Test(String arg0) {
        super(arg0);
    }

    public static void main(String args[]) {
        junit.swingui.TestRunner.run(RotationMatrix3x3Test.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public final void testRotationMatrix3x3() {
        RotationMatrix r = new RotationMatrix3x3();
        TestCase.assertEquals(true, r.isIdentity());
    }

    public final void testRotationMatrix3x3doubledoubledoubledoubledoubledoubledoubledoubledouble() {
        RotationMatrix r = new RotationMatrix3x3(1, 0, 0, 0, 1, 0, 0, 0, 1);
        TestCase.assertEquals(true, r.isIdentity());
    }

    public final void testRotationMatrix3x3doubleArrayArray() {
        double rr[][] = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
        RotationMatrix r = new RotationMatrix3x3(rr);
        TestCase.assertEquals(true, r.isIdentity());
    }

    public final void testSetAxisAngleintdouble() {
        RotationMatrix r = new RotationMatrix3x3();
        double angle = Math.PI / 4;
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        r.setAxisAngle(0, angle);
        TestCase.assertEquals(1, r.getElement(0, 0), Calculus.TOLERANCE);
        TestCase.assertEquals(0, r.getElement(0, 1), Calculus.TOLERANCE);
        TestCase.assertEquals(0, r.getElement(0, 2), Calculus.TOLERANCE);
        TestCase.assertEquals(0, r.getElement(1, 0), Calculus.TOLERANCE);
        TestCase.assertEquals(c, r.getElement(1, 1), Calculus.TOLERANCE);
        TestCase.assertEquals(-s, r.getElement(1, 2), Calculus.TOLERANCE);
        TestCase.assertEquals(0, r.getElement(2, 0), Calculus.TOLERANCE);
        TestCase.assertEquals(s, r.getElement(2, 1), Calculus.TOLERANCE);
        TestCase.assertEquals(c, r.getElement(2, 2), Calculus.TOLERANCE);
        r.setAxisAngle(1, angle);
        TestCase.assertEquals(c, r.getElement(0, 0), Calculus.TOLERANCE);
        TestCase.assertEquals(0, r.getElement(0, 1), Calculus.TOLERANCE);
        TestCase.assertEquals(s, r.getElement(0, 2), Calculus.TOLERANCE);
        TestCase.assertEquals(0, r.getElement(1, 0), Calculus.TOLERANCE);
        TestCase.assertEquals(1, r.getElement(1, 1), Calculus.TOLERANCE);
        TestCase.assertEquals(0, r.getElement(1, 2), Calculus.TOLERANCE);
        TestCase.assertEquals(-s, r.getElement(2, 0), Calculus.TOLERANCE);
        TestCase.assertEquals(0, r.getElement(2, 1), Calculus.TOLERANCE);
        TestCase.assertEquals(c, r.getElement(2, 2), Calculus.TOLERANCE);
        r.setAxisAngle(2, angle);
        TestCase.assertEquals(c, r.getElement(0, 0), Calculus.TOLERANCE);
        TestCase.assertEquals(-s, r.getElement(0, 1), Calculus.TOLERANCE);
        TestCase.assertEquals(0, r.getElement(0, 2), Calculus.TOLERANCE);
        TestCase.assertEquals(s, r.getElement(1, 0), Calculus.TOLERANCE);
        TestCase.assertEquals(c, r.getElement(1, 1), Calculus.TOLERANCE);
        TestCase.assertEquals(0, r.getElement(1, 2), Calculus.TOLERANCE);
        TestCase.assertEquals(0, r.getElement(2, 0), Calculus.TOLERANCE);
        TestCase.assertEquals(0, r.getElement(2, 1), Calculus.TOLERANCE);
        TestCase.assertEquals(1, r.getElement(2, 2), Calculus.TOLERANCE);
    }

    public final void testSetVectdouble() {
        RotationMatrix r = new RotationMatrix3x3(new Vect3(1, 1, 1), (2 * Math.PI) / 3);
        Vect v0 = new Vect3(1, 0, 0);
        Vect v1 = new Vect3(0, 1, 0);
        TestCase.assertEquals(true, r.mult(v0).equals(v1));
    }

    public final void testSetAxesAnglesintdouledoubledoubledoubledoubledouble() {
        double a0 = Math.PI / 4;
        double a1 = 0.2;
        double a2 = 1.2;
        RotationMatrix s012 = new RotationMatrix3x3().setAxesAngles(0, a0, a1, a2);
        RotationMatrix s021 = new RotationMatrix3x3().setAxesAngles(1, a0, a1, a2);
        RotationMatrix s102 = new RotationMatrix3x3().setAxesAngles(2, a0, a1, a2);
        RotationMatrix s120 = new RotationMatrix3x3().setAxesAngles(3, a0, a1, a2);
        RotationMatrix s201 = new RotationMatrix3x3().setAxesAngles(4, a0, a1, a2);
        RotationMatrix s210 = new RotationMatrix3x3().setAxesAngles(5, a0, a1, a2);
        RotationMatrix r0 = new RotationMatrix3x3().setAxisAngle(0, a0);
        RotationMatrix r1 = new RotationMatrix3x3().setAxisAngle(1, a1);
        RotationMatrix r2 = new RotationMatrix3x3().setAxisAngle(2, a2);
        RotationMatrix r = new RotationMatrix3x3();
        TestCase.assertEquals(true, (r.mult(r0).mult(r1).mult(r2)).equals(s012));
        r.setToIdentity();
        TestCase.assertEquals(true, (r.mult(r0).mult(r2).mult(r1)).equals(s021));
        r.setToIdentity();
        TestCase.assertEquals(true, (r.mult(r1).mult(r0).mult(r2)).equals(s102));
        r.setToIdentity();
        TestCase.assertEquals(true, (r.mult(r1).mult(r2).mult(r0)).equals(s120));
        r.setToIdentity();
        TestCase.assertEquals(true, (r.mult(r2).mult(r0).mult(r1)).equals(s201));
        r.setToIdentity();
        TestCase.assertEquals(true, (r.mult(r2).mult(r1).mult(r0)).equals(s210));
    }

    public final void testSetAxesAnglesintdouledoubledouble() {
        double a0 = Math.PI / 4;
        double a1 = 0.2;
        double a2 = 1.2;
        double c0 = Math.cos(a0);
        double c1 = Math.cos(a1);
        double c2 = Math.cos(a2);
        double s0 = Math.sin(a0);
        double s1 = Math.sin(a1);
        double s2 = Math.sin(a2);
        RotationMatrix s012 = new RotationMatrix3x3().setAxesAngles(0, a0, a1, a2);
        RotationMatrix s021 = new RotationMatrix3x3().setAxesAngles(1, a0, a1, a2);
        RotationMatrix s102 = new RotationMatrix3x3().setAxesAngles(2, a0, a1, a2);
        RotationMatrix s120 = new RotationMatrix3x3().setAxesAngles(3, a0, a1, a2);
        RotationMatrix s201 = new RotationMatrix3x3().setAxesAngles(4, a0, a1, a2);
        RotationMatrix s210 = new RotationMatrix3x3().setAxesAngles(5, a0, a1, a2);
        RotationMatrix r012 = new RotationMatrix3x3().setAxesAngles(0, c0, c1, c2, s0, s1, s2);
        RotationMatrix r021 = new RotationMatrix3x3().setAxesAngles(1, c0, c1, c2, s0, s1, s2);
        RotationMatrix r102 = new RotationMatrix3x3().setAxesAngles(2, c0, c1, c2, s0, s1, s2);
        RotationMatrix r120 = new RotationMatrix3x3().setAxesAngles(3, c0, c1, c2, s0, s1, s2);
        RotationMatrix r201 = new RotationMatrix3x3().setAxesAngles(4, c0, c1, c2, s0, s1, s2);
        RotationMatrix r210 = new RotationMatrix3x3().setAxesAngles(5, c0, c1, c2, s0, s1, s2);
        TestCase.assertEquals(true, (r012.equals(s012)));
        TestCase.assertEquals(true, (r021.equals(s021)));
        TestCase.assertEquals(true, (r102.equals(s102)));
        TestCase.assertEquals(true, (r120.equals(s120)));
        TestCase.assertEquals(true, (r201.equals(s201)));
        TestCase.assertEquals(true, (r210.equals(s210)));
    }
}
