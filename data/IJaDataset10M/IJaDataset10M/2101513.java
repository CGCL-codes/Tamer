package com.google.code.liquidform;

/**
 * Marker interface for clauses that are acceptable as subqueries. Not all query
 * parts can make up a subquery (<tt>ORDER BY</tt> is not valid)).
 * 
 * @param <T>
 *            the type that is being returned by the <tt>SELECT</tt> clause.
 *            Useful for type-checking <i>eg.</i> in
 *            {@link LiquidForm#in(Object, SubQuery) <tt>IN</tt> construct}
 */
public interface SubQuery<T> {
}
