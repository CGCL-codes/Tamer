package edu.gatech.cc.concolic.bytecode;

import static edu.gatech.cc.concolic.Concolic.executionStrategy;
import gov.nasa.jpf.jvm.KernelState;
import gov.nasa.jpf.jvm.SystemState;
import gov.nasa.jpf.jvm.ThreadInfo;
import gov.nasa.jpf.jvm.bytecode.Instruction;

public class I2F extends gov.nasa.jpf.jvm.bytecode.I2F {

    @Override
    public Instruction execute(SystemState ss, KernelState ks, ThreadInfo th) {
        Object expr = executionStrategy.execute(this, th.getTopFrame());
        Instruction next = super.execute(ss, ks, th);
        th.setOperandAttrNoClone(expr);
        return next;
    }
}
