package dxc.junit.opcodes.dcmpg;

import dxc.junit.DxTestCase;
import dxc.junit.DxUtil;
import dxc.junit.opcodes.dcmpg.jm.T_dcmpg_1;

public class Test_dcmpg extends DxTestCase {

    /**
     * @title  Arguments = 3.14d, 2.7d
     */
    public void testN1() {
        T_dcmpg_1 t = new T_dcmpg_1();
        assertEquals(1, t.run(3.14d, 2.7d));
    }

    /**
     * @title  Arguments = -3.14d, 2.7d
     */
    public void testN2() {
        T_dcmpg_1 t = new T_dcmpg_1();
        assertEquals(-1, t.run(-3.14d, 2.7d));
    }

    /**
     * @title  Arguments = 3.14, 3.14
     */
    public void testN3() {
        T_dcmpg_1 t = new T_dcmpg_1();
        assertEquals(0, t.run(3.14d, 3.14d));
    }

    /**
     * @title  Arguments = Double.NaN, Double.MAX_VALUE
     */
    public void testB1() {
        T_dcmpg_1 t = new T_dcmpg_1();
        assertEquals(1, t.run(Double.NaN, Double.MAX_VALUE));
    }

    /**
     * @title  Arguments = +0, -0
     */
    public void testB2() {
        T_dcmpg_1 t = new T_dcmpg_1();
        assertEquals(0, t.run(+0f, -0f));
    }

    /**
     * @title  Arguments = Double.NEGATIVE_INFINITY, Double.MIN_VALUE
     */
    public void testB3() {
        T_dcmpg_1 t = new T_dcmpg_1();
        assertEquals(-1, t.run(Double.NEGATIVE_INFINITY, Double.MIN_VALUE));
    }

    /**
     * @title  Arguments = Double.POSITIVE_INFINITY, Double.MAX_VALUE
     */
    public void testB4() {
        T_dcmpg_1 t = new T_dcmpg_1();
        assertEquals(1, t.run(Double.POSITIVE_INFINITY, Double.MAX_VALUE));
    }

    /**
     * @title  Arguments = Double.POSITIVE_INFINITY,
     * Double.NEGATIVE_INFINITY
     */
    public void testB5() {
        T_dcmpg_1 t = new T_dcmpg_1();
        assertEquals(1, t.run(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));
    }

    /**
     * @constraint 4.8.2.1
     * @title number of arguments
     */
    public void testVFE1() {
        try {
            Class.forName("dxc.junit.opcodes.dcmpg.jm.T_dcmpg_2");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.1
     * @title types of arguments - double, float
     */
    public void testVFE2() {
        try {
            Class.forName("dxc.junit.opcodes.dcmpg.jm.T_dcmpg_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.1
     * @title types of arguments - long, double
     */
    public void testVFE3() {
        try {
            Class.forName("dxc.junit.opcodes.dcmpg.jm.T_dcmpg_4");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.1
     * @title types of arguments - double, reference
     */
    public void testVFE4() {
        try {
            Class.forName("dxc.junit.opcodes.dcmpg.jm.T_dcmpg_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}
