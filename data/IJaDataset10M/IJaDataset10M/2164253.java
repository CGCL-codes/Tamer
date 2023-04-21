package org.basegen.base.persistence.query;

import java.util.List;

/**
 * In expression
 */
public class InExpression implements Expression {

    /**
     * The left value (property's value).
     */
    private Object lValue;

    /**
     * The right value (property).
     */
    private String rValue;

    /**
     * The operation.
     */
    private String operation;

    /**
     * Default construtor
     */
    public InExpression() {
        this(null, null, null);
    }

    /**
     * @param newLValue The left value (property).
     * @param newOperator The operator.
     * @param newRValue The operation.
     */
    public InExpression(Object newLValue, String newOperator, String newRValue) {
        this.lValue = newLValue;
        this.operation = newOperator;
        this.rValue = newRValue;
    }

    /**
     * @return Returns the lValue.
     */
    public Object getLValue() {
        return lValue;
    }

    /**
     * @param value The lValue to set.
     */
    public void setLValue(Object value) {
        lValue = value;
    }

    /**
     * @return Returns the rValue.
     */
    public String getRValue() {
        return rValue;
    }

    /**
     * @param value The rValue to set.
     */
    public void setRValue(String value) {
        rValue = value;
    }

    /**
     * @return Returns the operation.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * @param newOperation The operation to set.
     */
    public void setOperation(String newOperation) {
        this.operation = newOperation;
    }

    /**
     * @see org.basegen.base.persistence.query.Expression#toHql()
     * @return Hql
     */
    public String toHql() {
        return "( ? " + getOperation() + " elements(" + getRValue() + ") )";
    }

    /**
     * @see org.basegen.base.persistence.query.Expression#getValues(java.util.List)
     * @param values values
     * @return the values
     */
    public List getValues(List values) {
        values.add(getLValue());
        return values;
    }
}
