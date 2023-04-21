package org.op4j.operators.qualities;

import org.javaruntype.type.Type;
import org.op4j.functions.IFunction;
import org.op4j.operators.intf.array.Level1ArrayElementsOperator;

/**
 * <p>
 * This interface contains methods for executing functions on operators with
 * array target objects as their second-level structures.
 * </p>
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface ExecutableArrayOfArrayElementsOperator<T> {

    /**
     * <p>
     * Executes the specified function on the target object, creating a new operator
     * containing the result of the execution.
     * </p>
     * <p>
     * This function must be able to take as input an object of the current operator's
     * target type, and will return an object of the same type and same structure.
     * </p>
     * 
     * @param function the function to be executed
     * @return an operator on the results of function execution
     */
    public ExecutableArrayOfArrayElementsOperator<T> execAsArray(final IFunction<? extends T[], ? super T[]> function);

    /**
     * <p>
     * Executes a function in a way equivalent to {@link #execAsArray(IFunction)} but only
     * on non-null elements, leaving null elements untouched.
     * </p>
     * 
     * @param function the function to be executed
     * @return an operator on the results of function execution
     */
    public ExecutableArrayOfArrayElementsOperator<T> execIfNotNullAsArray(final IFunction<? extends T[], ? super T[]> function);

    /**
     * <p>
     * Executes the specified function on the target object, creating a new operator
     * containing the result of the execution.
     * </p>
     * <p>
     * This function must be able to take as input an object of the current operator's
     * target type, and will return an object of a different type but same structure, 
     * which will be from then on the new operator's target type.
     * </p>
     * 
     * @param <X> the type of the result elements
     * @param function the function to be executed
     * @return an operator on the results of function execution
     */
    public <X> ExecutableArrayOfArrayElementsOperator<X> execAsArrayOf(final Type<X> type, final IFunction<X[], ? super T[]> function);

    /**
     * <p>
     * Executes a function in a way equivalent to {@link #execAsArrayOf(Type, IFunction)} but only
     * on non-null elements, leaving null elements untouched.
     * </p>
     * 
     * @param <X> the type of the result elements
     * @param function the function to be executed
     * @return an operator on the results of function execution
     */
    public <X> ExecutableArrayOfArrayElementsOperator<X> execIfNotNullAsArrayOf(final Type<X> type, final IFunction<X[], ? super T[]> function);

    /**
     * <p>
     * Executes the specified function on the target object, creating a new operator
     * containing the result of the execution and setting the new operator type to the one
     * specified.
     * </p>
     * 
     * @param <X> the type of the result object
     * @param resultType the new type for the operator
     * @param function the function to be executed
     * @return an operator on the results of function execution
     */
    public <X> Level1ArrayElementsOperator<X> exec(final Type<X> resultType, final IFunction<X, ? super T[]> function);

    /**
     * <p>
     * Executes a function in a way equivalent to {@link #exec(Type,IFunction)} but only
     * on non-null elements, leaving null elements untouched.
     * </p>
     *
     * @param <X> the type of the result object
     * @param resultType the new type for the operator
     * @param function the function to be executed
     * @return an operator on the results of function execution
     */
    public <X> Level1ArrayElementsOperator<X> execIfNotNull(final Type<X> resultType, final IFunction<X, ? super T[]> function);

    /**
     * <p>
     * Executes the specified function on each of the elements, creating a new operator
     * containing the result of all the executions and setting the new operator type to the one
     * specified.
     * </p>
     * <p>
     * This method is equivalent to <tt>forEach().exec(type, function).endFor()</tt>.
     * </p>
     * 
     * @param <X> the type of the result elements
     * @param type the new type for the operator
     * @param function the function to be executed
     * @return an operator on the results of function execution on each element
     */
    public <X> ExecutableArrayOfArrayElementsOperator<X> map(final Type<X> type, final IFunction<X, ? super T> function);

    /**
     * <p>
     * Executes the specified function on each of the elements, creating a new operator
     * containing the result of all the executions but not changing the operator type. The
     * specified function will have to return results compatible with the current operator type.
     * </p>
     * <p>
     * This method is equivalent to <tt>forEach().exec(function).endFor()</tt>.
     * </p>
     * 
     * @param function the function to be executed
     * @return an operator on the results of function execution on each element
     */
    public ExecutableArrayOfArrayElementsOperator<T> map(final IFunction<? extends T, ? super T> function);
}
