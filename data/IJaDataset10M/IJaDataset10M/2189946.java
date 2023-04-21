package org.skycastle.util.time;

/**
 * A time that can be changed.
 *
 * @author Hans Haggstrom
 */
public interface MutableTime extends Time {

    /**
     *
     * @param time the time to copy.
     */
    void setTime(Time time);

    /**
     *
     * @param secondsSinceEpoch the seconds value.
     */
    void setSecondsSinceEpoch(double secondsSinceEpoch);

    /**
     * Adds a dureation to this time.
     *
     * @param duration_s seconds
     */
    void add(double duration_s);
}
