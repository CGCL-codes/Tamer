package sun.tools.tree;

import sun.tools.java.*;
import java.util.Hashtable;

/**
 * WARNING: The contents of this source file are not part of any
 * supported API.  Code that depends on them does so at its own risk:
 * they are subject to change or removal without notice.
 */
public abstract class BinaryLogicalExpression extends BinaryExpression {

    /**
     * constructor
     */
    public BinaryLogicalExpression(int op, long where, Expression left, Expression right) {
        super(op, where, Type.tBoolean, left, right);
    }

    /**
     * Check a binary expression
     */
    public Vset checkValue(Environment env, Context ctx, Vset vset, Hashtable exp) {
        ConditionVars cvars = new ConditionVars();
        checkCondition(env, ctx, vset, exp, cvars);
        return cvars.vsTrue.join(cvars.vsFalse);
    }

    public abstract void checkCondition(Environment env, Context ctx, Vset vset, Hashtable exp, ConditionVars cvars);

    /**
     * Inline
     */
    public Expression inline(Environment env, Context ctx) {
        left = left.inlineValue(env, ctx);
        right = right.inlineValue(env, ctx);
        return this;
    }
}
