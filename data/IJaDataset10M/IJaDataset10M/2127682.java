package dot.junit.verify.b17;

import dot.junit.DxTestCase;
import dot.junit.DxUtil;

public class Test_b17 extends DxTestCase {

    /**
     * @constraint B17
     * 
     * @title attempt to leave insns array without return or throw.
     * Since this constraint is trivial to be checked by the verifier,
     * it is sufficient to have a trivial test.
     */
    public void testVFE1() {
        try {
            Class.forName("dot.junit.verify.b17.d.T_b17_1");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}
