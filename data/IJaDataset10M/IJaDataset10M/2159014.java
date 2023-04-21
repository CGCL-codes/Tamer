package org.alcibiade.sculpt.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class VectorTest {

    @Test
    public void testVectorVector() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        Vector v2 = new Vector(v1);
        assertTrue(v2.x == 1.1);
        assertTrue(v2.y == 2.2);
        assertTrue(v2.z == 3.3);
    }

    @Test
    public void testVectorVectorVector() {
        Vector v1 = new Vector(new Vector(1.1, 2.2, 3.3), new Vector(2.2, 4.4, 6.6));
        assertTrue(v1.x == 1.1);
        assertTrue(v1.y == 2.2);
        assertTrue(v1.z == 3.3);
    }

    @Test
    public void testVectorDoubleDoubleDouble() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        assertTrue(v1.x == 1.1);
        assertTrue(v1.y == 2.2);
        assertTrue(v1.z == 3.3);
    }

    @Test
    public void testVector() {
        Vector v1 = new Vector();
        assertTrue(v1.x == 0);
        assertTrue(v1.y == 0);
        assertTrue(v1.z == 0);
    }

    @Test
    public void testVectorDouble() {
        Vector v1 = new Vector(1.1);
        assertTrue(v1.x == 1.1);
        assertTrue(v1.y == 1.1);
        assertTrue(v1.z == 1.1);
    }

    @Test
    public void testGetX() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        assertTrue(v1.getX() == 1.1);
    }

    @Test
    public void testSetX() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        v1.setX(4.4);
        assertTrue(v1.getX() == 4.4);
    }

    @Test
    public void testGetY() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        assertTrue(v1.getY() == 2.2);
    }

    @Test
    public void testSetY() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        v1.setY(4.4);
        assertTrue(v1.getY() == 4.4);
    }

    @Test
    public void testGetZ() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        assertTrue(v1.getZ() == 3.3);
    }

    @Test
    public void testSetZ() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        v1.setZ(4.4);
        assertTrue(v1.getZ() == 4.4);
    }

    @Test
    public void testAdd() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        v1.add(new Vector(1, 2, 3));
        assertEquals(new Vector(2.1, 4.2, 6.3), v1);
    }

    @Test
    public void testSubtract() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        v1.subtract(new Vector(1, 2, 3));
        assertEquals(new Vector(0.1, 0.2, 0.3), v1);
    }

    @Test
    public void testMultiplyVector() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        v1.multiply(new Vector(1, 2, 3));
        assertEquals(new Vector(1.1, 4.4, 9.9), v1);
    }

    @Test
    public void testDivide() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        v1.divide(new Vector(1, 2, 3));
        assertEquals(new Vector(1.1, 1.1, 1.1), v1);
    }

    @Test
    public void testMultiplyDouble() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        v1.multiply(2);
        assertEquals(new Vector(2.2, 4.4, 6.6), v1);
    }

    @Test
    public void testRotate() {
        double ax = 10;
        double ay = 20;
        double az = 30;
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        Vector v2 = new Vector(v1);
        v2.rotate(new Vector(ax, ay, az));
        assertTrue(!v1.equals(v2));
        v2.rotateInvert(new Vector(ax, ay, az));
        assertTrue(v1.equals(v2));
    }

    @Test
    public void testRotateInvert() {
        double ax = 10;
        double ay = 20;
        double az = 30;
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        Vector v2 = new Vector(v1);
        v2.rotate(new Vector(ax, ay, az));
        assertTrue(!v1.equals(v2));
        v2.rotateInvert(new Vector(ax, ay, az));
        assertTrue(v1.equals(v2));
    }

    @Test
    public void testRotateX() {
        double a = 10;
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        Vector v2 = new Vector(v1);
        v2.rotateX(a);
        assertTrue(!v1.equals(v2));
        v2.rotateX(-a);
        assertTrue(v1.equals(v2));
    }

    @Test
    public void testRotateY() {
        double a = 10;
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        Vector v2 = new Vector(v1);
        v2.rotateY(a);
        assertTrue(!v1.equals(v2));
        v2.rotateY(-a);
        assertTrue(v1.equals(v2));
    }

    @Test
    public void testRotateZ() {
        double a = 10;
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        Vector v2 = new Vector(v1);
        v2.rotateZ(a);
        assertTrue(!v1.equals(v2));
        v2.rotateZ(-a);
        assertTrue(v1.equals(v2));
    }

    @Test
    public void testLength() {
        Vector v1 = new Vector(0, 5, 0);
        assertTrue(v1.length() == 5);
    }

    @Test
    public void testDistance() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        Vector v2 = new Vector(-1.2, 4.3, 2.6);
        Vector v12 = new Vector(v1, v2);
        assertEquals(v12.length(), v1.distance(v2));
    }

    @Test
    public void testNormalize() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        v1.normalize();
        assertTrue(Math.abs(v1.length() - 1) < Vector.EPSILON);
    }

    @Test
    public void testIsColinear() {
        Vector v1 = new Vector(1.1, 2.2, 3.3);
        Vector v2 = new Vector(v1);
        v2.multiply(1.4);
        assertTrue(v1.isColinear(v2));
        Vector v3 = new Vector(v2);
        v3.add(new Vector(0, 0.3, 0));
        assertTrue(!v1.isColinear(v3));
        Vector v4 = new Vector(v2);
        v4.multiply(-2.1);
        assertTrue(!v1.isColinear(v4));
        assertTrue(!Vector.Y.isColinear(Vector.X));
        Vector r1 = new Vector(0, -0.349, 0.595);
        Vector r2 = new Vector(r1);
        r2.multiply(2.12);
        assertTrue(r1.isColinear(r2));
    }

    @Test
    public void testCrossProduct() {
    }

    @Test
    public void testDotProduct() {
    }

    @Test
    public void testCompareTo() {
    }

    @Test
    public void testEqualsObject() {
    }
}
