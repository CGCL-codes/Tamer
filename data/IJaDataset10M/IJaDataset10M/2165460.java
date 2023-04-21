package org.ldp.jdasm.attribute.instruction.opcode;

import org.ldp.jdasm.attribute.instruction.*;

/** The LSTORE_2 java instruction. */
public class LSTORE_2 extends Instruction {

    /** Default Constructor. */
    public LSTORE_2() {
        super(Opcode.LSTORE_2);
        super.setRegister((byte) 2);
    }

    /** True if this Instruction uses a register in the local variable array 
	 * which is of category 2 (it means 2 byte long (long,double)) */
    public boolean isCategory2() {
        return true;
    }

    /** Informs about the type of the values that will be stored 
	 * into the local variable array after the call. See REGISTER_ constants. */
    public int registerResponse() {
        return Instruction.REGISTER_LONG;
    }

    /**
	 * Returns the change in byte of the stack after this call.<br>
	 * Every Instruction specify how it will modify the stack: a positive
	 * value it means that one or more value (whose sum in bytes is the returned value)
	 * will be pushed onto the stack at the end of the call; a negative
	 * value it means that such values are popped. */
    public int getStackUse() {
        return -2;
    }

    /** Informs about the type of the values that must be onto the 
	 * stack before the call. See STACK_ constants. */
    public int[] stackRequest() {
        return new int[] { STACK_LONG };
    }
}
