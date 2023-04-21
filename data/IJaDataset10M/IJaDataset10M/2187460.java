package org.ldp.jdasm.attribute.instruction.opcode;

import org.ldp.jdasm.attribute.instruction.*;

/** The ALOAD_2 java instruction. */
public class ALOAD_2 extends Instruction {

    /** Default Constructor. */
    public ALOAD_2() {
        super(Opcode.ALOAD_2);
        super.setRegister((byte) 2);
    }

    /** True if this Instruction uses a register in the local variable array 
	 * which is of category 1 (it means 1 byte long (int,float,reference)) */
    public boolean isCategory1() {
        return true;
    }

    /** Informs about the type of the values that must be into the 
	 * local variable array before the call. See REGISTER_ constants. */
    public int registerRequest() {
        return Instruction.REGISTER_REFERENCE;
    }

    /**
	 * Returns the change in byte of the stack after this call.<br>
	 * Every Instruction specify how it will modify the stack: a positive
	 * value it means that one or more value (whose sum in bytes is the returned value)
	 * will be pushed onto the stack at the end of the call; a negative
	 * value it means that such values are popped. */
    public int getStackUse() {
        return 1;
    }

    /** Informs about the type of the values that will be pushed 
	 * onto the stack after the call. See STACK_ constants. */
    public int[] stackResponse() {
        return new int[] { STACK_REFERENCE };
    }
}
