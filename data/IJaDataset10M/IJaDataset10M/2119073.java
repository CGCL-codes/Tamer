package net.sf.jpasecurity.util;

/**
 * A simple object to hold values. May be used to emulate out parameters for method-calls.
 * @author Arne Limburg
 */
public class ValueHolder<T> {

    private T value;

    public ValueHolder() {
    }

    public ValueHolder(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
