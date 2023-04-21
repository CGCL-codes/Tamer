package lpp.citytrans.server.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class CTTime implements Comparable {

    private int h;

    private int m;

    private GregorianCalendar calendar = null;

    public CTTime(int ha, int mi) {
        this.h = (ha % 24);
        this.m = (mi % 60);
    }

    public int h() {
        return h % 24;
    }

    public int m() {
        return m % 60;
    }

    public GregorianCalendar calendarDate() {
        if (calendar == null) {
            calendar = (GregorianCalendar) GregorianCalendar.getInstance();
            calendar.setTimeZone(TimeZone.getTimeZone("CET"));
            calendar.set(Calendar.HOUR, h);
            calendar.set(Calendar.MINUTE, m);
        }
        return calendar;
    }

    public int compareTo(Object arg0) {
        CTTime other = (CTTime) arg0;
        if (h() < other.h()) {
            return -1;
        }
        if (h() == other.h()) {
            if (m() < other.m()) {
                return -1;
            } else if (m() == other.m()) {
                return 0;
            } else {
                return 1;
            }
        }
        return 1;
    }
}
