package org.ldp.jdasm.attribute.instruction.opcode;

import org.ldp.jdasm.attribute.instruction.*;
import org.ldp.jdasm.*;

/** The LDC_W java instruction. */
public class LDC_W extends Instruction {

    /** Default Constructor. */
    public LDC_W() {
        super(Opcode.LDC_W);
        super.setOperandNumber(1);
        super.setOperandSize(2);
        super.setOperandTypes(OPERAND_CPINFO);
    }

    /** Constructs a LDC_W class with specified constant pool element. */
    public LDC_W(int value) {
        super(Opcode.LDC_W);
        super.setOperandNumber(1);
        super.setOperandSize(2);
        super.setOperandTypes(OPERAND_CPINFO);
        super.setCpoolElement(new Integer(value), DConstantPool.CONSTANT_Integer);
    }

    /** Constructs a LDC_W class with specified constant pool element. */
    public LDC_W(float value) {
        super(Opcode.LDC_W);
        super.setOperandNumber(1);
        super.setOperandSize(2);
        super.setOperandTypes(OPERAND_CPINFO);
        super.setCpoolElement(new Float(value), DConstantPool.CONSTANT_Float);
    }

    /** Constructs a LDC_W class with specified constant pool element.
	 * Use can use this constructor either to add a Class reference (by setting
	 * true the second argument) or to add a String reference (by setting false
	 * the second argument). */
    public LDC_W(String value, boolean isClassReference) {
        super(Opcode.LDC_W);
        super.setOperandNumber(1);
        super.setOperandSize(2);
        super.setOperandTypes(OPERAND_CPINFO);
        super.setCpoolElement(value, isClassReference ? DConstantPool.CONSTANT_Class : DConstantPool.CONSTANT_String);
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
        return new int[] { (cpoolType == DConstantPool.CONSTANT_Class || cpoolType == DConstantPool.CONSTANT_String ? STACK_REFERENCE : ((cpoolType == DConstantPool.CONSTANT_Float ? STACK_FLOAT : ((cpoolType == DConstantPool.CONSTANT_Integer ? STACK_INT : STACK_UNKNOWN))))) };
    }
}
