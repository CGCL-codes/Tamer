package org.vps.bcv;

class FCONST extends Instruction {

    FCONST(InstructionContext ctx) {
        super(ctx);
    }

    void execute(Emulator e) {
        e.push(Type._T_FLOAT);
    }
}
