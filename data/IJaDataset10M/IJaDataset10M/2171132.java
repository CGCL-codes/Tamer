package com.abb.oql;

import com.abb.collections.Set;

/** describes an expression that occurres in a metrics query. Expressions
    are hierarchically structured, reflecting the expression's tree structure.
    Each type of expression can be evaluated in a certain context, most gerenally
    that of a traversed object.
  */
public interface Expression {

    /** evaluate the expression at hand in the given context

        @param context the object in whose context to evaluate this expression
        @param t the traversal in whose context this expression is evaluated
        @return the expression value as evaluated in the given context
      */
    public Object evaluate(Object context, Traversal t);

    /** determins the set of all variables that are used in the shape
	of variable expressions (e.g. <tt>%(x)</tt>). This is done
	recursively over all subtraversals, subexpressions and conditions.
	
	@return the set of variable names, denoting all variables used
		by this expression (recursively). Contained objects are of
		type <tt>String</tt>. The returned <tt>Set</tt> is always
		valid but may of course be empty.
      */
    public Set getAllUsedVariablesRecursively();
}
