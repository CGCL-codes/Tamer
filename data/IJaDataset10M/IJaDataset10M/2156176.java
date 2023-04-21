package com.lolcode.runtime.expression;

import com.lolcode.runtime.LOLRuntimeExpression;
import com.lolcode.types.LOLVariable;

/**
 * This is an Expression that always returns a static value.
 * @author J. Suereth
 *
 */
public class StaticExpression extends AbstractExpression implements LOLRuntimeExpression {

    /**
	 * The static value we are set to.
	 */
    private LOLVariable staticValue;

    /**
	 * Creates a new StaticExpression that will always return a single value.
	 * 
	 * @param value
	 *             The value to return.
	 */
    public StaticExpression(LOLVariable value) {
        this.staticValue = value;
    }

    public LOLVariable getValueImpl() {
        return staticValue;
    }
}
