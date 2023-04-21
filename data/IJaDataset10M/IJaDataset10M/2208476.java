package org.sodeja.silan.instruction;

import org.sodeja.silan.Process;
import org.sodeja.silan.SILObject;

public class PushStringLiteralInstruction implements PushInstruction {

    private final String value;

    public PushStringLiteralInstruction(String value) {
        this.value = value;
    }

    @Override
    public void execute(Process process) {
        SILObject obj = process.vm.objects.newString(value);
        process.getActiveContext().push(obj);
    }
}
