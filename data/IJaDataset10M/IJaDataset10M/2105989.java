package com.ibm.tuningfork.infra.stream.expression.operations;

import com.ibm.tuningfork.infra.stream.expression.base.BooleanExpression;
import com.ibm.tuningfork.infra.stream.expression.base.Expression;
import com.ibm.tuningfork.infra.stream.expression.base.MissingValueException;
import com.ibm.tuningfork.infra.stream.expression.base.StreamContext;

/**
 * Implement the boolean ! operation
 */
public class BooleanNot extends BooleanExpression {

    /** The expression to negate */
    private BooleanExpression toNegate;

    /**
     * Make a new BooleanNot
     * @param toNegate the expression to negate
     */
    public BooleanNot(BooleanExpression toNegate) {
        this.toNegate = toNegate;
        mergeContexts(toNegate);
    }

    public boolean getBooleanValue(StreamContext context) throws MissingValueException {
        return !toNegate.getBooleanValue(context);
    }

    public BooleanExpression resolve(Expression[] arguments, int depth) {
        BooleanExpression child = toNegate.resolve(arguments, depth);
        if (child != toNegate) {
            return new BooleanNot(child);
        }
        return this;
    }

    public String toString() {
        return "(!(" + toNegate + "))";
    }
}
