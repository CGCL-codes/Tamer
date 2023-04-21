package org.nexopenframework.persistence.criterion;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @see org.nexopenframework.persistence.criterion.Expression
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class NotExpression implements Expression {

    private static final long serialVersionUID = 1L;

    private Expression original;

    /**
	 * 
	 */
    public NotExpression(final Expression expression) {
        this.original = expression;
    }

    /**
	 * @see org.nexopenframework.persistence.criterion.Expression#getPropertyName()
	 */
    public String getPropertyName() {
        return null;
    }

    /**
	 * 
	 * @see org.nexopenframework.persistence.criterion.Expression#getType()
	 */
    public int getType() {
        return UnaryLogicalExpressionType.NOT;
    }

    /**
	 * 
	 * @see org.nexopenframework.persistence.criterion.Expression#getValue()
	 */
    public Object getValue() {
        return null;
    }

    public Expression getExpression() {
        return original;
    }
}
