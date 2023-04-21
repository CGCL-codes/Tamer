package org.jmetis.kernel.closure;

/**
 * {@code IBinaryPredicate} is a boolean function (predicate) that accepts two
 * arguments returning a {@code boolean} value as result.
 * 
 * <p>
 * Example:
 * </p>
 * 
 * <pre>
 *   IBinaryPredicate&lt;Integer, Integer&gt; predicate = new IBinaryPredicate&lt;Integer, Integer&gt;(){
 *     public boolean evaluate(Integer firstArgument, Integer secondArgument){
 *       return firstArgument % secondArgument == 0
 *     }
 *   };
 * </pre>
 * 
 * @param <A1>
 *            the allowable types for the predicate's first argument.
 * @param <A2>
 *            the allowable types for the predicate's second argument.
 * 
 * @author era
 */
public interface IBinaryPredicate<A1, A2> {

    /**
	 * Evaluates the receiver against the given {@code argument}.
	 * 
	 * @param argument
	 *            the argument to the predicate
	 * @return the result of the evaluation
	 */
    boolean evaluate(A1 firstArgument, A2 secondArgument);
}
