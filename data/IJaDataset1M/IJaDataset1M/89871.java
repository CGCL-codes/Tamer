package org.sonify.vm.intermediate.execution;

import org.sonify.vm.intermediate.symbol.Result;
import org.sonify.vm.execution.ExpressionValue;

/**
 * Provides a comparison operator for objects.
 *
 * @author Andreas Stefik
 */
public class BinaryEqualsCustomCustomStep extends BinaryOperationStep {

    @Override
    protected Result calculateOpcode(ExpressionValue left, ExpressionValue right) {
        Result result = new Result();
        if (!left.isNull() && !right.isNull()) {
            result.boolean_value = left.getObjectHash() == right.getObjectHash();
        }
        result.type = Result.BOOLEAN;
        return result;
    }
}
