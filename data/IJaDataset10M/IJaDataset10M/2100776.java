package de.matthiasmann.twl.model;

/**
 * A simple implementation of a property
 *
 * @param <T> the type of the property value
 *
 * @author Matthias Mann
 */
public class SimpleProperty<T> extends AbstractProperty<T> {

    private final Class<T> type;

    private final String name;

    private boolean readOnly;

    private T value;

    public SimpleProperty(Class<T> type, String name, T value) {
        this(type, name, value, false);
    }

    public SimpleProperty(Class<T> type, String name, T value, boolean readOnly) {
        this.type = type;
        this.name = name;
        this.readOnly = readOnly;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean canBeNull() {
        return false;
    }

    public T getPropertyValue() {
        return value;
    }

    /**
     * Changes the property value. It calls {@code valueChanged} to determine if
     * the value has really changed and if so updates the value and calls the callbacks.
     * 
     * @param value the new value for the property
     * @throws IllegalArgumentException is not thrown but part of the Property interface
     * @throws NullPointerException if value is null and canBeNull returned false
     * @see #canBeNull()
     * @see #valueChanged(java.lang.Object) 
     */
    public void setPropertyValue(T value) throws IllegalArgumentException {
        if (value == null && !canBeNull()) {
            throw new NullPointerException("value");
        }
        if (valueChanged(value)) {
            this.value = value;
            fireValueChangedCallback();
        }
    }

    public Class<T> getType() {
        return type;
    }

    /**
     * This method is used by setPropertyValue to check if the callback should be fired or not
     *
     * The default implementation calls equals on the current value.
     * 
     * @param newValue the new value passed to setPropertyValue
     * @return true if the value has changed and the callback should be fired
     */
    protected boolean valueChanged(T newValue) {
        return value != newValue && (value == null || !value.equals(newValue));
    }
}
