package net.sf.jexpel.internal.expr.logic;

import net.sf.jexpel.Expression;
import net.sf.jexpel.ExpressionException;
import net.sf.jexpel.internal.compiler.Compilable;
import net.sf.jexpel.internal.expr.BinaryExpression;
import net.sf.jexpel.internal.parser.OperatorType;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class OrExpression extends BinaryExpression {

    public OrExpression(Expression exp1, Expression exp2) throws ExpressionException {
        super(exp1, exp2, OperatorType.OR);
        if ((exp1.getType() != Boolean.class && exp1.getType() != boolean.class) || (exp1.getType() != Boolean.class && exp1.getType() != boolean.class)) {
            throw ExpressionException.forInputString(INVALID_ARG, operator.name);
        }
        this.type = boolean.class;
    }

    public Object get(Object scope) throws Exception {
        return (Boolean) exp1.get(scope) || (Boolean) exp2.get(scope);
    }

    public void compileGet(MethodVisitor mv) throws Exception {
        if (isConstant()) {
            mv.visitLdcInsn(get(null));
        } else {
            Label lb1 = new Label();
            Label lb2 = new Label();
            if (((Compilable) exp1).isConstant()) {
                mv.visitLdcInsn(exp1.get(null));
            } else {
                ((Compilable) exp1).compileGet(mv);
            }
            if (exp1.getType() == Boolean.class) {
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z");
            }
            mv.visitJumpInsn(IFNE, lb1);
            if (((Compilable) exp2).isConstant()) {
                mv.visitLdcInsn(exp2.get(null));
            } else {
                ((Compilable) exp2).compileGet(mv);
            }
            if (exp2.getType() == Boolean.class) {
                mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z");
            }
            mv.visitJumpInsn(IFNE, lb1);
            mv.visitInsn(ICONST_0);
            mv.visitJumpInsn(GOTO, lb2);
            mv.visitLabel(lb1);
            mv.visitInsn(ICONST_1);
            mv.visitLabel(lb2);
        }
    }
}
