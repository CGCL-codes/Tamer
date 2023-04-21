package net.sf.lightbound.demos.calendar;

import java.io.Serializable;
import java.util.Calendar;

/**
 * @author Esa Tanskanen
 *
 */
public class DayMarker implements Serializable {

    private final Calendar day;

    private final DayMarkerType type;

    public DayMarker(int year, int month, int day, DayMarkerType type) {
        this.day = Calendar.getInstance();
        this.day.set(Calendar.YEAR, year);
        this.day.set(Calendar.MONTH, month - 1);
        this.day.set(Calendar.DATE, day);
        this.type = type;
    }

    public DayMarker(Calendar day, DayMarkerType type) {
        this.day = (Calendar) day.clone();
        this.type = type;
    }

    public Calendar getDay() {
        return day;
    }

    public DayMarkerType getType() {
        return type;
    }
}
