package de.gstpl.data;

import de.gstpl.resource.language.L;
import java.util.BitSet;

/**
 * This class is the timeatom (indivisable). Each task which occupies space
 * in timetable has at least one TimeInterval. For several time intervals use
 * Timetable instead!<br>
 * Use GDB to create a new TimeInterval!
 * And use TimeInterval2() constructor to temporarly create a time interval.
 *
 * @author Peter Karich
 */
public class TimeInterval extends TimeableImpl implements Comparable {

    protected static final String DURATION_TIME_PROPERTY = "durationTime";

    protected static final String START_TIME_PROPERTY = "startTime";

    protected static final String ROOM_PROPERTY = "room";

    protected static final String SUBJECT_PROPERTY = "subject";

    public TimeInterval() {
        super();
        setStartTime(-1);
        setDuration(-1);
    }

    public void setRoom(Room room, boolean reverse) {
        setToOneTarget(ROOM_PROPERTY, room, reverse);
    }

    public void setSubject(Subject subject, boolean reverse) {
        setToOneTarget(SUBJECT_PROPERTY, subject, reverse);
    }

    /**
     * This method returns the associated room.
     */
    public Room getRoom() {
        return (Room) readProperty(ROOM_PROPERTY);
    }

    /**
     * This method returns the associated subject.
     */
    public Subject getSubject() {
        return (Subject) readProperty(SUBJECT_PROPERTY);
    }

    public void setStartTime(int absStartTime) {
        writeProperty(START_TIME_PROPERTY, new Integer(absStartTime));
        calculateAgain = true;
    }

    /**
     * This method returns the absolute start time.
     * @return absolute start time; unit: minimalTimeInterval
     */
    public int getStartTime() {
        return ((Integer) readProperty(START_TIME_PROPERTY)).intValue();
    }

    public void setDuration(int duration) {
        if (TimeFormat.exceedsDay(getStartTime(), duration)) throw new UnsupportedOperationException(L.tr("Can't_create_TimeInterval:_") + L.tr("duration_exceeds_day_end!_Start:") + getStartTime() + L.tr("_Duration:") + duration);
        writeProperty(DURATION_TIME_PROPERTY, new Integer(duration));
        calculateAgain = true;
    }

    /**
     * This method returns the duration of this TimeInterval
     *
     * @return the duration of this TimeInterval in minTimeInterval
     */
    public int getDuration() {
        return ((Integer) readProperty(DURATION_TIME_PROPERTY)).intValue();
    }

    /**
     * This method returns the 'natural' start day.
     * @return 0 until dayNo-1
     */
    public int getRelStartDay() {
        int dayDuration = DBProperties.get().getDayDuration();
        return getStartTime() / dayDuration;
    }

    /**
     * This method returns the time relative to the absStartTime day
     *
     * @param return a String in the format HH:MM where H means hour and M means minute.
     */
    public int getRelStartTime() {
        int dayDuration = DBProperties.get().getDayDuration();
        return getStartTime() - getRelStartDay() * dayDuration;
    }

    /**
     * This method returns the time relative to the absStartTime day
     *
     * @param return a String in the format HH:MM where H means hour and M means minute.
     */
    public String getRelStartTimeAsString() {
        return TimeFormat.getRelStartTimeAsString(getStartTime(), getRelStartDay());
    }

    public BitSet getBitSet(int week) {
        BitSet bsTmp = super.getBitSet(week);
        if (calculateAgain) {
            bsTmp.set(getStartTime(), getStartTime() + getDuration());
            calculateAgain = false;
        }
        return bsTmp;
    }

    public String toString() {
        return L.tr("TimeInterval[") + getRelStartTimeAsString() + "  (" + (getDuration() * DBProperties.get().getMinTimeInterval()) + L.tr("_min),_Day:") + getRelStartDay() + ((getSubject() != null) ? L.tr(",_Subject:") + getSubject() : "") + ((getRoom() != null) ? L.tr(",_Room:") + getRoom() : "") + "]";
    }

    public String toHTMLString() {
        return "<html>" + getRelStartTimeAsString() + "&nbsp;&nbsp;(" + getDuration() * DBProperties.get().getMinTimeInterval() + L.tr("_min)<br>") + ((getSubject() != null) ? "&nbsp;&nbsp;" + getSubject() + "<br>" : "") + ((getRoom() != null) ? L.tr("&nbsp;&nbsp;Room:_") + getRoom() : "") + "</html>";
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof TimeInterval)) return false;
        if (this == obj) return true;
        TimeInterval ti = (TimeInterval) obj;
        if (ti.getStartTime() == this.getStartTime() && ti.getDuration() == getDuration()) {
            if (ti.getSubject() != null && !ti.getSubject().equals(getSubject())) return false;
            if (ti.getRoom() != null && !ti.getRoom().equals(getRoom())) return false;
            return true;
        }
        return false;
    }

    /**
     * This method allows comparing timeIntervals smaller absStartTime will cause
     * a smaller index in a collection.
     *
     * @return -1 if this.absStartTime < o.absStartTime OR (this.absStartTime==o.absStartTime AND this.duration < o.duration)<br>
     * @return 1 if this.absStartTime > o.absStartTime OR (this.absStartTime==o.absStartTime AND this.duration > o.duration)<br>
     * @return 0 if this.absStartTime == o.absStartTime AND if this.duration == o.duration<br>
     */
    public int compareTo(Object o) {
        TimeInterval ti = (TimeInterval) o;
        if (getStartTime() == ti.getStartTime()) {
            if (getDuration() == ti.getDuration()) {
                int ret = 0;
                if (getRoom() != null && ti.getRoom() != null) ret = getRoom().compareTo(ti.getRoom());
                if (ret == 0) {
                    if (getSubject() != null && ti.getSubject() != null) return getSubject().compareTo(ti.getSubject()); else return 0;
                } else return ret;
            } else if (getDuration() > ti.getDuration()) return -1; else return 1;
        } else if (getStartTime() < ti.getStartTime()) return -1; else return 1;
    }

    public int hashCode() {
        return getStartTime() * 37 + 13 * getDuration() + (getRoom() == null ? 0 : getRoom().hashCode()) + (getSubject() == null ? 0 : getSubject().hashCode());
    }
}
