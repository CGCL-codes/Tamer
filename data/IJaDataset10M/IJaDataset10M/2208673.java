package org.xteam.sled.model;

import org.xteam.sled.semantic.Range;

public class RangeConstraint<T> {

    protected T field;

    protected Range range;

    public RangeConstraint(T field, Range range) {
        this.field = field;
        this.range = range;
    }

    public T field() {
        return field;
    }

    public int low() {
        return range.low();
    }

    public int high() {
        return range.high();
    }

    public boolean isContradictory() {
        return range.low() >= range.high();
    }

    public RangeConstraint<T> intersection(RangeConstraint<T> other) {
        return new RangeConstraint<T>(field, new Range(Math.max(range.low(), other.range.low()), Math.min(range.high(), other.range.high())));
    }

    @Override
    public String toString() {
        return range.low() + "<=" + field + "<" + range.high();
    }
}
