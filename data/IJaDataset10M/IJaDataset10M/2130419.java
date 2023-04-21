package dot.junit.opcodes.move_object_from16;

import dot.junit.DxTestCase;
import dot.junit.DxUtil;
import dot.junit.opcodes.move_object_from16.d.T_move_object_from16_1;

public class Test_move_object_from16 extends DxTestCase {

    /**
     * @title v4999 -> v255 -> v1
     */
    public void testN1() {
        T_move_object_from16_1 t = new T_move_object_from16_1();
        assertEquals(t.run(), t);
    }

    /**
     * @constraint A23 
     * @title number of registers - src is not valid
     */
    public void testVFE1() {
        try {
            Class.forName("dot.junit.opcodes.move_object_from16.d.T_move_object_from16_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint A23 
     * @title number of registers - dst is not valid
     */
    public void testVFE2() {
        try {
            Class.forName("dot.junit.opcodes.move_object_from16.d.T_move_object_from16_4");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint B1 
     * @title src register contains integer
     */
    public void testVFE3() {
        try {
            Class.forName("dot.junit.opcodes.move_object_from16.d.T_move_object_from16_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint B1 
     * @title src register contains wide
     */
    public void testVFE4() {
        try {
            Class.forName("dot.junit.opcodes.move_object_from16.d.T_move_object_from16_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint B1 
     * @title src register is a part of reg pair
     */
    public void testVFE5() {
        try {
            Class.forName("dot.junit.opcodes.move_object_from16.d.T_move_object_from16_7");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint B18 
     * @title When writing to a register that is one half of a 
     * register pair, but not touching the other half, the old register pair gets broken 
     * up, and the other register involved in it becomes undefined.
     */
    public void testVFE6() {
        try {
            Class.forName("dot.junit.opcodes.move_object_from16.d.T_move_object_from16_8");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}
