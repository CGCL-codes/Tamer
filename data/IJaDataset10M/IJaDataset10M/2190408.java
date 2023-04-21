package org.op4j.operators.qualities;

import java.util.Map;
import org.javaruntype.type.Type;
import org.op4j.functions.IFunction;
import org.op4j.operators.intf.array.Level1ArrayElementsOperator;

/**
 * <p>
 * This interface contains methods for executing functions on operators with
 * map target objects.
 * </p>
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public interface ExecutableArrayOfMapElementsOperator<K, V> {

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
     * @param <X> the type of the resulting keys
     * @param <Y> the type of the resulting values
     * @param function the function to be executed
     * @return an operator on the results of function execution
     */
    public <X, Y> ExecutableArrayOfMapElementsOperator<X, Y> execAsMap(final IFunction<? extends Map<X, Y>, ? super Map<K, V>> function);

    /**
     * <p>
     * Executes a function in a way equivalent to {@link #execAsMap(IFunction)} but only
     * on non-null elements, leaving null elements untouched.
     * </p>
     * 
     * @param <X> the type of the resulting keys
     * @param <Y> the type of the resulting values
     * @param function the function to be executed
     * @return an operator on the results of function execution
     */
    public <X, Y> ExecutableArrayOfMapElementsOperator<X, Y> execIfNotNullAsMap(final IFunction<? extends Map<X, Y>, ? super Map<K, V>> function);

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
    public <X> Level1ArrayElementsOperator<X> exec(final Type<X> resultType, final IFunction<X, ? super Map<K, V>> function);

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
    public <X> Level1ArrayElementsOperator<X> execIfNotNull(final Type<X> resultType, final IFunction<X, ? super Map<K, V>> function);
}
