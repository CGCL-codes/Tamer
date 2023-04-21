package org.jvalue;

import org.jvalue.numbers.Range;

/**
 * A Restriction that ensures a given value falls into the value range specified by the restriction.
 */
public class RangeRestriction<T extends Comparable<T>> extends Restriction<T> {

    /**
	 * 
	 */
    protected Range<T> range;

    /**
	 * 
	 */
    public RangeRestriction(T lowerBound, T upperBound) {
        this(new Range<T>(lowerBound, upperBound));
    }

    /**
	 * 
	 */
    public RangeRestriction(Range<T> range) {
        this.range = range;
    }

    /**
	 * 
	 */
    @Override
    public boolean isSatisfiedBy(T value) {
        return range.includes(value);
    }
}
