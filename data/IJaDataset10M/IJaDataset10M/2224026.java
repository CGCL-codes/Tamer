package org.sonify.vm.intermediate.execution;

import org.sonify.vm.intermediate.symbol.Result;
import org.sonify.vm.execution.ExpressionValue;

/**
 * Provides an greater than check for strings.
 *
 * @author Andreas Stefik
 */
public class BinaryGreaterThanStringStep extends BinaryOperationStep {

    @Override
    protected Result calculateOpcode(ExpressionValue left, ExpressionValue right) {
        Result result = new Result();
        result.boolean_value = left.getResult().text.compareTo(right.getResult().text) > 0;
        result.type = Result.BOOLEAN;
        return result;
    }
}
