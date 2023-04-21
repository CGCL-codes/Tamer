package com.maschinenstuermer.profiler.transform;

import java.lang.reflect.Method;
import org.ow2.util.asm.MethodVisitor;
import org.ow2.util.asm.Type;
import org.ow2.util.asm.commons.AdviceAdapter;
import com.maschinenstuermer.profiler.model.TraceModelBuilder;

class TransactionEndAdviceAdapter extends AdviceAdapter {

    private static final String TRACE_MODEL_BUILDER_CLAZZ = Type.getInternalName(TraceModelBuilder.class);

    private static final Method END_TRANSACTION_METHOD;

    private static final String END_TRANSACTION_METHOD_DESC;

    static {
        try {
            END_TRANSACTION_METHOD = TraceModelBuilder.class.getDeclaredMethod("endTransaction", int.class);
            END_TRANSACTION_METHOD_DESC = Type.getMethodDescriptor(END_TRANSACTION_METHOD);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private final String className;

    public TransactionEndAdviceAdapter(final MethodVisitor mv, final int access, final String name, final String desc, final String className) {
        super(mv, access, name, desc);
        this.className = className;
    }

    @Override
    protected void onMethodExit(final int opcode) {
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, className, "getStatus", "()I");
        mv.visitMethodInsn(INVOKESTATIC, TRACE_MODEL_BUILDER_CLAZZ, END_TRANSACTION_METHOD.getName(), END_TRANSACTION_METHOD_DESC);
    }
}
