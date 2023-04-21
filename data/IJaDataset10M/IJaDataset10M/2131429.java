package com.compress4j.arithmetic.ppm;

/**
 * king of bean for containing range info
 * @author b.pierre
 *
 */
public class RangeBean {

    private int start;

    private int end;

    private int max;

    public void set(int start, int end, int max) {
        this.start = start;
        this.end = end;
        this.max = max;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getMax() {
        return max;
    }

    public boolean worthcoding() {
        return end != max || start != 0;
    }
}
