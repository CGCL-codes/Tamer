package de.tudresden.inf.lat.jcel.core.datatype;

/**
 * This class models a visitor of <code>IntegerObjectPropertyExpression</code>.
 * 
 * @param <T>
 *            Type of the returning value of visit functions.
 * 
 * @author Julian Mendez
 * 
 * @see IntegerObjectPropertyExpression
 */
public interface IntegerObjectPropertyExpressionVisitor<T> {

    public T visit(IntegerInverseObjectProperty objectPropertyExpression);

    public T visit(IntegerObjectProperty objectPropertyExpression);
}
