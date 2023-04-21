package com.hifiremote.jp1;

/**
 * The Class HexInteger.
 */
public class HexInteger extends Number implements Comparable<HexInteger> {

    /**
   * Instantiates a new hex integer.
   * 
   * @param i the i
   */
    public HexInteger(int i) {
        value = new Integer(i);
    }

    /**
   * Instantiates a new hex integer.
   * 
   * @param text the text
   */
    public HexInteger(String text) {
        value = Integer.valueOf(text, 16);
    }

    public String toString() {
        return Integer.toString(value.intValue(), 16);
    }

    public byte byteValue() {
        return value.byteValue();
    }

    public double doubleValue() {
        return value.doubleValue();
    }

    public float floatValue() {
        return value.floatValue();
    }

    public int intValue() {
        return value.intValue();
    }

    public long longValue() {
        return value.longValue();
    }

    public short shortValue() {
        return value.shortValue();
    }

    public int compareTo(HexInteger o) {
        return value.compareTo(o.value);
    }

    /** The value. */
    private Integer value = null;
}
