package dxc.junit.opcodes.fmul;

import dxc.junit.DxTestCase;
import dxc.junit.DxUtil;
import dxc.junit.opcodes.fmul.jm.T_fmul_1;

public class Test_fmul extends DxTestCase {

    /**
     * @title  Arguments = 2.7f, 3.14f
     */
    public void testN1() {
        T_fmul_1 t = new T_fmul_1();
        assertEquals(8.478001f, t.run(2.7f, 3.14f));
    }

    /**
     * @title  Arguments = 0, -3.14f
     */
    public void testN2() {
        T_fmul_1 t = new T_fmul_1();
        assertEquals(-0f, t.run(0, -3.14f));
    }

    /**
     * @title  Arguments = -2.7f, -3.14f
     */
    public void testN3() {
        T_fmul_1 t = new T_fmul_1();
        assertEquals(8.478001f, t.run(-3.14f, -2.7f));
    }

    /**
     * @title  Arguments = Float.MAX_VALUE, Float.NaN
     */
    public void testB1() {
        T_fmul_1 t = new T_fmul_1();
        assertEquals(Float.NaN, t.run(Float.MAX_VALUE, Float.NaN));
    }

    /**
     * @title  Arguments = Float.POSITIVE_INFINITY, 0
     */
    public void testB2() {
        T_fmul_1 t = new T_fmul_1();
        assertEquals(Float.NaN, t.run(Float.POSITIVE_INFINITY, 0));
    }

    /**
     * @title  Arguments = Float.POSITIVE_INFINITY, -2.7f
     */
    public void testB3() {
        T_fmul_1 t = new T_fmul_1();
        assertEquals(Float.NEGATIVE_INFINITY, t.run(Float.POSITIVE_INFINITY, -2.7f));
    }

    /**
     * @title  Arguments = Float.POSITIVE_INFINITY,
     * Float.NEGATIVE_INFINITY
     */
    public void testB4() {
        T_fmul_1 t = new T_fmul_1();
        assertEquals(Float.NEGATIVE_INFINITY, t.run(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY));
    }

    /**
     * @title  Arguments = +0, -0f
     */
    public void testB5() {
        T_fmul_1 t = new T_fmul_1();
        assertEquals(-0f, t.run(+0f, -0f));
    }

    /**
     * @title  Arguments = -0f, -0f
     */
    public void testB6() {
        T_fmul_1 t = new T_fmul_1();
        assertEquals(+0f, t.run(-0f, -0f));
    }

    /**
     * @title  Arguments = Float.MAX_VALUE, Float.MAX_VALUE
     */
    public void testB7() {
        T_fmul_1 t = new T_fmul_1();
        assertEquals(Float.POSITIVE_INFINITY, t.run(Float.MAX_VALUE, Float.MAX_VALUE));
    }

    /**
     * @title  Arguments = Float.MIN_VALUE, -1.4E-45f
     */
    public void testB8() {
        T_fmul_1 t = new T_fmul_1();
        assertEquals(-0f, t.run(Float.MIN_VALUE, -1.4E-45f));
    }

    /**
     * @constraint 4.8.2.1
     * @title number of arguments
     */
    public void testVFE1() {
        try {
            Class.forName("dxc.junit.opcodes.fmul.jm.T_fmul_2");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.1
     * @title types of arguments - float, double
     */
    public void testVFE2() {
        try {
            Class.forName("dxc.junit.opcodes.fmul.jm.T_fmul_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.1
     * @title types of arguments - long, float
     */
    public void testVFE3() {
        try {
            Class.forName("dxc.junit.opcodes.fmul.jm.T_fmul_4");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.1
     * @title types of arguments - reference, float
     */
    public void testVFE4() {
        try {
            Class.forName("dxc.junit.opcodes.fmul.jm.T_fmul_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}
