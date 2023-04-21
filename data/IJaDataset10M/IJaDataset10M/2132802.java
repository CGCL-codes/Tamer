package org.jacex.compiler.operations;

import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * Operation appender for integer comparison operations.
 *
 * @author alno
 */
final class IntegerComparisonOperationAppender extends OperationAppender {

    private final int jumpInsn;

    private final int noJumpInsn;

    public IntegerComparisonOperationAppender(Class<?> resultType, int jumpInsn, int noJumpInsn, Class<?>... argTypes) {
        super(resultType, argTypes);
        this.jumpInsn = jumpInsn;
        this.noJumpInsn = noJumpInsn;
    }

    @Override
    public void appendOp(MethodVisitor mv) {
        Label no = new Label();
        Label end = new Label();
        mv.visitJumpInsn(noJumpInsn, no);
        mv.visitInsn(ICONST_1);
        mv.visitJumpInsn(GOTO, end);
        mv.visitLabel(no);
        mv.visitInsn(ICONST_0);
        mv.visitLabel(end);
    }

    public void appendSelect(MethodVisitor mv, Label yes, Label no) {
        if (yes != null) {
            mv.visitJumpInsn(jumpInsn, yes);
            if (no != null) mv.visitJumpInsn(GOTO, no);
        } else if (no != null) {
            mv.visitJumpInsn(noJumpInsn, no);
        } else {
            throw new RuntimeException("'yes' or 'no' label mustb be specified");
        }
    }
}
