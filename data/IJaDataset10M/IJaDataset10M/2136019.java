package net.sf.kerner.commons.range.impl;

import net.sf.kerner.commons.Transformer;
import net.sf.kerner.commons.range.LongRange;
import net.sf.kerner.commons.range.LongRangeFactory;
import net.sf.kerner.commons.range.IntegerRange;

public class IntegerRangeToLongRangeTransformator implements Transformer<IntegerRange, LongRange> {

    private final LongRangeFactory factory;

    public IntegerRangeToLongRangeTransformator(LongRangeFactory factory) {
        this.factory = factory;
    }

    public LongRange transform(IntegerRange element) {
        return factory.create(element.getStart(), element.getStop());
    }
}
