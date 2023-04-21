package org.evertree.themolition.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class MathTest {

    @Test
    public void angleZero() {
        double degree = 0;
        assertEquals(0, Math.toRadians(degree));
    }

    @Test
    public void angleNorth() {
        double degree = 90;
        assertEquals(Math.PI / 2, Math.toRadians(degree));
    }

    @Test
    public void angleWest() {
        double degree = 180;
        assertEquals(Math.PI, Math.toRadians(degree));
    }

    @Test
    public void angleSouth() {
        double degree = 270;
        assertEquals(3 / 2.0 * Math.PI, Math.toRadians(degree));
    }

    @Test
    public void angleEast() {
        double degree = 360;
        assertEquals(2 * Math.PI, Math.toRadians(degree));
    }

    @Test
    public void roundUp() {
        assertEquals(1, 9 / 5);
        assertEquals(1.8, (double) 9 / 5);
        assertEquals(2.0, Math.ceil((double) 9 / 5));
        assertEquals(1, 6 / 5);
        assertEquals(1.2, (double) 6 / 5);
        assertEquals(2.0, Math.ceil((double) 6 / 5));
    }
}
