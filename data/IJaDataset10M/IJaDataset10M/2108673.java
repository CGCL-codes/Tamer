package org.csiro.darjeeling.infuser.bytecode.instructions;

import org.csiro.darjeeling.infuser.bytecode.Instruction;
import org.csiro.darjeeling.infuser.bytecode.Opcode;

public abstract class PushInstruction extends Instruction {

    protected long value;

    public PushInstruction(Opcode opcode, long value) {
        super(opcode);
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
