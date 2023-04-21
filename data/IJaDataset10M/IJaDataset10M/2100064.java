package org.tzi.use.uml.ocl.expr;

import org.tzi.use.uml.ocl.type.CollectionType;
import org.tzi.use.uml.ocl.type.TypeFactory;
import org.tzi.use.uml.ocl.value.BagValue;
import org.tzi.use.uml.ocl.value.Value;

/**
 * Constant bag literal.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author  Mark Richters
 */
public final class ExpBagLiteral extends ExpCollectionLiteral {

    public ExpBagLiteral(Expression[] elemExpr) throws ExpInvalidException {
        super("Bag", elemExpr);
        setResultType(TypeFactory.mkBag(inferElementType()));
    }

    /**
     * Evaluates expression and returns result value.
     */
    public Value eval(EvalContext ctx) {
        ctx.enter(this);
        Value res = new BagValue(((CollectionType) type()).elemType(), evalArgs(ctx));
        ctx.exit(this, res);
        return res;
    }
}
