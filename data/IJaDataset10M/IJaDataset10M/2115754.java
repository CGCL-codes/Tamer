package com.strategicgains.jbel.function.math;

import com.strategicgains.jbel.exception.FunctionException;
import com.strategicgains.jbel.function.UnaryFunction;

public class AbsFunction extends MathFunction implements UnaryFunction {

    public Object perform(Object argument) throws FunctionException {
        return (getBigDecimalValue(argument).abs());
    }
}
