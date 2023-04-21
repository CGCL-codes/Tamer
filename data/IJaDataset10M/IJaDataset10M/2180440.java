package org.sqsh.variables;

import org.sqsh.CannotSetValueError;
import org.sqsh.Variable;

public class IntegerVariable extends Variable {

    private int value;

    private Integer minValue = null;

    private Integer maxValue = null;

    /**
     * Required for digester.
     */
    public IntegerVariable() {
    }

    /**
     * Creates an integer variable.
     * 
     * @param name The name of the variable.
     * @param value The value that it is to be set to.
     */
    public IntegerVariable(String name, int value) {
        super(name);
        this.value = value;
    }

    /**
     * Creates a exportable integer variable.
     * 
     * @param name The name of the variable.
     * @param value The value that it is to be set to.
     * @param isExported True if the variable is to be exported.
     */
    public IntegerVariable(String name, int value, boolean isExported) {
        super(name, isExported);
        this.value = value;
    }

    /**
     * @return the maxValue
     */
    public Integer getMaxValue() {
        return maxValue;
    }

    /**
     * @param maxValue the maxValue to set
     */
    public void setMaxValue(Integer maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * @return the minValue
     */
    public Integer getMinValue() {
        return minValue;
    }

    /**
     * @param minValue the minValue to set
     */
    public void setMinValue(Integer minValue) {
        this.minValue = minValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String setValue(String value) throws CannotSetValueError {
        int oldValue = this.value;
        try {
            int newValue = Integer.parseInt(value);
            if (minValue != null && newValue < minValue) {
                throw new CannotSetValueError("Value exceeds minimum possible " + "value (" + minValue + ")");
            }
            if (maxValue != null && newValue > maxValue) {
                throw new CannotSetValueError("Value exceeds maximum possible " + "value (" + maxValue + ")");
            }
            this.value = newValue;
        } catch (NumberFormatException e) {
            throw new CannotSetValueError("Invalid number format.");
        }
        return Integer.toString(oldValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
