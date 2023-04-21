package org.jquantlib.util.stdlibc;

@Deprecated
public class DoubleReference {

    private final double[] list;

    private final int index;

    public DoubleReference(final double[] list, final int index) {
        this.list = list;
        this.index = index;
    }

    public double getValue() {
        return list[index];
    }

    public void setValue(final double value) {
        list[index] = value;
    }
}
