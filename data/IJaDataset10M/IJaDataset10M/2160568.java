package com.generalynx.ecos.utils;

public abstract class MuNumber extends Number implements Comparable, Cloneable, IMutable {

    /**
     * Returns the value of the specified number as a <code>byte</code>.
     * This may involve rounding or truncation.
     *
     * @return The numeric value represented by this object after conversion
     *         to type <code>byte</code>.
     */
    public byte byteValue() {
        return (byte) longValue();
    }

    /**
     * Returns the value of the specified number as a <code>short</code>.
     * This may involve rounding or truncation.
     *
     * @return The numeric value represented by this object after conversion
     *         to type <code>short</code>.
     */
    public short shortValue() {
        return (short) longValue();
    }

    /**
     * Returns the value of the specified number as a <code>int</code>.
     * This may involve rounding or truncation.
     *
     * @return The numeric value represented by this object after conversion
     *         to type <code>int</code>.
     */
    public int intValue() {
        return (int) longValue();
    }

    /**
     * Returns the value of the specified number as a <code>float</code>.
     * This may involve rounding or truncation.
     *
     * @return The numeric value represented by this object after conversion
     *         to type <code>float</code>.
     */
    public float floatValue() {
        return (float) doubleValue();
    }
}
