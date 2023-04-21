package music;

/**
 *
 * @author martin
 */
public class IntervalSequence {

    /**
   * A copy of the scale data from Scale class.
   */
    private final short data;

    /**
   * This is value contained in data, with the lowest 12 bits right-rotated
   * (with wrapping), but there is always a 1 in the lowest bit.
   */
    private short state;

    IntervalSequence(short data) {
        this.data = data;
        this.state = data;
    }

    public int getNext() {
        int nextInterval = 0;
        do nextInterval++; while (((state >>= 1) & 1) == 0);
        state |= 1 << 12;
        return nextInterval;
    }

    public void reset() {
        state = data;
    }

    public IntervalSequence clone() {
        IntervalSequence is = new IntervalSequence(data);
        is.state = state;
        return is;
    }
}
