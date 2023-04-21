package org.csiro.darjeeling.infuser.bytecode.instructions;

import org.csiro.darjeeling.infuser.bytecode.InstructionHandle;
import org.csiro.darjeeling.infuser.bytecode.Opcode;
import org.csiro.darjeeling.infuser.structure.BaseType;
import org.csiro.darjeeling.infuser.structure.LocalId;
import org.csiro.darjeeling.infuser.structure.elements.AbstractMethodDefinition;

public abstract class AbstractInvokeInstruction extends LocalIdInstruction {

    protected AbstractMethodDefinition methodDefinition;

    public AbstractInvokeInstruction(Opcode opcode, LocalId localId, AbstractMethodDefinition methodDefinition) {
        super(opcode, localId);
        this.methodDefinition = methodDefinition;
    }

    public AbstractMethodDefinition getMethodDefinition() {
        return methodDefinition;
    }

    public BaseType[] getOperandTypes() {
        return methodDefinition.getArgumentTypes();
    }

    public void setOptimisationHints(InstructionHandle handle) {
        BaseType operandTypes[] = methodDefinition.getArgumentTypes();
        for (int i = 0; i < operandTypes.length; i++) handle.getPreState().getStack().peek(i).setOptimizationHint(operandTypes[operandTypes.length - 1 - i]);
    }

    @Override
    public int getNrOutputValues() {
        return methodDefinition.getReturnType() != BaseType.Void ? 1 : 0;
    }

    @Override
    public BaseType getOuputType(int index, InstructionHandle handle) {
        if (index != 0) throw new IllegalArgumentException("invoke instructions produce at most one output");
        return methodDefinition.getReturnType();
    }

    public String toString() {
        return super.toString() + methodDefinition.getGlobalId() + "[" + methodDefinition.getName() + " " + methodDefinition.getSignature() + "]";
    }
}
