package com.peterhi;

public final class Uint24 extends Number implements Comparable<Uint24> {

    private static final long serialVersionUID = -4628629148034466571L;

    public static final int SIZE = 24;

    public static final int MIN_VALUE = 0x0;

    public static final int MAX_VALUE = 0xffffff;

    private final int value;

    public Uint24(int value) {
        if (value < MIN_VALUE || value > MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public boolean equals(Object theOther) {
        if (theOther instanceof Uint24) {
            Uint24 theOtherUnsignedInteger24 = (Uint24) theOther;
            return value == theOtherUnsignedInteger24.value;
        }
        return false;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public int compareTo(Uint24 theOther) {
        if (theOther == null) {
            return 1;
        }
        return value - theOther.value;
    }
}
