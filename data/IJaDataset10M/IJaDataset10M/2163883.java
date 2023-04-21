package com.maschinenstuermer.profiler.transform;

import java.lang.reflect.Method;
import org.ow2.util.asm.MethodVisitor;
import org.ow2.util.asm.Type;
import org.ow2.util.asm.commons.AdviceAdapter;
import com.maschinenstuermer.profiler.model.TraceModelBuilder;

class TransactionBeginAdviceAdapter extends AdviceAdapter {

    private static final String TRACE_MODEL_BUILDER_CLAZZ = Type.getInternalName(TraceModelBuilder.class);

    private static final Method BEGIN_TRANSACTION_METHOD;

    private static final String BEGIN_TRANSACTION_METHOD_DESC;

    static {
        try {
            BEGIN_TRANSACTION_METHOD = TraceModelBuilder.class.getDeclaredMethod("beginTransaction");
            BEGIN_TRANSACTION_METHOD_DESC = Type.getMethodDescriptor(BEGIN_TRANSACTION_METHOD);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public TransactionBeginAdviceAdapter(final MethodVisitor mv, final int access, final String name, final String desc) {
        super(mv, access, name, desc);
    }

    @Override
    protected void onMethodEnter() {
        mv.visitMethodInsn(INVOKESTATIC, TRACE_MODEL_BUILDER_CLAZZ, BEGIN_TRANSACTION_METHOD.getName(), BEGIN_TRANSACTION_METHOD_DESC);
        mv.visitInsn(POP);
    }
}
