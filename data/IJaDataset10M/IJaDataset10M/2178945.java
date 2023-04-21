package org.butu.utils.date;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.butu.utils.compare.CompareUtils;

public class Interval implements Serializable {

    private static final long serialVersionUID = 1L;

    private Calendar start;

    private Calendar end;

    public Interval() {
        this.start = new GregorianCalendar();
        DateUtils.clearTime(this.start);
        this.end = new GregorianCalendar();
        DateUtils.clearTime(this.end);
    }

    public Interval(Calendar start, Calendar end) {
        this.start = start;
        this.end = end;
    }

    public Calendar getStart() {
        return start;
    }

    public void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    public void setEnd(Calendar end) {
        this.end = end;
    }

    public boolean isComplete() {
        return start != null && end != null;
    }

    public boolean contains(Calendar date) {
        if (date == null) return false; else if (start != null && date.compareTo(start) < 0) return false; else if (end != null && date.compareTo(end) > 0) return false;
        return true;
    }

    public String toString() {
        return String.format("%1$td.%1$tm.%1$tY - %2$td.%2$tm.%2$tY", start, end);
    }

    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        } else if (obj instanceof Interval) {
            Interval other = (Interval) obj;
            return CompareUtils.nullsOrEquals(start, other.start) && CompareUtils.nullsOrEquals(end, other.end);
        }
        return false;
    }
}
