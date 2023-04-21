package com.trazere.util.closure;

import com.trazere.util.text.Describable;
import com.trazere.util.text.Description;
import com.trazere.util.text.TextUtils;
import com.trazere.util.type.Maybe;

/**
 * The {@link ConstantClosure} class represents closures which evaluate to constant values.
 * 
 * @param <T> Type of the value.
 * @param <X> Type of the exceptions.
 */
public class ConstantClosure<T, X extends Exception> implements Closure<T, X>, Describable {

    /**
	 * Build a closure evaluating to the given value.
	 * 
	 * @param <T> Type of the value.
	 * @param <X> Type of the exceptions.
	 * @param value The value. May be <code>null</code>.
	 * @return The closure.
	 */
    public static <T, X extends Exception> Closure<T, X> build(final T value) {
        return new ConstantClosure<T, X>(value);
    }

    /** The value. May be <code>null</code>. */
    private final T _value;

    /**
	 * Instantiate a closure using the given value.
	 * 
	 * @param value The value. May be <code>null</code>.
	 */
    public ConstantClosure(final T value) {
        _value = value;
    }

    public T evaluate() {
        return _value;
    }

    public boolean isEvaluated() {
        return true;
    }

    public Maybe<T> asMaybe() {
        return Maybe.some(_value);
    }

    @Override
    public String toString() {
        return TextUtils.computeDescription(this);
    }

    public void fillDescription(final Description description) {
        description.append("Value", _value);
    }
}
