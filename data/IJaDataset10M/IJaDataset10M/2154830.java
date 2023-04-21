package dxc.junit.verify.t481_1;

import dxc.junit.DxTestCase;
import dxc.junit.DxUtil;

/**
 * 
 */
public class Test_t481_1 extends DxTestCase {

    /**
     * @constraint 4.8.1.1
     * @title code_length must not be zero
     */
    public void testVFE1() {
        try {
            Class.forName("dxc.junit.verify.t481_1.jm.T_t481_1_1");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}
