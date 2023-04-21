package org.basegen.base.persistence.query;

import java.util.List;

/**
 * Logical expression that generates the following HQL: <code> ( lValue operation ? ) </code>
 */
public class LogicalExpression implements Expression {

    /**
     * The expression's left side.
     */
    private Expression lExpression;

    /**
     * The expression's right side.
     */
    private Expression rExpression;

    /**
     * The operation.
     */
    private String operation;

    /**
     * Default construtor
     */
    public LogicalExpression() {
        this(null, null, null);
    }

    /**
     * @param newLExpression The expression's left side.
     * @param newOperator The operation.
     * @param newRExpression The expression's right side.
     */
    public LogicalExpression(Expression newLExpression, String newOperator, Expression newRExpression) {
        this.lExpression = newLExpression;
        this.operation = newOperator;
        this.rExpression = newRExpression;
    }

    /**
     * Returns the lExpression.
     * 
     * @return The lExpression.
     */
    public Expression getLExpression() {
        return lExpression;
    }

    /**
     * Set the lExpression.
     * @param newExpression new expression
     */
    public void setLExpression(Expression newExpression) {
        lExpression = newExpression;
    }

    /**
     * Returns the operation.
     * 
     * @return The operation.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Set the operation.
     * @param newOperation new operation
     */
    public void setOperation(String newOperation) {
        operation = newOperation;
    }

    /**
     * Returns the rExpression.
     * 
     * @return The rExpression.
     */
    public Expression getRExpression() {
        return rExpression;
    }

    /**
     * Set the rExpression.
     * @param newExpression new expression
     */
    public void setRExpression(Expression newExpression) {
        rExpression = newExpression;
    }

    /**
     * @see org.basegen.base.persistence.query.Expression#toHql()
     * @return Hql
     */
    public String toHql() {
        return "( " + getLExpression().toHql() + " " + getOperation() + " " + getRExpression().toHql() + " )";
    }

    /**
     * @see org.basegen.base.persistence.query.Expression#getValues(java.util.List)
     * @param values values
     * @return the values
     */
    public List getValues(List values) {
        getLExpression().getValues(values);
        getRExpression().getValues(values);
        return values;
    }
}
