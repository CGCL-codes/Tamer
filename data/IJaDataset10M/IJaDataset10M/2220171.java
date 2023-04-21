package org.jrcaf.rule.bool;

/**
 *  Interface for the expressions of a rule.
 */
public interface IExpression {

    /**
    * Evaluates the expression.
    * @return Returns the result of the evaluation as boolean
    */
    public boolean evaluate();

    /**
    * Removes the expression.
    */
    public void remove();
}
