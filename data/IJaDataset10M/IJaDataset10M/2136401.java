package uk.ac.liv.jt.codec;

public class ArithmeticProbabilityRange {

    long low;

    long high;

    long scale;

    public ArithmeticProbabilityRange(long low, long high, long scale) {
        super();
        this.low = low;
        this.high = high;
        this.scale = scale;
    }

    public long getLow() {
        return low;
    }

    public long getHigh() {
        return high;
    }

    public long getScale() {
        return scale;
    }
}
