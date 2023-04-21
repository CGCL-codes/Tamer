package org.sonify.vm.intermediate.execution;

import org.sonify.vm.intermediate.symbol.Result;
import org.sonify.vm.execution.ExpressionValue;

/**
 *
 * @author melissa
 */
public class UnaryBooleanBooleanCastStep extends UnaryOperationStep {

    @Override
    protected Result calculateOpcode(ExpressionValue value) {
        Result result = new Result();
        result.boolean_value = value.getResult().boolean_value;
        result.type = Result.BOOLEAN;
        return result;
    }
}
