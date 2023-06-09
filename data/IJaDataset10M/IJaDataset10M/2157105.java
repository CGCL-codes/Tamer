package sun.tools.tree;

import sun.tools.java.*;
import sun.tools.asm.Assembler;
import sun.tools.asm.Label;

/**
 * WARNING: The contents of this source file are not part of any
 * supported API.  Code that depends on them does so at its own risk:
 * they are subject to change or removal without notice.
 */
public class EqualExpression extends BinaryEqualityExpression {

    /**
     * constructor
     */
    public EqualExpression(long where, Expression left, Expression right) {
        super(EQ, where, left, right);
    }

    /**
     * Evaluate
     */
    Expression eval(int a, int b) {
        return new BooleanExpression(where, a == b);
    }

    Expression eval(long a, long b) {
        return new BooleanExpression(where, a == b);
    }

    Expression eval(float a, float b) {
        return new BooleanExpression(where, a == b);
    }

    Expression eval(double a, double b) {
        return new BooleanExpression(where, a == b);
    }

    Expression eval(boolean a, boolean b) {
        return new BooleanExpression(where, a == b);
    }

    /**
     * Simplify
     */
    Expression simplify() {
        if (left.isConstant() && !right.isConstant()) {
            return new EqualExpression(where, right, left);
        }
        return this;
    }

    /**
     * Code
     */
    void codeBranch(Environment env, Context ctx, Assembler asm, Label lbl, boolean whenTrue) {
        left.codeValue(env, ctx, asm);
        switch(left.type.getTypeCode()) {
            case TC_BOOLEAN:
            case TC_INT:
                if (!right.equals(0)) {
                    right.codeValue(env, ctx, asm);
                    asm.add(where, whenTrue ? opc_if_icmpeq : opc_if_icmpne, lbl, whenTrue);
                    return;
                }
                break;
            case TC_LONG:
                right.codeValue(env, ctx, asm);
                asm.add(where, opc_lcmp);
                break;
            case TC_FLOAT:
                right.codeValue(env, ctx, asm);
                asm.add(where, opc_fcmpl);
                break;
            case TC_DOUBLE:
                right.codeValue(env, ctx, asm);
                asm.add(where, opc_dcmpl);
                break;
            case TC_ARRAY:
            case TC_CLASS:
            case TC_NULL:
                if (right.equals(0)) {
                    asm.add(where, whenTrue ? opc_ifnull : opc_ifnonnull, lbl, whenTrue);
                } else {
                    right.codeValue(env, ctx, asm);
                    asm.add(where, whenTrue ? opc_if_acmpeq : opc_if_acmpne, lbl, whenTrue);
                }
                return;
            default:
                throw new CompilerError("Unexpected Type");
        }
        asm.add(where, whenTrue ? opc_ifeq : opc_ifne, lbl, whenTrue);
    }
}
