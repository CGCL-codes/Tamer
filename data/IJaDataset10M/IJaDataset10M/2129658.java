package com.rapidminer.tools.math.function.expressions;

import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 * Calculates the maximum of an arbitrary number of arguments.
 * 
 * @author Ingo Mierswa
 */
public class Maximum extends PostfixMathCommand {

    public Maximum() {
        numberOfParameters = -1;
    }

    /**
	 * Calculates the result of summing up all parameters, which are assumed to
	 * be of the Double type.
	 */
    @Override
    @SuppressWarnings("unchecked")
    public void run(Stack stack) throws ParseException {
        checkStack(stack);
        Object first = stack.pop();
        if (!(first instanceof Double)) {
            throw new ParseException("Invalid parameter type, only numbers are allowed for 'min'.");
        }
        Double currentMin = (Double) first;
        int i = 1;
        while (i < curNumberOfParameters) {
            Object param = stack.pop();
            if (param instanceof Double) {
                Double currentValue = (Double) param;
                currentMin = Math.max(currentMin, currentValue);
            } else {
                throw new ParseException("Invalid parameter type, only numbers are allowed for 'avg'.");
            }
            i++;
        }
        stack.push(currentMin);
    }
}
