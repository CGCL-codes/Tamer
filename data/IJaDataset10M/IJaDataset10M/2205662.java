package net.sf.saxon.type;

import net.sf.saxon.expr.Expression;
import net.sf.saxon.expr.ExpressionVisitor;
import net.sf.saxon.expr.RoleLocator;
import net.sf.saxon.trans.XPathException;

/**
 * Higher-order functions in XQuery 1.1 introduce a third kind of Item, namely a Function Item.
 * This type is represented here by a placeholder interfaces. The implementation of this type
 * is found only in Saxon-EE
 */
public interface FunctionItemType extends ItemType {

    /**
     * Determine the relationship of one function item type to another
     * @return for example {@link TypeHierarchy#SUBSUMES}, {@link TypeHierarchy#SAME_TYPE}
     */
    public int relationship(FunctionItemType other, TypeHierarchy th);

    /**
     * Create an expression whose effect is to apply function coercion to coerce a function to this function type
     * @param exp the expression that delivers the supplied sequence of function items (the ones in need of coercion)
     * @param role information for use in diagnostics
     * @param visitor the expression visitor, supplies context information
     * @return the coerced function, a function that calls the original function after checking the parameters
     */
    public Expression makeFunctionSequenceCoercer(Expression exp, RoleLocator role, ExpressionVisitor visitor) throws XPathException;
}
