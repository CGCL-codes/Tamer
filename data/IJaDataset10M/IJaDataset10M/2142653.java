package org.genxdm.processor.xpath.v10.expressions;

import org.genxdm.Model;
import org.genxdm.xpath.v10.ExprContextDynamic;

public final class NumberConstantExpr extends ConvertibleNumberExpr {

    private final double number;

    public NumberConstantExpr(final double number) {
        super();
        this.number = number;
    }

    public <N> double numberFunction(Model<N> model, final N contextNode, final ExprContextDynamic<N> dynEnv) {
        return number;
    }
}
