package jhomenet.commons.responsive;

import jhomenet.commons.responsive.condition.ExecutorOperatorContext;
import jhomenet.commons.responsive.condition.Expression;

/**
 * The respnosive object converter is responsible for taking a particular
 * representation of sensor responsive objects and converting them
 * into appropriate objects for use with the sensor responsive
 * system.
 * <p>
 * This class is mostly used when retrieving an expression from a
 * persisted state.
 * 
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public interface ResponsiveConverter<T> {

    /**
	 * Convert an alternative expression representation into an <code>Expression</code>.
	 * 
	 * @param expressionEntity The alternate expression representation
	 * @return An equivalent expression object
	 * @throws ResponsiveConverterException
	 */
    public Expression toExpression(T expressionEntity) throws ResponsiveConverterException;

    /**
	 * Convert an <code>Expression</code> object into an alternate expression
	 * representation.
	 * 
	 * @param expression The expression object to convert
	 * @return An alternative expression representation
	 * @throws ResponsiveConverterException
	 */
    public T fromExpression(Expression expression) throws ResponsiveConverterException;
}
