package org.nakedobjects.application.value;

import org.nakedobjects.application.Clock;

/**
 * Value object representing a date/time value marking a point in time This is a user facing date/time value,
 * more a marker used to indicate the temporal relationship between two objects.
 * 
 * @see DataTime
 */
public class TimeStamp extends Magnitude {

    private static Clock clock;

    public static void setClock(final Clock clock) {
        TimeStamp.clock = clock;
    }

    private final long time;

    /**
     * Create a TimeStamp object for storing a timeStamp set to the current time.
     */
    public TimeStamp() {
        time = clock.getTime();
    }

    public TimeStamp(long time) {
        this.time = time;
    }

    /**
     * returns true if the time stamp of this object has the same value as the specified timeStamp
     */
    public boolean isEqualTo(final Magnitude timeStamp) {
        if (timeStamp instanceof TimeStamp) {
            return this.time == ((TimeStamp) timeStamp).time;
        } else {
            throw new IllegalArgumentException("Parameter must be of type Time");
        }
    }

    /**
     * returns true if the timeStamp of this object is earlier than the specified timeStamp
     */
    public boolean isLessThan(final Magnitude timeStamp) {
        if (timeStamp instanceof TimeStamp) {
            return time < ((TimeStamp) timeStamp).time;
        } else {
            throw new IllegalArgumentException("Parameter must be of type Time");
        }
    }

    public long longValue() {
        return time;
    }

    public String toString() {
        return "Time Stamp " + longValue();
    }
}
