package org.sonify.vm.intermediate.execution;

import org.sonify.vm.intermediate.symbol.Result;
import org.sonify.vm.execution.ExpressionValue;

/**
 * Provides a less than or equal to step for numbers.
 *
 * @author Andreas Stefik
 */
public class BinaryLessEqualsNumberStep extends BinaryOperationStep {

    @Override
    protected Result calculateOpcode(ExpressionValue left, ExpressionValue right) {
        Result result = new Result();
        result.boolean_value = left.getResult().number <= right.getResult().number;
        result.type = Result.BOOLEAN;
        return result;
    }
}
