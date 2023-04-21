package org.genxdm.processor.xpath.v10.expressions;

import org.genxdm.Model;
import org.genxdm.xpath.v10.ExprContextDynamic;
import org.genxdm.xpath.v10.ExprException;
import org.genxdm.xpath.v10.NumberExpr;

/**
 * a compiled XPath expression (component) that represents the addition of two sub-expressions
 */
final class AddExpr extends ConvertibleNumberExpr {

    private final NumberExpr expr1;

    private final NumberExpr expr2;

    /**
	 * construct with two NumberExpr
	 */
    AddExpr(final NumberExpr expr1, final NumberExpr expr2) {
        super();
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    /**
	 * evaluate to result of a double
	 */
    public <N> double numberFunction(Model<N> model, final N contextNode, final ExprContextDynamic<N> dynEnv) throws ExprException {
        return expr1.numberFunction(model, contextNode, dynEnv) + expr2.numberFunction(model, contextNode, dynEnv);
    }
}
