package org.jacex.expressions;

import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public final class AndExpressionAppender extends ExpressionAppender {

    private final ExpressionAppender[] operands;

    public AndExpressionAppender(ExpressionAppender[] operands) {
        super(Boolean.TYPE);
        this.operands = operands;
    }

    @Override
    public void appendSelect(MethodVisitor mv, Label yes, Label no) {
        if (yes != null) {
            if (no != null) {
                for (ExpressionAppender arg : operands) arg.appendSelect(mv, null, no);
                mv.visitJumpInsn(GOTO, yes);
            } else {
                Label n = new Label();
                for (ExpressionAppender arg : operands) arg.appendSelect(mv, null, n);
                mv.visitJumpInsn(GOTO, yes);
                mv.visitLabel(n);
            }
        } else if (no != null) {
            for (ExpressionAppender arg : operands) arg.appendSelect(mv, null, no);
        } else {
            throw new IllegalArgumentException("'yes' or 'no' label must be specified");
        }
    }

    @Override
    public void appendEval(MethodVisitor mv) {
        Label no = new Label();
        Label end = new Label();
        for (ExpressionAppender arg : operands) arg.appendSelect(mv, null, no);
        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, end);
        mv.visitLabel(no);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(end);
    }
}
