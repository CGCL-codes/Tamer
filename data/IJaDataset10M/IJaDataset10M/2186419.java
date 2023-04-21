package org.jsmg.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Variable implements ValueHolder {

    private String type;

    private String variableId;

    private Scope scope;

    /**
	 * Variable's value, if known.
	 */
    private String value;

    /** {@inheritDoc} */
    @Override
    public String getImage() {
        return getVariableId();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVariableId() {
        return variableId;
    }

    public void setVariableId(String variableId) {
        this.variableId = variableId;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isClassScope() {
        return Scope.CLASS_SCOPE.equals(getScope());
    }

    public boolean isValueKnown() {
        return value != null;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getType() + " " + getVariableId() + " " + getScope();
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Variable)) {
            return false;
        }
        final Variable o = (Variable) other;
        return new EqualsBuilder().append(getVariableId(), o.getVariableId()).append(getScope(), o.getScope()).isEquals();
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 7).append(getVariableId()).append(getScope()).toHashCode();
    }
}
