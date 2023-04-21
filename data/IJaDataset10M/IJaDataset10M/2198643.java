package com.rapidminer.tools.math.function.expressions;

import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.Add;
import org.nfunk.jep.function.Divide;
import org.nfunk.jep.function.PostfixMathCommand;

/**
 * Calculates the average of an arbitrary number of arguments.
 * 
 * @author Ingo Mierswa
 */
public class Average extends PostfixMathCommand {

    private Add addFun = new Add();

    private Divide divideFun = new Divide();

    public Average() {
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
        Object sum = stack.pop();
        if (!(sum instanceof Double)) {
            throw new ParseException("Invalid parameter type, only numbers are allowed for 'min'.");
        }
        int i = 1;
        while (i < curNumberOfParameters) {
            Object param = stack.pop();
            if (param instanceof Double) {
                sum = addFun.add(param, sum);
            } else {
                throw new ParseException("Invalid parameter type, only numbers are allowed for 'avg'.");
            }
            i++;
        }
        sum = divideFun.div(sum, curNumberOfParameters);
        stack.push(sum);
    }
}
