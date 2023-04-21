package dot.junit.opcodes.cmpl_float;

import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.cmpl_float.d.T_cmpl_float_6;
import dot.junit.opcodes.cmpl_float.d.T_cmpl_float_1;

public class Test_cmpl_float extends DxTestCase {

    /**
     * @title Arguments = 3.14f, 2.7f
     */
    public void testN1() {
        T_cmpl_float_1 t = new T_cmpl_float_1();
        assertEquals(1, t.run(3.14f, 2.7f));
    }

    /**
     * @title Arguments = -3.14f, 2.7f
     */
    public void testN2() {
        T_cmpl_float_1 t = new T_cmpl_float_1();
        assertEquals(-1, t.run(-3.14f, 2.7f));
    }

    /**
     * @title Arguments = 3.14, 3.14
     */
    public void testN3() {
        T_cmpl_float_1 t = new T_cmpl_float_1();
        assertEquals(0, t.run(3.14f, 3.14f));
    }

    /**
     * @title Types of arguments - int, float. Dalvik doens't distinguish 32-bits types internally,
     * so this comparison of int and float makes no sense but shall not crash the VM.  
     */
    public void testN4() {
        T_cmpl_float_6 t = new T_cmpl_float_6();
        try {
            t.run(123, 3.145f);
        } catch (Throwable e) {
        }
    }

    /**
     * @title Arguments = Float.NaN, Float.MAX_VALUE
     */
    public void testB1() {
        T_cmpl_float_1 t = new T_cmpl_float_1();
        assertEquals(-1, t.run(Float.NaN, Float.MAX_VALUE));
    }

    /**
     * @title Arguments = +0, -0
     */
    public void testB2() {
        T_cmpl_float_1 t = new T_cmpl_float_1();
        assertEquals(0, t.run(+0f, -0f));
    }

    /**
     * @title Arguments = Float.NEGATIVE_INFINITY, Float.MIN_VALUE
     */
    public void testB3() {
        T_cmpl_float_1 t = new T_cmpl_float_1();
        assertEquals(-1, t.run(Float.NEGATIVE_INFINITY, Float.MIN_VALUE));
    }

    /**
     * @title Arguments = Float.POSITIVE_INFINITY, Float.MAX_VALUE
     */
    public void testB4() {
        T_cmpl_float_1 t = new T_cmpl_float_1();
        assertEquals(1, t.run(Float.POSITIVE_INFINITY, Float.MAX_VALUE));
    }

    /**
     * @title Arguments = Float.POSITIVE_INFINITY,
     * Float.NEGATIVE_INFINITY
     */
    public void testB5() {
        T_cmpl_float_1 t = new T_cmpl_float_1();
        assertEquals(1, t.run(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY));
    }

    /**
     * @constraint B1 
     * @title  types of arguments - float, double
     */
    public void testVFE1() {
        try {
            Class.forName("dot.junit.opcodes.cmpl_float.d.T_cmpl_float_2");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint B1
     * @title types of arguments - long, float
     */
    public void testVFE2() {
        try {
            Class.forName("dot.junit.opcodes.cmpl_float.d.T_cmpl_float_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint B1 
     * @title  types of arguments - reference, float
     */
    public void testVFE3() {
        try {
            Class.forName("dot.junit.opcodes.cmpl_float.d.T_cmpl_float_4");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint A23 
     * @title number of registers
     */
    public void testVFE4() {
        try {
            Class.forName("dot.junit.opcodes.cmpl_float.d.T_cmpl_float_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}
