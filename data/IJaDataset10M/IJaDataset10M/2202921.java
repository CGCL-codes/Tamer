package org.vps.bcv;

class JSR_W extends JumpInstr {

    JSR_W(InstructionContext ctx) {
        super(ctx);
        offset = ((Integer) ctx.args[0]).intValue();
        target = offset + ctx.pc;
    }

    void execute(Emulator e) {
        if (e.cldc_limit) {
            throw new Stop(VerifierResults.EC_RT_JSR, "CLDC doesn't allow JSR_W");
        }
        e.execute_jsr(target, ctx.pc + 5);
    }

    void reportTargets(Emulator e) {
    }
}
