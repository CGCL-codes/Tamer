package org.sonify.vm.intermediate.execution;

import org.sonify.vm.intermediate.symbol.Result;
import org.sonify.vm.execution.ExpressionValue;

/**
 * Tries to convert a text value to an integer. If this operation fails,
 * an error condition is set.
 *
 * @author Andreas Stefik
 */
public class UnaryIntegerNumberCastStep extends UnaryOperationStep {

    @Override
    protected Result calculateOpcode(ExpressionValue value) {
        Result result = new Result();
        result.integer = (int) value.getResult().number;
        result.type = Result.INTEGER;
        return result;
    }
}
