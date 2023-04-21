package org.sodeja.silan;

import org.sodeja.silan.context.Context;
import org.sodeja.silan.instruction.Instruction;

public class Process {

    public final VirtualMachine vm;

    private Context activeContext;

    private SILObject value;

    public Process(VirtualMachine vm) {
        this.vm = vm;
    }

    public void run() {
        while (activeContext != null) {
            Instruction instr = activeContext.nextInstruction();
            instr.execute(this);
        }
    }

    public Context getActiveContext() {
        return activeContext;
    }

    public void setActiveContext(Context activeContext) {
        this.activeContext = activeContext;
    }

    public void setValue(SILObject value) {
        this.value = value;
    }

    public SILObject getValue() {
        return value;
    }
}
