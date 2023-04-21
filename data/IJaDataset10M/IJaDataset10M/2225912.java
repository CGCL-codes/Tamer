package dxc.junit.verify.t482_11;

import dxc.junit.DxTestCase;
import dxc.junit.DxUtil;
import dxc.junit.verify.t482_11.jm.T_t482_11_2;

/**
 * 
 */
public class Test_t482_11 extends DxTestCase {

    /**
     * @constraint 4.8.2.11
     * @title  instance fields declared in the class may be accessed before
     * calling <init>
     */
    public void testN1() {
        T_t482_11_2 t = new T_t482_11_2();
        assertEquals(11, t.v);
    }

    /**
     * @constraint 4.8.2.11
     * @title  super.<init> or another <init> must be called
     */
    public void testVFE1() {
        try {
            Class.forName("dxc.junit.verify.t482_11.jm.T_t482_11_1");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.11
     * @title only instance fields declared in the class may be accessed
     * before calling <init>
     */
    public void testVFE2() {
        try {
            Class.forName("dxc.junit.verify.t482_11.jm.T_t482_11_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}
