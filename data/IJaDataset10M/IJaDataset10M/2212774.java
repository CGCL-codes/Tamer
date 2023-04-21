package net.sf.jode.bytecode;

import junit.framework.*;
import java.io.*;

public class BlockTest extends TestCase implements Opcodes {

    public BlockTest(String name) {
        super(name);
    }

    public void testJsr() {
        Block b1 = new Block();
        Block b2 = new Block();
        Instruction jsr = Instruction.forOpcode(opc_jsr);
        b1.setCode(new Instruction[] { jsr }, new Block[] { b2, null });
        assertEquals("pop", 0, b1.maxpop);
        assertEquals("push", 0, b1.maxpush);
        assertEquals("delta", 0, b1.delta);
        try {
            b1.setCode(new Instruction[] { jsr }, new Block[] { b2 });
            fail("jsr must have two successors");
        } catch (IllegalArgumentException ex) {
        }
        try {
            b1.setCode(new Instruction[] { jsr }, new Block[] { null, b2 });
            fail("jsr succ mustn't be null");
        } catch (IllegalArgumentException ex) {
        }
        try {
            b1.setCode(new Instruction[] { jsr, Instruction.forOpcode(opc_nop) }, new Block[] { null, b2 });
            fail("jsr must be last in block");
        } catch (IllegalArgumentException ex) {
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new BlockTest("testJsr"));
        return suite;
    }
}
