package org.scribble.conversation.model;

import org.scribble.model.Reference;

/**
 * This class represents a group of activities within
 * an interrupt specific 'escape' block of a
 * try/escape construct.
 * 
 */
public class InterruptBlock extends EscapeBlock {

    private static final long serialVersionUID = 3296525328792975708L;

    /**
	 * This method returns the optional expression.
	 * 
	 * @return The expression, or null if not defined
	 */
    @Reference(containment = true)
    public Expression getExpression() {
        return (m_expression);
    }

    /**
	 * This method sets the optional expression.
	 * 
	 * @param expr The expression
	 */
    public void setExpression(Expression expr) {
        if (m_expression != null) {
            m_expression.setParent(null);
        }
        m_expression = expr;
        if (m_expression != null) {
            m_expression.setParent(this);
        }
    }

    private Expression m_expression = null;
}
