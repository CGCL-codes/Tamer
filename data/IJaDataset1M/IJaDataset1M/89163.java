package dot.junit.opcodes.nop;

import dot.junit.DxTestCase;
import dot.junit.opcodes.nop.d.T_nop_1;

public class Test_nop extends DxTestCase {

    /**
     * @title tests nop
     */
    public void testN1() {
        T_nop_1 t = new T_nop_1();
        assertTrue(t.run());
    }
}
