package org.op4j.operators.qualities;

import java.util.Set;
import org.javaruntype.type.Type;
import org.op4j.functions.IFunction;
import org.op4j.operators.intf.array.Level1ArrayElementsOperator;

/**
 * <p>
 * This interface contains methods for executing functions on operators with
 * set entry target objects.
 * </p>
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface ExecutableArrayOfSetElementsOperator<T> {

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
    public <X> ExecutableArrayOfSetElementsOperator<X> execAsSet(final IFunction<? extends Set<X>, ? super Set<T>> function);

    /**
     * <p>
     * Executes a function in a way equivalent to {@link #execAsSet(IFunction)} but only
     * on non-null elements, leaving null elements untouched.
     * </p>
     * 
     * @param <X> the type of the result elements
     * @param function the function to be executed
     * @return an operator on the results of function execution
     */
    public <X> ExecutableArrayOfSetElementsOperator<X> execIfNotNullAsSet(final IFunction<? extends Set<X>, ? super Set<T>> function);

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
    public <X> Level1ArrayElementsOperator<X> exec(final Type<X> resultType, final IFunction<X, ? super Set<T>> function);

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
    public <X> Level1ArrayElementsOperator<X> execIfNotNull(final Type<X> resultType, final IFunction<X, ? super Set<T>> function);

    /**
     * <p>
     * Executes the specified function on each of the elements, creating a new operator
     * containing the result of all the executions and setting the new operator type to the one
     * resulting from the function execution.
     * </p>
     * <p>
     * This method is equivalent to <tt>forEach().exec(function).endFor()</tt>.
     * </p>
     * 
     * @param <X> the type of the result elements
     * @param function the function to be executed
     * @return an operator on the results of function execution on each element
     */
    public <X> ExecutableArrayOfSetElementsOperator<X> map(final IFunction<X, ? super T> function);
}
