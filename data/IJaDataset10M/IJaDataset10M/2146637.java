package com.ibm.wala.ipa.slicer;

import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ssa.SSAAbstractInvokeInstruction;

/**
 * A {@link Statement} representing the exceptional return value in a caller,
 * immediately after returning to the caller.
 */
public class ExceptionalReturnCaller extends StatementWithInstructionIndex implements ValueNumberCarrier {

    public ExceptionalReturnCaller(CGNode node, int callIndex) {
        super(node, callIndex);
    }

    public int getValueNumber() {
        return getInstruction().getException();
    }

    @Override
    public SSAAbstractInvokeInstruction getInstruction() {
        return (SSAAbstractInvokeInstruction) super.getInstruction();
    }

    @Override
    public Kind getKind() {
        return Kind.EXC_RET_CALLER;
    }
}
