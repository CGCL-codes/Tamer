package org.openmi.standard;

/**
 * TimeSpan interface.
 */
public interface ITimeSpan extends ITime {

    /**
     * Time span's begin time stamp.
     *
     * @return beginnig time stamp
     */
    public ITimeStamp getStart();

    /**
     * Time span's end time stamp.
     *
     * @return endding time stamp
     */
    public ITimeStamp getEnd();
}
