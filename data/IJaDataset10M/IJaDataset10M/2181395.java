package de.tudresden.inf.lat.jcel.core.datatype;

/**
 * This interface is implemented by classes that model object property
 * expressions with integer numbers.
 * 
 * @author Julian Mendez
 */
public interface IntegerObjectPropertyExpression extends IntegerDatatype {

    public <T> T accept(IntegerObjectPropertyExpressionVisitor<T> visitor);

    /**
	 * Returns the identifier of the used object property.
	 * 
	 * @return the identifier of the used object property
	 */
    public Integer getId();

    /**
	 * Tells whether or not this property expression contains only literals.
	 * 
	 * @return <code>true</code> if and only if this class expression contains
	 *         only literals
	 */
    public boolean hasOnlyLiterals();

    /**
	 * Tells whether or not this property expression is a literal.
	 * 
	 * @return <code>true</code> if and only if this class expression is a
	 *         literal
	 */
    public boolean isLiteral();
}
