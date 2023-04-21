package org.jnorm;

import org.apache.commons.lang.ObjectUtils;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.analysis.Value;

public class InstructionLinkValue implements Value {

    private AbstractInsnNode instruction;

    private Value value;

    public InstructionLinkValue(Value value, AbstractInsnNode instruction) {
        this.instruction = instruction;
        this.value = value;
    }

    public AbstractInsnNode getInstruction() {
        return instruction;
    }

    public void setInstruction(AbstractInsnNode instruction) {
        this.instruction = instruction;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public int getSize() {
        return value.getSize();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof InstructionLinkValue) {
            InstructionLinkValue v = (InstructionLinkValue) o;
            return ObjectUtils.equals(value, v.getValue());
        }
        return false;
    }
}
