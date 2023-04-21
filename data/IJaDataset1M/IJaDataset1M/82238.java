package com.lolcode.runtime.expression.math;

import com.lolcode.runtime.LOLRuntimeExpression;
import com.lolcode.runtime.expression.BinaryExpression;
import com.lolcode.types.LOLVariable;

/**
 * Expression to calculate the product of two expressions.
 * @author J. Suereth
 *
 */
public class ProductExpression extends BinaryExpression {

    public ProductExpression(LOLRuntimeExpression lhs, LOLRuntimeExpression rhs) {
        super(lhs, rhs);
    }

    public LOLVariable getValueImpl() {
        return getLOLRuntime().getALU().productOf(lhs.getValue(), rhs.getValue());
    }
}
