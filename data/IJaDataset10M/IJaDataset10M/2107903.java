package org.tzi.use.uml.ocl.expr;

import org.tzi.use.uml.ocl.type.TypeFactory;
import org.tzi.use.uml.ocl.value.BooleanValue;
import org.tzi.use.uml.ocl.value.Value;

/**
 * Constant boolean expression.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author  Mark Richters
 */
public final class ExpConstBoolean extends Expression {

    private boolean fValue;

    public ExpConstBoolean(boolean b) {
        super(TypeFactory.mkBoolean());
        fValue = b;
    }

    public boolean value() {
        return fValue;
    }

    /**
     * Evaluates expression and returns result value.
     */
    public Value eval(EvalContext ctx) {
        ctx.enter(this);
        Value res = BooleanValue.get(fValue);
        ctx.exit(this, res);
        return res;
    }

    public String toString() {
        return Boolean.toString(fValue);
    }
}
