package org.octaedr.upnp.cp.datatype;

import org.octaedr.upnp.cp.DataTypeInfo;

/**
 * Numerical data type info base class.
 */
abstract class NumericDataTypeInfo extends DataTypeInfo {

    /**
     * Constructs new info.
     * 
     * @param name
     *            Name of the data type.
     */
    protected NumericDataTypeInfo(final String name) {
        super(name);
    }

    public final boolean isValueListAllowed() {
        return false;
    }

    public final boolean isValueRangeAllowed() {
        return true;
    }

    protected abstract boolean checkValue(final Object value, final Object allowedRangeMinimum, final Object allowedRangeMaximum, final Object allowedRangeStep);

    public abstract boolean validateRange(final Object minimum, final Object maximum, final Object step);

    public String valueToStringLong(long value) {
        return Long.toString(value, 10);
    }

    protected boolean checkValueLong(final long value, final long allowedRangeMinimum, final long allowedRangeMaximum, final long allowedRangeStep) {
        if (value < allowedRangeMinimum) {
            return false;
        }
        if (value > allowedRangeMaximum) {
            return false;
        }
        return true;
    }
}
