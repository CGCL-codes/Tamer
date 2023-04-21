package org.omegat.convert.v20to21.data;

import java.io.Serializable;

/**
 * Options for Text filter.
 * Serializable to allow saving to / reading from configuration file.
 * <p>
 * Text filter would have the following options
 * ([+] means default on).
 * Segment text into paragraphs on:
 * <ul>
 * <li>[] Line breaks
 * <li>[+] Empty lines (double line break)
 * <li>[] Never
 * </ul>
 *
 *
 * @author Maxym Mykhalchuk
 */
public class TextOptions implements Serializable {

    /**
     * Text filter should segmentOn text into paragraphs on line breaks. 
     */
    public static final int SEGMENT_BREAKS = 1;

    /**
     * Defult. Text filter should segmentOn text into paragraphs on empty lines. 
     */
    public static final int SEGMENT_EMPTYLINES = 2;

    /**
     * Text filter should not segmentOn text into paragraphs. 
     */
    public static final int SEGMENT_NEVER = 3;

    /** Holds value of property. */
    private int segmentOn = SEGMENT_EMPTYLINES;

    /**
     * Returns when Text filter should segmentOn text into paragraphs.
     * @return One of {@link #SEGMENT_BREAKS}, {@link #SEGMENT_EMPTYLINES}, 
     *                  {@link #SEGMENT_NEVER}.
     */
    public int getSegmentOn() {
        return this.segmentOn;
    }

    /**
     * Sets when Text filter should segmentOn text into paragraphs.
     * @param segmentOn One of {@link #SEGMENT_BREAKS}, {@link #SEGMENT_EMPTYLINES},
     *                  {@link #SEGMENT_NEVER}.
     */
    public void setSegmentOn(int segmentOn) {
        this.segmentOn = segmentOn;
    }
}
