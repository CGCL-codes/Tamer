package org.ldp.jdasm.attribute.instruction.opcode;

import org.ldp.jdasm.attribute.instruction.*;

/** The I2B java instruction. */
public class I2B extends Instruction {

    /** Default Constructor. */
    public I2B() {
        super(Opcode.I2B);
    }

    /**
	 * Returns the change in byte of the stack after this call.<br>
	 * Every Instruction specify how it will modify the stack: a positive
	 * value it means that one or more value (whose sum in bytes is the returned value)
	 * will be pushed onto the stack at the end of the call; a negative
	 * value it means that such values are popped. */
    public int getStackUse() {
        return 0;
    }

    /** Informs about the type of the values that must be onto the 
	 * stack before the call. See STACK_ constants. */
    public int[] stackRequest() {
        return new int[] { STACK_INT };
    }

    /** Informs about the type of the values that will be pushed 
	 * onto the stack after the call. See STACK_ constants. */
    public int[] stackResponse() {
        return new int[] { STACK_INT };
    }
}
