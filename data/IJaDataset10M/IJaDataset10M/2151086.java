package org.genxdm.processor.xpath.v10.functions;

import org.genxdm.processor.xpath.v10.expressions.ConvertibleExprImpl;
import org.genxdm.xpath.v10.ExprContextStatic;
import org.genxdm.xpath.v10.ExprParseException;
import org.genxdm.xpath.v10.extend.Function;
import org.genxdm.xpath.v10.extend.ConvertibleExpr;

abstract class Function2 implements Function {

    abstract ConvertibleExprImpl makeCallExpr(ConvertibleExpr e1, ConvertibleExpr e2, ExprContextStatic statEnv) throws ExprParseException;

    public ConvertibleExpr makeCallExpr(final ConvertibleExpr[] e, final ExprContextStatic statEnv) throws ExprParseException {
        if (e.length != 2) {
            throw new ExprParseException("expected two arguments");
        }
        return makeCallExpr(e[0], e[1], statEnv);
    }
}
