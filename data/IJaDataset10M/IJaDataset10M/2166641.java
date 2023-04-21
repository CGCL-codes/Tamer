package com.netx.generics.R1.time;

import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import com.netx.generics.R1.time.TimeValue.MEASURE;
import com.netx.generics.R1.util.Strings;
import com.netx.basic.R1.eh.Checker;
import com.netx.basic.R1.eh.IntegrityException;

public class Time extends FormattableImpl implements Comparable<Time> {

    private static final DateFormat _TIME_01 = new SimpleDateFormat("HH:mm:ss.SSS");

    private static final DateFormat _TIME_02 = new SimpleDateFormat("HH:mm:ss");

    private static final DateFormat _TIME_03 = new SimpleDateFormat("HH:mm");

    static {
        _TIME_01.setLenient(false);
        _TIME_02.setLenient(false);
        _TIME_03.setLenient(false);
    }

    public static Time parse(String s) {
        Checker.checkEmpty(s, "s");
        if (s.contains(".")) {
            _checkLength(s, 12);
            return new Time(s, _TIME_01);
        }
        if (Strings.countOccurrences(s, ':') == 2) {
            _checkLength(s, 8);
            return new Time(s, _TIME_02);
        } else {
            _checkLength(s, 5);
            return new Time(s, _TIME_03);
        }
    }

    private static void _checkLength(String s, int length) {
        if (s.length() > length) {
            throw new DateFormatException("unparseable time: '" + s + "'");
        }
    }

    private int _hours;

    private int _minutes;

    private int _seconds;

    private int _milliseconds;

    public Time() {
        this(Calendar.getInstance());
    }

    public Time(int hours, int minutes, int seconds, int milliseconds) {
        _setHours(hours);
        _setMinutes(minutes);
        _setSeconds(seconds);
        _setMilliseconds(milliseconds);
    }

    public Time(int hours, int minutes, int seconds) {
        this(hours, minutes, seconds, 0);
    }

    public Time(String s, DateFormat df) {
        Checker.checkEmpty(s, "s");
        Checker.checkNull(df, "df");
        try {
            Time t = new Timestamp(df.parse(s).getTime()).getTime();
            _setHours(t.getHours());
            _setMinutes(t.getMinutes());
            _setSeconds(t.getSeconds());
            _setMilliseconds(t.getMilliseconds());
        } catch (ParseException pe) {
            throw new DateFormatException("unparseable time: '" + s + "'");
        }
    }

    public Time(String s) {
        this(s, DateFormat.getTimeInstance());
    }

    Time(Calendar c) {
        _hours = c.get(Calendar.HOUR_OF_DAY);
        _minutes = c.get(Calendar.MINUTE);
        _seconds = c.get(Calendar.SECOND);
        _milliseconds = c.get(Calendar.MILLISECOND);
    }

    public int getHours() {
        return _hours;
    }

    public int getMinutes() {
        return _minutes;
    }

    public int getSeconds() {
        return _seconds;
    }

    public int getMilliseconds() {
        return _milliseconds;
    }

    public Time setHours(int hours) {
        return new Time(hours, getMinutes(), getSeconds(), getMilliseconds());
    }

    public Time setMinutes(int minutes) {
        return new Time(getHours(), minutes, getSeconds(), getMilliseconds());
    }

    public Time setSeconds(int seconds) {
        return new Time(getHours(), getMinutes(), seconds, getMilliseconds());
    }

    public Time setMilliseconds(int milliseconds) {
        return new Time(getHours(), getMinutes(), getSeconds(), milliseconds);
    }

    public String format(DateFormat df) {
        Checker.checkNull(df, "df");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(Calendar.HOUR_OF_DAY, _hours);
        cal.set(Calendar.MINUTE, _minutes);
        cal.set(Calendar.SECOND, _seconds);
        cal.set(Calendar.MILLISECOND, _milliseconds);
        return df.format(cal.getTime());
    }

    public String toString() {
        return format();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Time)) {
            return false;
        }
        return compareTo((Time) o) == 0;
    }

    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + _hours;
        hash = 31 * hash + _minutes;
        hash = 31 * hash + _seconds;
        hash = 31 * hash + _milliseconds;
        return hash;
    }

    public int compareTo(Time t) {
        Checker.checkNull(t, "t");
        long result = new TimeValue(this).getAs(MEASURE.MILLISECONDS) - new TimeValue(t).getAs(MEASURE.MILLISECONDS);
        if (result == 0) {
            return 0;
        }
        return result < 0 ? -1 : 1;
    }

    public boolean after(Time t) {
        Checker.checkNull(t, "t");
        return compareTo(t) > 0;
    }

    public boolean before(Time t) {
        Checker.checkNull(t, "t");
        return compareTo(t) < 0;
    }

    public Time clone() {
        try {
            return (Time) super.clone();
        } catch (CloneNotSupportedException cnse) {
            throw new IntegrityException(cnse);
        }
    }

    protected DateFormat getDefaultFormat() {
        return DateFormat.getTimeInstance();
    }

    public void _setHours(int hours) {
        if (hours < 0 || hours > 23) {
            throw new IllegalArgumentException("illegal hours: " + Strings.valueOf(hours, 2));
        }
        _hours = hours;
    }

    public void _setMinutes(int minutes) {
        if (minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("illegal minutes: " + Strings.valueOf(minutes, 2));
        }
        _minutes = minutes;
    }

    public void _setSeconds(int seconds) {
        if (seconds < 0 || seconds > 59) {
            throw new IllegalArgumentException("illegal seconds: " + Strings.valueOf(seconds, 2));
        }
        _seconds = seconds;
    }

    public void _setMilliseconds(int milliseconds) {
        if (milliseconds < 0 || milliseconds > 999) {
            throw new IllegalArgumentException("illegal milliseconds: " + Strings.valueOf(milliseconds, 3));
        }
        _milliseconds = milliseconds;
    }
}
