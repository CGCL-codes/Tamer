package net.sf.jexpel.internal.expr.bitwise;

import net.sf.jexpel.Expression;
import net.sf.jexpel.ExpressionException;
import net.sf.jexpel.internal.compiler.Compilable;
import net.sf.jexpel.internal.compiler.CompilerUtils;
import net.sf.jexpel.internal.expr.BinaryExpression;
import net.sf.jexpel.internal.parser.OperatorType;
import org.objectweb.asm.MethodVisitor;

public class BitwiseAndExpression extends BinaryExpression {

    private interface BitAnd {

        Object and(Object value1, Object value2);
    }

    private class AndLong implements BitAnd {

        public Object and(Object value1, Object value2) {
            return (Long) value1 & (Long) value2;
        }
    }

    private class AndInteger implements BitAnd {

        public Object and(Object value1, Object value2) {
            return (Integer) value1 & (Integer) value2;
        }
    }

    private class AndShort implements BitAnd {

        public Object and(Object value1, Object value2) {
            return (Short) value1 & (Short) value2;
        }
    }

    private class AndByte implements BitAnd {

        public Object and(Object value1, Object value2) {
            return (Byte) value1 & (Byte) value2;
        }
    }

    public BitwiseAndExpression(Expression exp1, Expression exp2) throws ExpressionException {
        super(exp1, exp2, OperatorType.BITAND);
        if (exp1.getType() == Long.class || exp1.getType() == long.class || exp2.getType() == Long.class || exp2.getType() == long.class) {
            type = long.class;
            bitand = new AndLong();
        } else if (exp1.getType() == Integer.class || exp1.getType() == int.class || exp2.getType() == Integer.class || exp2.getType() == int.class) {
            type = int.class;
            bitand = new AndInteger();
        } else if (exp1.getType() == Short.class || exp1.getType() == short.class || exp2.getType() == Short.class || exp2.getType() == short.class) {
            type = short.class;
            bitand = new AndShort();
        } else if (exp1.getType() == Byte.class || exp1.getType() == byte.class || exp2.getType() == Byte.class || exp2.getType() == byte.class) {
            type = byte.class;
            bitand = new AndByte();
        } else {
            throw ExpressionException.forInputString(INVALID_ARG, operator.name);
        }
    }

    BitAnd bitand;

    public Object get(Object scope) throws Exception {
        return bitand.and(exp1.get(scope), exp2.get(scope));
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
            mv.visitInsn(LAND);
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
            mv.visitInsn(IAND);
        }
    }
}
