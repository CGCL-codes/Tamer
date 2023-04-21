package dxc.junit.opcodes.fstore_1;

import dxc.junit.DxTestCase;
import dxc.junit.DxUtil;
import dxc.junit.opcodes.fstore_1.jm.T_fstore_1_1;
import dxc.junit.opcodes.fstore_1.jm.T_fstore_1_5;

public class Test_fstore_1 extends DxTestCase {

    /**
     * @title value is stored
     */
    public void testN1() {
        assertEquals(2f, T_fstore_1_1.run());
    }

    /**
     * @title equality of fstore_<n> and fstore <n>
     */
    public void testN2() {
        assertTrue(T_fstore_1_5.run());
    }

    /**
     * @constraint 4.8.1.22
     * @title index must be no greater than the value
     * of max_locals-1
     */
    public void testVFE1() {
        try {
            Class.forName("dxc.junit.opcodes.fstore_1.jm.T_fstore_1_2");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.1
     * @title types of argument - double
     */
    public void testVFE2() {
        try {
            Class.forName("dxc.junit.opcodes.fstore_1.jm.T_fstore_1_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.1
     * @title types of argument - long
     */
    public void testVFE3() {
        try {
            Class.forName("dxc.junit.opcodes.fstore_1.jm.T_fstore_1_4");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}
