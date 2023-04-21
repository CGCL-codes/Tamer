package org.vps.bcv;

class D2I extends Instruction {

    D2I(InstructionContext ctx) {
        super(ctx);
    }

    void execute(Emulator e) {
        e.pop(Type._T_DOUBLE);
        e.push(Type._T_INT);
    }
}
