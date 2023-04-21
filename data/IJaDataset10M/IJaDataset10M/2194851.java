package org.modelingvalue.modelsync.elements;

/**
 * @author Wim Bast
 *
 */
public abstract class VariableBinding<D extends Variable> extends RuntimeElement<D> {

    private final D variable;

    private Object value;

    public VariableBinding(D variable, Object value) {
        this.variable = variable;
        this.setValue(value);
    }

    /**
	 * @return the variable
	 */
    public D getVariable() {
        return variable;
    }

    /**
	 * @return the value
	 */
    public Object getValue() {
        return value;
    }

    /**
	 * @param value the value to set
	 */
    @SuppressWarnings("boxing")
    public Boolean setValue(Object value) {
        Object oldValue = this.value;
        this.value = value;
        if (value == null) {
            return null;
        } else if (oldValue == null) {
            return Boolean.TRUE;
        } else {
            return !value.equals(oldValue);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + variable.hashCode();
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final VariableBinding<?> other = (VariableBinding<?>) obj;
        if (!variable.equals(other.variable)) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }

    @Override
    public D getDefinition() {
        return getVariable();
    }
}
