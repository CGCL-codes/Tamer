package org.op4j.operators.qualities;

import java.util.List;
import org.op4j.functions.IFunction;

/**
 * <p>
 * This interface contains methods for executing functions on operators with
 * list target objects on which a selection ("if") has already been done.
 * </p>
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface ExecutableListSelectedOperator<T> {

    /**
     * <p>
     * Executes the specified function on the target object, creating a new list operator
     * containing the result of the execution.
     * </p>
     * <p>
     * This function does not allow the operator target type to change because a selection ("if") has 
     * already been done on the target objects, and this would render the operator inconsistent
     * (some objects would belong to a type and others to another type).
     * </p>
     * 
     * @param function the function to be executed
     * @return an operator on the results of function execution
     */
    public ExecutableListSelectedOperator<T> execAsList(final IFunction<? super List<T>, ? extends List<? extends T>> function);

    /**
     * <p>
     * Executes the specified function on each of the elements, creating a new list operator
     * containing the result of all the executions.
     * </p>
     * <p>
     * This function does not allow the operator target type to change because a selection ("if") has 
     * already been done on the target objects, and this would render the operator inconsistent
     * (some objects would belong to a type and others to another type).
     * </p>
     * <p>
     * This method is equivalent to <tt>forEach().exec(function).endFor()</tt>.
     * </p>
     * 
     * @param function the function to be executed
     * @return an operator on the results of function execution on each element
     */
    public ExecutableListSelectedOperator<T> map(final IFunction<? super T, ? extends T> function);
}
