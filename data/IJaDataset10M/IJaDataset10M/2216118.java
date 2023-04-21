package org.sonify.vm.intermediate.execution;

import org.sonify.vm.intermediate.symbol.Result;
import org.sonify.vm.execution.ExpressionValue;

/**
 * Provides a greater than or equal to comparison.
 *
 * @author Andreas Stefik
 */
public class BinaryGreaterEqualsNumberIntegerStep extends BinaryOperationStep {

    @Override
    protected Result calculateOpcode(ExpressionValue left, ExpressionValue right) {
        Result result = new Result();
        result.boolean_value = left.getResult().number >= right.getResult().integer;
        result.type = Result.BOOLEAN;
        return result;
    }
}
