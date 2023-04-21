package net.sf.jwisp.internal.jexpel.internal.expr.logic;

import net.sf.jwisp.internal.jexpel.Expression;
import net.sf.jwisp.internal.jexpel.ExpressionException;
import net.sf.jwisp.internal.jexpel.internal.compiler.Compilable;
import net.sf.jwisp.internal.jexpel.internal.compiler.CompilerUtils;
import net.sf.jwisp.internal.jexpel.internal.expr.ConstantExpression;
import net.sf.jwisp.internal.jexpel.internal.parser.OperatorType;
import org.objectweb.asm.MethodVisitor;

public class EqualsExpression extends LogicExpression {

    public EqualsExpression(Expression exp1, Expression exp2) throws ExpressionException {
        super(exp1, exp2, OperatorType.EQ);
    }

    @Override
    protected Object compare(Object scope) throws Exception {
        Object vl1 = exp1.get(scope);
        Object vl2 = exp2.get(scope);
        return (vl1 == vl2 || vl1.equals(vl2));
    }

    public void compileGet(MethodVisitor mv) throws Exception {
        if (isConstant()) {
            mv.visitLdcInsn(get(null));
        } else if (exp1 instanceof ConstantExpression && ((ConstantExpression) exp1).get(null) == null) {
            ((Compilable) exp2).compileGet(mv);
            CompilerUtils.compare(mv, IFNULL);
        } else if (exp2 instanceof ConstantExpression && ((ConstantExpression) exp2).get(null) == null) {
            ((Compilable) exp1).compileGet(mv);
            CompilerUtils.compare(mv, IFNULL);
        } else if (exp1.getType() == Double.class || exp1.getType() == double.class || exp2.getType() == Double.class || exp2.getType() == double.class) {
            if (((Compilable) exp1).isConstant()) {
                mv.visitLdcInsn(exp1.get(null));
            } else {
                ((Compilable) exp1).compileGet(mv);
            }
            CompilerUtils.convert2primitive(mv, exp1.getType(), double.class);
            if (((Compilable) exp2).isConstant()) {
                mv.visitLdcInsn(exp2.get(null));
            } else {
                ((Compilable) exp2).compileGet(mv);
            }
            CompilerUtils.convert2primitive(mv, exp2.getType(), double.class);
            mv.visitInsn(DCMPL);
            CompilerUtils.compare(mv, IFEQ);
        } else if (exp1.getType() == Float.class || exp1.getType() == float.class || exp2.getType() == Float.class || exp2.getType() == float.class) {
            if (((Compilable) exp1).isConstant()) {
                mv.visitLdcInsn(exp1.get(null));
            } else {
                ((Compilable) exp1).compileGet(mv);
            }
            CompilerUtils.convert2primitive(mv, exp1.getType(), float.class);
            if (((Compilable) exp2).isConstant()) {
                mv.visitLdcInsn(exp2.get(null));
            } else {
                ((Compilable) exp2).compileGet(mv);
            }
            CompilerUtils.convert2primitive(mv, exp2.getType(), float.class);
            mv.visitInsn(FCMPL);
            CompilerUtils.compare(mv, IFEQ);
        } else if (exp1.getType() == Long.class || exp1.getType() == long.class || exp2.getType() == Long.class || exp2.getType() == long.class) {
            if (((Compilable) exp1).isConstant()) {
                mv.visitLdcInsn(exp1.get(null));
            } else {
                ((Compilable) exp1).compileGet(mv);
            }
            CompilerUtils.convert2primitive(mv, exp1.getType(), long.class);
            if (((Compilable) exp2).isConstant()) {
                mv.visitLdcInsn(exp2.get(null));
            } else {
                ((Compilable) exp2).compileGet(mv);
            }
            CompilerUtils.convert2primitive(mv, exp2.getType(), long.class);
            mv.visitInsn(LCMP);
            CompilerUtils.compare(mv, IFEQ);
        } else if (exp1.getType() == Integer.class || exp1.getType() == int.class || exp2.getType() == Integer.class || exp2.getType() == int.class || exp1.getType() == Short.class || exp1.getType() == short.class || exp2.getType() == Short.class || exp2.getType() == short.class || exp1.getType() == Byte.class || exp1.getType() == byte.class || exp2.getType() == Byte.class || exp2.getType() == byte.class || exp1.getType() == char.class || exp2.getType() == char.class || exp1.getType() == Character.class || exp2.getType() == Character.class) {
            if (((Compilable) exp1).isConstant()) {
                mv.visitLdcInsn(exp1.get(null));
            } else {
                ((Compilable) exp1).compileGet(mv);
            }
            if (exp1.getType() == Character.class) CompilerUtils.invokeMethod(mv, Character.class, "charValue", new Class<?>[] {}, false); else CompilerUtils.convert2primitive(mv, exp1.getType(), int.class);
            if (((Compilable) exp2).isConstant()) {
                mv.visitLdcInsn(exp2.get(null));
            } else {
                ((Compilable) exp2).compileGet(mv);
            }
            if (exp2.getType() == Character.class) CompilerUtils.invokeMethod(mv, Character.class, "charValue", new Class<?>[] {}, false); else CompilerUtils.convert2primitive(mv, exp2.getType(), int.class);
            CompilerUtils.compare(mv, IF_ICMPEQ);
        } else {
            if (((Compilable) exp1).isConstant()) {
                mv.visitLdcInsn(exp1.get(null));
            } else {
                ((Compilable) exp1).compileGet(mv);
            }
            if (((Compilable) exp2).isConstant()) {
                mv.visitLdcInsn(exp2.get(null));
            } else {
                ((Compilable) exp2).compileGet(mv);
            }
            CompilerUtils.invokeMethod(mv, exp1.getType(), "equals", new Class<?>[] { Object.class }, false);
        }
    }
}
