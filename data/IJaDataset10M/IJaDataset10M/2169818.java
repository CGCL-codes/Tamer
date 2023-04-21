package org.datanucleus.jdo.query;

import org.datanucleus.query.expression.Expression;
import org.datanucleus.query.typesafe.ObjectExpression;
import org.datanucleus.query.typesafe.PersistableExpression;

/**
 * Implementation of an expression for all unsupported types.
 */
public class ObjectExpressionImpl<T> extends ExpressionImpl<T> implements ObjectExpression<T> {

    public ObjectExpressionImpl(PersistableExpression parent, String name) {
        super(parent, name);
    }

    public ObjectExpressionImpl(Class<T> cls, String name, ExpressionType type) {
        super(cls, name, type);
    }

    public ObjectExpressionImpl(Expression queryExpr) {
        super(queryExpr);
    }
}
