package net.sourceforge.jeval.function.math;

import net.sourceforge.jeval.Evaluator;
import net.sourceforge.jeval.function.Function;
import net.sourceforge.jeval.function.FunctionConstants;
import net.sourceforge.jeval.function.FunctionException;
import net.sourceforge.jeval.function.FunctionResult;

/**
 * This class is a function that executes within Evaluator. The function returns
 * a random double value greater than or equal to 0.0 and less than 1.0. See the
 * Math.random() method in the JDK for a complete description of how this
 * function works.
 */
public class Random implements Function {

    /**
	 * Returns the name of the function - "random".
	 * 
	 * @return The name of this function class.
	 */
    public String getName() {
        return "random";
    }

    /**
	 * Executes the function for the specified argument. This method is called
	 * internally by Evaluator.
	 * 
	 * @param evaluator
	 *            An instance of Evaluator.
	 * @param arguments
	 *            Not used.
	 * 
	 * @return A random double value greater than or equal to 0.0 and less than
	 *         1.0.
	 * 
	 * @exception FunctionException
	 *                Thrown if the argument(s) are not valid for this function.
	 */
    public FunctionResult execute(final Evaluator evaluator, final String arguments) throws FunctionException {
        Double result = new Double(Math.random());
        return new FunctionResult(result.toString(), FunctionConstants.FUNCTION_RESULT_TYPE_NUMERIC);
    }
}
