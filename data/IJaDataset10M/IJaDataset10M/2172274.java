package org.plc.util;

/**
 * A wrapper class to pass object by reference to method.
 * @author pierreluc
 * @date 2010-09-15
 *
 */
public final class Wrapper<T> {

    private T value;

    public Wrapper(T value) {
        this.value = value;
    }

    /**
	 * @return the value
	 */
    public T getValue() {
        return value;
    }

    /**
	 * @param value the value to set
	 */
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int hashCode() {
        if (value == null) {
            return 0;
        }
        return value.hashCode();
    }
}
