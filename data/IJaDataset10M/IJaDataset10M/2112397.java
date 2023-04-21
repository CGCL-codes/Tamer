package net.sourceforge.plantuml.project;

class NumericNumber implements Numeric {

    private final int value;

    public NumericNumber(int v) {
        this.value = v;
    }

    public Numeric add(Numeric other) {
        if (other.getNumericType() != getNumericType()) {
            throw new IllegalArgumentException();
        }
        return new NumericNumber(value + ((NumericNumber) other).value);
    }

    public NumericType getNumericType() {
        return NumericType.NUMBER;
    }

    public int getIntValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Number:" + value;
    }

    public int compareTo(Numeric other) {
        final NumericNumber this2 = (NumericNumber) other;
        if (this2.value > value) {
            return -1;
        }
        if (this2.value < value) {
            return 1;
        }
        return 0;
    }
}
