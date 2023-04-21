package com.dukesoftware.utils.math;

import org.junit.Assert;
import org.junit.Test;

public class MathUtilTest {

    @Test
    public void testPower2() throws Exception {
        Assert.assertEquals(2, MathUtils.power2(4));
    }

    @Test
    public void testDistanceOnSphere() {
        System.out.println(MathUtils.distance1(1, 0, 0, Math.PI, Math.PI / 2));
        System.out.println(MathUtils.distance2(1, Math.PI / 3, Math.PI / 4, Math.PI / 2, Math.PI / 2));
        System.out.println(MathUtils.q(1, Math.PI / 3, Math.PI / 4, Math.PI / 2, Math.PI / 2));
    }

    @Test
    public void testArea() throws Exception {
        SimplePoint2d[] points = new SimplePoint2d[] { new SimplePoint2d(0.0, 0.0), new SimplePoint2d(1.0, 0.0), new SimplePoint2d(0.0, 1.0) };
        Assert.assertEquals("Area size is wrong", MathUtils.area(points), 0.5, 0d);
    }
}
