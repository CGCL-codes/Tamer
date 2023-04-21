package org.jheuristics.util;

import junit.framework.TestCase;

public class MathUtilsTest extends TestCase {

    public final void testConstructor() {
        new MathUtils();
    }

    public void testSum() {
        try {
            MathUtils.sum(null);
            fail();
        } catch (NullPointerException e) {
        }
        double v0 = 0;
        double v01 = 0.1;
        double v02 = 0.2;
        double v_03 = -0.3;
        double v04 = 0.4;
        assertEquals(v01 + v02 + v04 + v_03, MathUtils.sum(new double[] { v0, v01, v02, v_03, v04 }), 0);
    }

    public void testMax() {
        try {
            MathUtils.max(null);
            fail();
        } catch (NullPointerException e) {
        }
        double v0 = 0;
        double v01 = 0.1;
        double v02 = 0.2;
        double v_03 = -0.3;
        double v04 = 0.4;
        assertEquals(v04, MathUtils.max(new double[] { v0, v01, v02, v_03, v04 }), 0);
    }

    public void testMin() {
        try {
            MathUtils.min(null);
            fail();
        } catch (NullPointerException e) {
        }
        double v0 = 0;
        double v01 = 0.1;
        double v02 = 0.2;
        double v_03 = -0.3;
        double v04 = 0.4;
        assertEquals(v_03, MathUtils.min(new double[] { v0, v01, v02, v_03, v04 }), 0);
    }
}
