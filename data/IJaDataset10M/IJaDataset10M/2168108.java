package org.tzi.use.uml.ocl.expr;

import org.tzi.use.uml.ocl.type.Type;
import org.tzi.use.uml.ocl.type.TypeFactory;
import org.tzi.use.uml.ocl.value.SetValue;
import org.tzi.use.uml.ocl.value.Value;

/**
 * Internal operation mapping a single object (resulting from
 * navigation over associations with multiplicity zero or one) to a
 * singleton or empty set.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author      Mark Richters 
 */
public final class ExpObjAsSet extends Expression {

    private Expression fObjExp;

    private Type fElemType;

    public ExpObjAsSet(Expression objExp) {
        super(TypeFactory.mkSet(objExp.type()));
        fObjExp = objExp;
        fElemType = objExp.type();
    }

    /**
     * Evaluates expression and returns result value. 
     */
    public Value eval(EvalContext ctx) {
        ctx.enter(this);
        SetValue res;
        Value val = fObjExp.eval(ctx);
        if (val.isUndefined()) {
            res = new SetValue(fElemType);
        } else {
            res = new SetValue(fElemType, new Value[] { val });
        }
        ctx.exit(this, res);
        return res;
    }

    public String toString() {
        return fObjExp.toString();
    }
}
