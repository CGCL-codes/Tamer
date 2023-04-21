package dxc.junit.opcodes.athrow;

import dxc.junit.DxTestCase;
import dxc.junit.DxUtil;
import dxc.junit.opcodes.athrow.jm.T_athrow_1;
import dxc.junit.opcodes.athrow.jm.T_athrow_11;
import dxc.junit.opcodes.athrow.jm.T_athrow_12;
import dxc.junit.opcodes.athrow.jm.T_athrow_2;
import dxc.junit.opcodes.athrow.jm.T_athrow_4;
import dxc.junit.opcodes.athrow.jm.T_athrow_5;
import dxc.junit.opcodes.athrow.jm.T_athrow_8;

public class Test_athrow extends DxTestCase {

    /**
     * @title normal test
     */
    public void testN1() {
        T_athrow_1 t = new T_athrow_1();
        try {
            t.run();
            fail("must throw a RuntimeException");
        } catch (RuntimeException re) {
        }
    }

    /**
     * @title  Throwing of the objectref on the class Throwable
     */
    public void testN2() {
        T_athrow_2 t = new T_athrow_2();
        try {
            t.run();
            fail("must throw a Throwable");
        } catch (Throwable e) {
        }
    }

    /**
     * @title  Throwing of the objectref on the subclass of Throwable
     */
    public void testN3() {
        T_athrow_8 t = new T_athrow_8();
        try {
            t.run();
            fail("must throw a Error");
        } catch (Error e) {
        }
    }

    /**
     * @title  Nearest matching catch must be executed in case of exception
     */
    public void testN4() {
        T_athrow_12 t = new T_athrow_12();
        assertTrue(t.run());
    }

    /**
     * @title  NullPointerException If objectref is null, athrow throws
     * a NullPointerException instead of objectref
     */
    public void testE1() {
        T_athrow_4 t = new T_athrow_4();
        try {
            t.run();
            fail("expected NullPointerException");
        } catch (NullPointerException npe) {
        }
    }

    /**
     * @title  IllegalMonitorStateException expected
     */
    public void testE2() {
        T_athrow_5 t = new T_athrow_5();
        try {
            t.run();
            fail("expected IllegalMonitorStateException");
        } catch (IllegalMonitorStateException imse) {
        }
    }

    /**
     * @title IllegalMonitorStateException if structural lock rule violated -
     */
    public void testE3() {
        T_athrow_11 t = new T_athrow_11();
        try {
            t.run();
            fail("expected IllegalMonitorStateException");
        } catch (IllegalMonitorStateException imse) {
        } catch (NullPointerException npe) {
            System.out.print("dvmvfe:");
        }
    }

    /**
     * @constraint 4.8.1.19
     * @title constant pool index
     */
    public void testVFE1() {
        try {
            Class.forName("dxc.junit.opcodes.athrow.jm.T_athrow_3");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.1
     * @title number of arguments
     */
    public void testVFE2() {
        try {
            Class.forName("dxc.junit.opcodes.athrow.jm.T_athrow_6");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.1
     * @title type of argument - float
     */
    public void testVFE3() {
        try {
            Class.forName("dxc.junit.opcodes.athrow.jm.T_athrow_7");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.1 
     * @title type of argument - String
     */
    public void testVFE4() {
        try {
            Class.forName("dxc.junit.opcodes.athrow.jm.T_athrow_10");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }

    /**
     * @constraint 4.8.2.19 
     * @title Throwing of the objectref on the class which is not
     *          the class Throwable or a subclass of Throwable.
     */
    public void testVFE5() {
        try {
            Class.forName("dxc.junit.opcodes.athrow.jm.T_athrow_9");
            fail("expected a verification exception");
        } catch (Throwable t) {
            DxUtil.checkVerifyException(t);
        }
    }
}
