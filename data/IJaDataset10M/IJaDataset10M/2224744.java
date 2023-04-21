package hu.akarnokd.reactive4java;

/**
 * The function interface which takes one parameter and returns something.
 * @author akarnokd
 * @param <Return> the return type
 * @param <Param1> the first parameter
 */
public interface Func1<Return, Param1> {

    /**
	 * The method that gets invoked with a parameter.
	 * @param param1 the parameter value
	 * @return the return object
	 */
    Return invoke(Param1 param1);
}
