package org.genxdm.xpath.v10;

import org.genxdm.Model;

/**
 *
 */
public interface VariantExpr {

    <N> Variant<N> evaluateAsVariant(Model<N> model, N contextNode, ExprContextDynamic<N> dynEnv) throws ExprException;
}
