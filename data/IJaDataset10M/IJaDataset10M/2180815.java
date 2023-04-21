package dxc.junit.opcodes.areturn;

import dxc.junit.DxTestCase;
import dxc.junit.DxUtil;
import dxc.junit.opcodes.areturn.jm.T_areturn_1;
import dxc.junit.opcodes.areturn.jm.T_areturn_12;
import dxc.junit.opcodes.areturn.jm.T_areturn_13;
import dxc.junit.opcodes.areturn.jm.T_areturn_2;
import dxc.junit.opcodes.areturn.jm.T_areturn_6;
import dxc.junit.opcodes.areturn.jm.T_areturn_7;
import dxc.junit.opcodes.areturn.jm.T_areturn_8;
import dxc.junit.opcodes.areturn.jm.T_areturn_9;

public class Test_areturn extends DxTestCase {

    /**
     * @title  simple
     */
    public void testN1() {
        T_areturn_1 t = new T_areturn_1();
        assertEquals("hello", t.run());
    }

    /**
     * @title  simple
     */
    public void testN2() {
        T_areturn_1 t = new T_areturn_1();
        assertEquals(t, t.run2());
    }

    /**
     * @title  simple
     */
    public void testN3() {
        T_areturn_1 t = new T_areturn_1();
        Integer a = 12345;
        assertEquals(a, t.run3());
    }

    /**
     * @title test for null
     */
    public void testN4() {
        T_areturn_2 t = new T_areturn_2();
        assertNull(t.run());
    }

    /**
     * @title  check that frames are discarded and reinstananted correctly
     */
    public void testN5() {
        T_areturn_6 t = new T_areturn_6();
        assertEquals("hello", t.run());
    }

    /**
     * @title  check that monitor is released by areturn
     */
    public void testN6() {
        assertTrue(T_areturn_7.execute());
    }

    /**
     * @title  assignment compatibility (TChild returned as TSuper)
     */
    public void testN7() {
        T_areturn_12 t = new T_areturn_12();
        assertTrue(t.run());
    }

    /**
     * @title  assignment compatibility (TChild returned as TInterface)
     */
    public void testN8() {
        T_areturn_13 t = new T_areturn_13();
        assertTrue(t.run());
    }

    /**
     * @title  Method is synchronized but thread is not monitor owner
     */
    public void testE1() {
        T_areturn_8 t = new T_areturn_8();
        try {
            assertTrue(t.run());
            fail("expected IllegalMonitorStateException");
        } catch (IllegalMonitorStateException imse) {
        }
    }

    /**
     * @title  Lock structural rule 1 is violated
     */
    public void testE2() {
        T_areturn_9 t = new T_areturn_9();
        try {
            assertEquals("abc", t.run());
            System.out.print("dvmvfe:");
        } catch (IllegalMonitorStateException imse) {
        }
    }

    /**
     * @constraint 4.8.2.14
     * @title method's return type - void
     */
    public void testVFE1() {
        try {
            Class.forName("dxc.junit.opcodes.areturn.jm.T_areturn_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.14
     * @title method's return type - float
     */
    public void testVFE2() {
        try {
            Class.forName("dxc.junit.opcodes.areturn.jm.T_areturn_4");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.1
     * @title number of arguments
     */
    public void testVFE3() {
        try {
            Class.forName("dxc.junit.opcodes.areturn.jm.T_areturn_5");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.1
     * @title types of argument - float
     */
    public void testVFE4() {
        try {
            Class.forName("dxc.junit.opcodes.areturn.jm.T_areturn_10");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.5
     * @title stack size
     */
    public void testVFE5() {
        try {
            Class.forName("dxc.junit.opcodes.areturn.jm.T_areturn_11");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.14
     * @title assignment incompatible references
     */
    public void testVFE6() {
        try {
            Class.forName("dxc.junit.opcodes.areturn.jm.T_areturn_14");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.14
     * @title assignment incompatible references
     */
    @SuppressWarnings("cast")
    public void testVFE7() {
        try {
            RunnerGenerator rg = (RunnerGenerator) Class.forName("dxc.junit.opcodes.areturn.jm.T_areturn_15").newInstance();
            Runner r = rg.run();
            assertFalse(r instanceof Runner);
            assertFalse(Runner.class.isAssignableFrom(r.getClass()));
            r.doit();
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}
