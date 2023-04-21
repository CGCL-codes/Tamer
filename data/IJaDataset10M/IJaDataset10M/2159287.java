package net.sf.jexpel.internal.expr.arithmetic;

import java.math.BigDecimal;
import net.sf.jexpel.Expression;
import net.sf.jexpel.ExpressionException;
import net.sf.jexpel.internal.compiler.Compilable;
import net.sf.jexpel.internal.compiler.CompilerUtils;
import net.sf.jexpel.internal.expr.BinaryExpression;
import net.sf.jexpel.internal.parser.OperatorType;
import org.objectweb.asm.MethodVisitor;

public class RemainderExpression extends BinaryExpression {

    private interface Remainder {

        Object div(Object vl1, Object vl2);
    }

    private class DivInteger implements Remainder {

        public Object div(Object vl1, Object vl2) {
            return ((Number) vl1).intValue() % ((Number) vl2).intValue();
        }
    }

    private class DivLong implements Remainder {

        public Object div(Object vl1, Object vl2) {
            return ((Number) vl1).longValue() % ((Number) vl2).longValue();
        }
    }

    private class DivBigDecimal implements Remainder {

        public Object div(Object vl1, Object vl2) {
            return ((BigDecimal) vl1).remainder((BigDecimal) vl2);
        }
    }

    public RemainderExpression(Expression exp1, Expression exp2) throws ExpressionException {
        super(exp1, exp2, OperatorType.REMAINDER);
        if (exp1.getType() == Long.class || exp1.getType() == long.class || exp2.getType() == Long.class || exp2.getType() == long.class) {
            type = long.class;
            remainder = new DivLong();
        } else if (exp1.getType() == Integer.class || exp1.getType() == int.class || exp2.getType() == Integer.class || exp2.getType() == int.class || exp1.getType() == Short.class || exp1.getType() == short.class || exp2.getType() == Short.class || exp2.getType() == short.class || exp1.getType() == Byte.class || exp1.getType() == byte.class || exp2.getType() == Byte.class || exp2.getType() == byte.class) {
            type = int.class;
            remainder = new DivInteger();
        } else if (exp1.getType() == BigDecimal.class && exp2.getType() == BigDecimal.class) {
            type = BigDecimal.class;
            remainder = new DivBigDecimal();
        } else {
            throw ExpressionException.forInputString(INVALID_ARG, operator.name);
        }
    }

    Remainder remainder;

    public Object get(Object scope) throws Exception {
        return remainder.div(exp1.get(scope), exp2.get(scope));
    }

    public void compileGet(MethodVisitor mv) throws Exception {
        if (isConstant()) {
            mv.visitLdcInsn(get(null));
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
            mv.visitInsn(LREM);
        } else if (exp1.getType() == Integer.class || exp1.getType() == int.class || exp2.getType() == Integer.class || exp2.getType() == int.class || exp1.getType() == Short.class || exp1.getType() == short.class || exp2.getType() == Short.class || exp2.getType() == short.class || exp1.getType() == Byte.class || exp1.getType() == byte.class || exp2.getType() == Byte.class || exp2.getType() == byte.class) {
            if (((Compilable) exp1).isConstant()) {
                mv.visitLdcInsn(exp1.get(null));
            } else {
                ((Compilable) exp1).compileGet(mv);
            }
            CompilerUtils.convert2primitive(mv, exp1.getType(), int.class);
            if (((Compilable) exp2).isConstant()) {
                mv.visitLdcInsn(exp2.get(null));
            } else {
                ((Compilable) exp2).compileGet(mv);
            }
            CompilerUtils.convert2primitive(mv, exp2.getType(), int.class);
            mv.visitInsn(IREM);
        } else if (exp1.getType() == BigDecimal.class && exp2.getType() == BigDecimal.class) {
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/math/BigDecimal", "remainder", "(Ljava/math/BigDecimal;)Ljava/math/BigDecimal;");
        }
    }
}
