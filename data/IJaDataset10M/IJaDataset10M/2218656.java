package com.lolcode.runtime.expression.math;

import com.lolcode.runtime.LOLRuntimeExpression;
import com.lolcode.runtime.expression.BinaryExpression;
import com.lolcode.types.LOLVariable;

/**
 * This represents a sum expression.
 * 
 * @author J. Suereth
 *
 */
public class SumExpression extends BinaryExpression implements LOLRuntimeExpression {

    /**
	 * Creates a new Sum Expression with the given lhs & rhs expressions.
	 * @param lhs
	 *          The lhs expression
	 * @param rhs
	 *          The rhs expression
	 */
    public SumExpression(LOLRuntimeExpression lhs, LOLRuntimeExpression rhs) {
        super(lhs, rhs);
    }

    /**
	 * @return
	 *       The sum of the lhs & rhs variables.
	 */
    public LOLVariable getValueImpl() {
        return getLOLRuntime().getALU().sumOf(lhs.getValue(), rhs.getValue());
    }
}
