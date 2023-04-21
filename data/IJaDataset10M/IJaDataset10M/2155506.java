package at.jku.xlwrap.map.transf;

import at.jku.xlwrap.common.XLWrapException;

/**
 * @author dorgon
 *
 */
public abstract class Shift extends TransformationBase {

    /** the shift factor */
    protected final int shift;

    /** times to repeat including initial state (no shift) */
    protected final int repeat;

    /**
	 * @param shift
	 * @param repeat
	 * @param restriction
	 * @param skipCondition
	 * @param breakCondition
	 * @throws XLWrapException
	 */
    public Shift(int shift, int repeat, String restriction, String skipCondition, String breakCondition) throws XLWrapException {
        super(restriction, skipCondition, breakCondition);
        this.shift = shift;
        this.repeat = repeat;
    }

    /**
	 * @return the repeat
	 */
    public int getRepeat() {
        return repeat;
    }

    /**
	 * @return the shift
	 */
    public int getShift() {
        return shift;
    }
}
