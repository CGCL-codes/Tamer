package org.forzaframework.core.persistance;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author cesarreyes
 *         Date: 29-jul-2008
 *         Time: 12:50:11
 */
public class SimpleExpression extends Criterion {

    private Object value;

    private String valueType;

    private Boolean ignoreCase = false;

    private String operator;

    public SimpleExpression() {
    }

    protected SimpleExpression(String property, Object value, String operator) {
        this.property = property;
        this.value = value;
        this.operator = operator;
    }

    protected SimpleExpression(String property, Object value, String operator, Boolean ignoreCase) {
        this.property = property;
        this.value = value;
        this.ignoreCase = ignoreCase;
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Boolean getIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(Boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public SimpleExpression ignoreCase() {
        ignoreCase = true;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("value", value).append("valueType", valueType).append("ignoreCase", ignoreCase).append("operator", operator).toString();
    }
}
