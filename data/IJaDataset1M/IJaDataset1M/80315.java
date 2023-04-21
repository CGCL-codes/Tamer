package dxc.junit.verify.t481_6;

import dxc.junit.DxTestCase;
import dxc.junit.DxUtil;

/**
 * 
 */
public class Test_t481_6 extends DxTestCase {

    /**
     * @constraint 4.8.1.6
     * @title Last opcode instruction shall be at offset code_length -1.
     * The idea of the test is to put invalid opcode at offset code_length -1.
     */
    public void testVFE1() {
        try {
            Class.forName("dxc.junit.verify.t481_6.jm.T_t481_6_1");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}
