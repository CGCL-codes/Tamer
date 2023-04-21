package org.openmi.backbone;

import org.openmi.standard.ITimeSpan;
import org.openmi.standard.ITimeStamp;
import java.io.Serializable;

/**
 * The TimeSpan class defines a time span given a start and end time, which are
 * always specified as Modified Julian Day values.
 */
public class TimeSpan implements ITimeSpan, Serializable {

    private ITimeStamp start;

    private ITimeStamp end;

    /**
     * Creates an instance with the specified values.
     *
     * @param start Beginning time
     * @param end   Endding time
     */
    public TimeSpan(ITimeStamp start, ITimeStamp end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Creates an instance based on the specified time span.
     *
     * @param source The time span to copy values from
     */
    public TimeSpan(ITimeSpan source) {
        this.start = new TimeStamp(source.getStart());
        this.end = new TimeStamp(source.getEnd());
    }

    /**
     * Gets the start time for the time span.
     *
     * @return The start time as TimeStamp
     */
    public ITimeStamp getStart() {
        return start;
    }

    /**
     * Gets the end time for the time span.
     *
     * @return The end time as TimeStamp
     */
    public ITimeStamp getEnd() {
        return end;
    }

    /**
     * Sets the end time for the time span.
     *
     * @param end End time as TimeStamp
     */
    public void setEnd(ITimeStamp end) {
        this.end = end;
    }

    /**
     * Sets the start time for the time span.
     *
     * @param start Start time as TimeStamp
     */
    public void setStart(ITimeStamp start) {
        this.start = start;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.getClass() != obj.getClass()) return false;
        TimeSpan s = (TimeSpan) obj;
        if ((!getStart().equals(s.getStart())) || (!getEnd().equals(s.getEnd()))) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + getStart().hashCode() + getEnd().hashCode();
    }
}
