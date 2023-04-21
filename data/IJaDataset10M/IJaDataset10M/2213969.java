package org.freelords.util;

public final class DualIncrement {

    private int indexMin;

    private int indexMax;

    private int valueMin;

    private int valueIncrement;

    private int index;

    private int value;

    public DualIncrement(int indexMin, int indexMax, int valueMin, int valueIncrement) {
        this.indexMin = indexMin;
        this.indexMax = indexMax;
        this.valueMin = valueMin;
        this.valueIncrement = valueIncrement;
        reset();
    }

    public void reset() {
        index = indexMin - 1;
        value = valueMin - valueIncrement;
    }

    public boolean next() {
        index++;
        value += valueIncrement;
        return index < indexMax;
    }

    public int getIndex() {
        return index;
    }

    public int getValue() {
        return value;
    }
}
