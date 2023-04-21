package de.bioutils.range;

/**
 * <p>
 * An implementation for {@link NewRangeFeature}.
 * </p>
 * <p>
 * Limitations: <blockquote> {@code start} must be > 0 </blockquote>
 * </p>
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-12-15
 * @see NewRangeFeature
 *@see AbstractRange
 */
public class OneBasedPositiveIntegerRange extends AbstractRange {

    public OneBasedPositiveIntegerRange(int start, int stop) {
        super(start, stop);
        if (start < 1) throw new IndexOutOfBoundsException("start point must be greater than -1");
    }

    public OneBasedPositiveIntegerRange(NewRangeFeature range) {
        super(range);
        if (getStart() < 1) throw new IndexOutOfBoundsException("start point must be greater than -1");
    }

    public ZeroBasedPositiveIntegerRange toZeroBased() {
        final NewRangeFeature r = getRange().shiftRange(-1);
        return new ZeroBasedPositiveIntegerRange(r);
    }

    @Override
    protected AbstractRange newInstance(int i, int j) {
        return new OneBasedPositiveIntegerRange(i, j);
    }
}
