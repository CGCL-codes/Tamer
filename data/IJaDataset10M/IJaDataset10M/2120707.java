package swifu.main;

/** Is thrown if you add a function to a FunctionManager and the 
 *  FunctionManager can't fetch the Action assigned to the function.
 */
public class NoSuchFunctionActionException extends FunctionException {

    /** Name of the function */
    private String functionName;

    /**
	 * Creates a new NoSuchFunctionActionException.
	 * @param functionName name of the function where no Action could be
	 *                     found for
	 */
    public NoSuchFunctionActionException(String functionName) {
        this.functionName = functionName;
    }

    /** Returns the name of the function no Action could be found for.
	 * @return name of the function no Action could be found for
	 */
    public String getFunction() {
        return functionName;
    }
}
