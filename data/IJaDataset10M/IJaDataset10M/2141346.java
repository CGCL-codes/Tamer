package net.java.dev.joode;

import net.java.dev.joode.util.Matrix3;
import net.java.dev.joode.util.Matrix;
import org.openmali.vecmath2.Matrix3f;
import junit.framework.TestCase;

/**
 */
public class MassTest extends TestCase {

    /**
     * tests the intertia and inv inertia are calculated according to
     * I = R*I*R'
     * I_inv = (inverse(I))
     */
    public void testRotate() {
        Matrix3 rotation = Matrix3.pool.aquire();
        Matrix3 tmp1 = new Matrix3();
        Matrix3 I = new Matrix3();
        Matrix3 invI = new Matrix3();
        Matrix3f inv = new Matrix3f();
        for (int i = 0; i < 100; i++) {
            Matrix3.setRandomRotationMatrix(rotation);
            Mass m = Mass.createCapsule((float) Math.random() + 1, 1, (float) Math.random() + 1, (float) Math.random() + 1);
            m.getMomentOfInertia().mulTranspose(rotation, tmp1);
            rotation.mul(tmp1, I);
            inv.setRow(0, I.m[0], I.m[1], I.m[2]);
            inv.setRow(1, I.m[4], I.m[5], I.m[6]);
            inv.setRow(2, I.m[8], I.m[9], I.m[10]);
            inv.invert();
            invI.set(0, 0, inv.m00());
            invI.set(0, 1, inv.m01());
            invI.set(0, 2, inv.m02());
            invI.set(1, 0, inv.m10());
            invI.set(1, 1, inv.m11());
            invI.set(1, 2, inv.m12());
            invI.set(2, 0, inv.m20());
            invI.set(2, 1, inv.m21());
            invI.set(2, 2, inv.m22());
            Mass mr = m.rotate(rotation, Mass.pool.aquire());
            System.out.println("mr.momentOfInertia = " + mr.getMomentOfInertia());
            System.out.println("I = " + I);
            System.out.println("mr.invI = " + mr.invI);
            System.out.println("invI = " + invI);
            assertTrue(Matrix.epsilonEquals(mr.getMomentOfInertia(), I, 0.0001f));
            assertTrue(Matrix.epsilonEquals(mr.invI, invI, .01f));
            Mass.pool.release(mr);
        }
        Matrix3.pool.release(rotation);
    }
}
