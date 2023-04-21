package dot.junit.opcodes.and_int;

import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.and_int.d.T_and_int_1;
import dot.junit.opcodes.and_int.d.T_and_int_5;

public class Test_and_int extends DxTestCase {

    /**
     * @title Arguments = 15, 8
     */
    public void testN1() {
        T_and_int_1 t = new T_and_int_1();
        assertEquals(8, t.run(15, 8));
    }

    /**
     * @title Arguments = 0xfffffff8, 0xfffffff1
     */
    public void testN2() {
        T_and_int_1 t = new T_and_int_1();
        assertEquals(0xfffffff0, t.run(0xfffffff8, 0xfffffff1));
    }

    /**
     * @title Arguments = 0xcafe & -1
     */
    public void testN3() {
        T_and_int_1 t = new T_and_int_1();
        assertEquals(0xcafe, t.run(0xcafe, -1));
    }

    /**
     * @title Types of arguments - int, float. Dalvik doens't distinguish 32-bits types internally,
     * so this (float & int) makes no sense but shall not crash the VM.  
     */
    public void testN4() {
        T_and_int_5 t = new T_and_int_5();
        try {
            t.run(123, 44.44f);
        } catch (Throwable e) {
        }
    }

    /**
     * @title Arguments = 0 & -1
     */
    public void testB1() {
        T_and_int_1 t = new T_and_int_1();
        assertEquals(0, t.run(0, -1));
    }

    /**
     * @title Arguments = Integer.MAX_VALUE & Integer.MIN_VALUE
     */
    public void testB2() {
        T_and_int_1 t = new T_and_int_1();
        assertEquals(0, t.run(Integer.MAX_VALUE, Integer.MIN_VALUE));
    }

    /**
     * @constraint B1 
     * @title types of arguments - double & int
     */
    public void testVFE2() {
        try {
            Class.forName("dot.junit.opcodes.and_int.d.T_and_int_2");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint B1 
     * @title types of arguments - long & int
     */
    public void testVFE3() {
        try {
            Class.forName("dot.junit.opcodes.and_int.d.T_and_int_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint B1 
     * @title types of arguments - int & reference
     */
    public void testVFE4() {
        try {
            Class.forName("dot.junit.opcodes.and_int.d.T_and_int_4");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint A23 
     * @title number of registers
     */
    public void testVFE5() {
        try {
            Class.forName("dot.junit.opcodes.and_int.d.T_and_int_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}
