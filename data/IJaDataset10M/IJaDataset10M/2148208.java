package org.matsim.facilities;

import org.matsim.gbl.Gbl;
import org.matsim.utils.misc.Time;

public class Opentime implements Comparable<Opentime> {

    private final String day;

    private double startTime;

    private double endTime;

    public Opentime(final String day, final String start_time, final String end_time) {
        this.day = day;
        this.startTime = Time.parseTime(start_time);
        this.endTime = Time.parseTime(end_time);
        this.acceptTimes();
    }

    public int compareTo(Opentime other) {
        if (this.startTime > other.endTime) {
            return -6;
        } else if (this.startTime == other.endTime) {
            return -5;
        } else if (this.startTime > other.startTime) {
            if (this.endTime > other.endTime) {
                return -4;
            } else if (this.endTime == other.endTime) {
                return -3;
            } else {
                return -2;
            }
        } else if (this.startTime == other.startTime) {
            if (this.endTime > other.endTime) {
                return -1;
            } else if (this.endTime == other.endTime) {
                return 0;
            } else {
                return 3;
            }
        } else if (this.endTime > other.endTime) {
            return 2;
        } else if (this.endTime == other.endTime) {
            return 1;
        } else if (this.endTime > other.startTime) {
            return 4;
        } else if (this.endTime == other.startTime) {
            return 5;
        } else {
            return 6;
        }
    }

    @Override
    public final boolean equals(final Object o) {
        if (o instanceof Opentime) {
            Opentime other = (Opentime) o;
            if (other.day.equals(this.day) && (other.startTime == this.startTime) && (other.endTime == this.endTime)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return (this.day.hashCode() + Double.valueOf(this.startTime).hashCode() + Double.valueOf(this.endTime).hashCode());
    }

    private final void acceptTimes() {
        if (this.startTime >= this.endTime) {
            Gbl.errorMsg(this + "[startTime=" + this.startTime + " >= endTime=" + this.endTime + " not allowed]");
        }
    }

    protected final void setStartTime(final double start_time) {
        this.startTime = start_time;
        this.acceptTimes();
    }

    protected final void setEndTime(final double end_time) {
        this.endTime = end_time;
        this.acceptTimes();
    }

    public final String getDay() {
        return this.day;
    }

    public final double getStartTime() {
        return this.startTime;
    }

    public final double getEndTime() {
        return this.endTime;
    }

    @Override
    public final String toString() {
        return "[day=" + this.day + "]" + "[startTime=" + this.startTime + "]" + "[endTime=" + this.endTime + "]";
    }
}
