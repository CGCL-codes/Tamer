package com.fastaop.transform;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class AfterMixin extends AdviceAdapter {

    private String name;

    private Label timeVarEnd = new Label();

    private final String clazz;

    private final String pointcutId;

    public AfterMixin(String clazz, MethodVisitor mv, int acc, String name, String desc, String pointcutId) {
        super(mv, acc, name, desc);
        this.clazz = clazz;
        this.name = name;
        this.pointcutId = pointcutId;
    }

    public void onMethodExit(int opcode) {
        if (opcode != ATHROW) {
            mv.visitLdcInsn(pointcutId);
            mv.visitLdcInsn(clazz);
            mv.visitLdcInsn(name);
            mv.visitVarInsn(ALOAD, 0);
            if (opcode == Opcodes.ATHROW) {
                mv.visitInsn(ICONST_1);
            } else {
                mv.visitInsn(ICONST_0);
            }
            mv.visitMethodInsn(INVOKESTATIC, "com/fastaop/dispatcher/FastDispatcher", "dispatchEnd", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;Z)V");
        }
    }

    public void visitMaxs(int stack, int locals) {
        visitLabel(timeVarEnd);
        super.visitMaxs(stack + 5, locals);
    }
}
