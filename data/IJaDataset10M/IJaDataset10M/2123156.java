package org.datanucleus.api.jdo.query;

import org.datanucleus.query.expression.Expression;
import org.datanucleus.query.expression.InvokeExpression;
import org.datanucleus.query.typesafe.EnumExpression;
import org.datanucleus.query.typesafe.NumericExpression;
import org.datanucleus.query.typesafe.PersistableExpression;

/**
 * Implementation of an Enum expression.
 */
public class EnumExpressionImpl<T> extends ComparableExpressionImpl<Enum> implements EnumExpression<Enum> {

    public EnumExpressionImpl(PersistableExpression parent, String name) {
        super(parent, name);
    }

    public EnumExpressionImpl(Class<Enum> cls, String name, ExpressionType type) {
        super(cls, name, type);
    }

    public EnumExpressionImpl(Expression queryExpr) {
        super(queryExpr);
    }

    public NumericExpression ordinal() {
        org.datanucleus.query.expression.Expression invokeExpr = new InvokeExpression(queryExpr, "ordinal", null);
        return new NumericExpressionImpl<Integer>(invokeExpr);
    }
}
