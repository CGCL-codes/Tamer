package neembuu.common;

/**
 * A RangeArrayElement whose reference point (startingOffset)
 * has been shifted and also the properties haven been shifted accordingly.
 *
 * <br/>
 * Example :<br/>
 * <pre>
 * Iterator<RangeArrayElement> it = rangeArray.iteratorOver(new RangeArrayElement(startBound,endBound));
 * // todo
 * </pre>
 * @author Shashank Tulsyan
 */
public class ShiftedRangeArrayElement extends RangeArrayElement {

    public ShiftedRangeArrayElement(RangeArrayElement toShift, long newStart, long newEnd) {
        super(toShift);
        super.checkShiftRequest(newStart, newEnd);
        this.setStarting(newStart);
        this.setEnding(newEnd);
    }

    @Override
    public final boolean isShifted() {
        return true;
    }
}
