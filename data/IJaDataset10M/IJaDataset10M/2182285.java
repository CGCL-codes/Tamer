package org.vps.bcv;

class F2L extends Instruction {

    F2L(InstructionContext ctx) {
        super(ctx);
    }

    void execute(Emulator e) {
        e.pop(Type._T_FLOAT);
        e.push(Type._T_LONG);
    }
}
