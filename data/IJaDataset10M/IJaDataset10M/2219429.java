package org.inxar.jenesis;

/**
 * <code>Statement</code> subinterface for the <code>return</code>
 * keyword.  Depending on the context of the method, a return
 * statement can return with the assigned expression.  
 */
public interface Return extends Statement {

    /**
     * Gets the expression for this return statement.
     */
    Expression getExpression();

    /**
     * Sets the expression for this return statement.
     */
    void setExpression(Expression expr);
}
