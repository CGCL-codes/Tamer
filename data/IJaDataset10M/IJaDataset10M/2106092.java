package com.trazere.util.lang;

import com.trazere.util.text.Describable;
import com.trazere.util.text.Description;
import com.trazere.util.text.TextUtils;

/**
 * The {@link MutableDouble} class represents mutable double values.
 */
public class MutableDouble implements Describable {

    /** The value. */
    protected double _value;

    /**
	 * Instantiates a new mutable double with the given value.
	 * 
	 * @param value The initial value.
	 */
    public MutableDouble(final double value) {
        _value = value;
    }

    /**
	 * Gets the value of the receiver mutable double.
	 * 
	 * @return The value.
	 */
    public double get() {
        return _value;
    }

    /**
	 * Sets the value of the receiver mutable boolean to the given value.
	 * 
	 * @param value The value.
	 * @return The given value.
	 */
    public double set(final double value) {
        _value = value;
        return value;
    }

    /**
	 * Negates the value of the receiver mutable double.
	 * 
	 * @return The resulting value.
	 */
    public double neg() {
        _value = -_value;
        return _value;
    }

    /**
	 * Adds the given value to the value of the receiver mutable double.
	 * 
	 * @param value The value
	 * @return The resulting value.
	 */
    public double add(final double value) {
        _value = _value + value;
        return _value;
    }

    /**
	 * Substracts the given value from the value of the receiver mutable double.
	 * 
	 * @param value The value
	 * @return The resulting value.
	 */
    public double sub(final double value) {
        _value = _value - value;
        return _value;
    }

    /**
	 * Multiplies the value of the receiver mutable double by the given value.
	 * 
	 * @param value The value
	 * @return The resulting value.
	 */
    public double mul(final double value) {
        _value = _value * value;
        return _value;
    }

    /**
	 * Divides the value of the receiver mutable double by the given value.
	 * 
	 * @param value The value
	 * @return The resulting value.
	 */
    public double div(final double value) {
        _value = _value / value;
        return _value;
    }

    @Override
    public final String toString() {
        return TextUtils.computeDescription(this);
    }

    public void fillDescription(final Description description) {
        description.append("Value", _value);
    }
}
