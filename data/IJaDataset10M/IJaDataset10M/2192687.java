package edu.cmu.sphinx.frontend;

/**
 * A signal that indicates the start of data.
 *
 * @see Data
 * @see DataProcessor
 * @see Signal
 */
public class DataStartSignal extends Signal {

    /**
     * Constructs a DataStartSignal.
     */
    public DataStartSignal() {
        this(System.currentTimeMillis());
    }

    /**
     * Constructs a DataStartSignal at the given time.
     *
     * @param time the time this DataStartSignal is created
     */
    public DataStartSignal(long time) {
        super(time);
    }

    /**
     * Returns the string "DataStartSignal".
     *
     * @return the string "DataStartSignal"
     */
    public String toString() {
        return "DataStartSignal: creation time: " + getTime();
    }
}
