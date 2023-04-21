package com.octane.util;

/**
 * Simple counter for use with clojure.
 * 
 * <pre>
 *  ctr (com.octane.util.Counter.)]
 * </pre>
 * 
 * @author Berlin
 *
 * @version $Revision: 1.0 $
 */
public final class Counter {

    private int value = 0;

    /**
     * Implementation Routine Counter.
     */
    public void Counter() {
        this.reset();
    }

    /**
     * Implementation Routine reset.
     */
    public void reset() {
        value = 0;
    }

    /**
     * Implementation Routine inc.
     */
    public void inc() {
        value++;
    }

    /**
     * Implementation Routine getValue.
     * @return int
     */
    public int getValue() {
        return value;
    }

    /**
     * Implementation Routine toString.
     * @return String
     */
    public String toString() {
        return "" + this.getValue();
    }
}
