package org.sonify.vm.intermediate.execution;

import org.sonify.vm.intermediate.symbol.Result;
import org.sonify.vm.execution.ExpressionValue;

/**
 *
 * @author Melissa Stefik
 */
public class UnaryIntegerBooleanCastStep extends UnaryOperationStep {

    @Override
    protected Result calculateOpcode(ExpressionValue value) {
        Result result = new Result();
        if (value.getResult().boolean_value) result.integer = 1; else result.integer = 0;
        result.type = Result.INTEGER;
        return result;
    }
}
