package org.vps.bcv;

class INVOKEVIRTUAL extends InvokeInstr {

    INVOKEVIRTUAL(InstructionContext ctx) {
        super(ctx, false, false, false);
    }

    void verify() {
        super.verify();
        if ("<init>".equals(methodRef.name)) {
            throw new Stop(VerifierResults.EC_OP_INVOKEVRITUAL_INIT, "Calling constructor through invokevirtual");
        }
        if ((mi != null) && ((mi.access_flags & ACC_PRIVATE) != 0)) {
            throw new Stop(VerifierResults.EC_OP_INVOKEVIRTUAL_PRIVATE, "<invokevirtual> used on private method");
        }
    }

    void execute(Emulator e) {
        Type[] args = methodRef.argTypes;
        for (int i = args.length - 1; i >= 0; i--) {
            e.pop(args[i]);
        }
        e.pop(methodSource);
        if (!methodRef.returnType.isVoid) {
            e.push(methodRef.returnType);
        }
    }
}
