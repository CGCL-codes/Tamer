package com.strategicgains.jbel.predicate;

import com.strategicgains.jbel.exception.EvaluationException;
import com.strategicgains.jbel.expression.Expression;

public class NotPredicate extends UnaryPredicate {

    public NotPredicate(Expression expression) {
        super(expression);
    }

    protected Object evaluateResults(Object object) throws EvaluationException {
        return (((Boolean) object).booleanValue() ? Boolean.FALSE : Boolean.TRUE);
    }
}
