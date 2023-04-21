package it.dangelo.saj.validation.json_vl.date;

import java.util.TimeZone;

public class Time implements Comparable<Time> {

    private static final TimeZone TIME_ZONE_GMT = TimeZone.getTimeZone("GMT");

    private int hour;

    private int minute;

    private int second;

    private TimeZone timeZone;

    public Time(String time) {
        if (time == null || time.length() < 8) throw new IllegalArgumentException("Invalid time format : " + time);
        if (time.length() == 8) {
            this.parseTime(time);
            this.timeZone = TimeZone.getDefault();
        } else {
            this.parseTime(time.substring(0, 8));
            String tz = time.substring(8);
            if (tz.equals("Z")) this.timeZone = TIME_ZONE_GMT; else this.timeZone = TimeZone.getTimeZone("GMT" + tz);
        }
    }

    private void parseTime(String time) {
        for (int i = 0; i < time.length(); i++) {
            char c = time.charAt(i);
            if (i == 2 || i == 5) if (c != ':') throw new IllegalArgumentException("Invalid time format : " + time); else continue;
            if (!Character.isDigit(c)) throw new IllegalArgumentException("Invalid time format : " + time);
        }
        this.set(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(3, 5)), Integer.parseInt(time.substring(6, 8)));
    }

    public Time(int hour, int minute, int second, TimeZone timeZone) {
        super();
        this.set(hour, minute, second);
    }

    private void set(int hour, int minute, int second) {
        if (hour < 0 || hour > 23) throw new IllegalArgumentException("Invalid hour : " + hour);
        this.hour = hour;
        if (minute < 0 || minute > 59) throw new IllegalArgumentException("Invalid minute : " + minute);
        this.minute = minute;
        if (second < 0 || second > 59) throw new IllegalArgumentException("Invalid second : " + minute);
        this.second = second;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Time)) return false;
        Time other = (Time) obj;
        boolean result = (this.hour == other.hour && this.minute == other.minute && this.hour == other.hour);
        boolean tzResult = (this.timeZone == other.timeZone);
        if (!tzResult && this.timeZone != null && other.timeZone != null) tzResult = (this.timeZone.getRawOffset() == other.timeZone.getRawOffset());
        return (result && tzResult);
    }

    @Override
    public int hashCode() {
        final int iConstant = 37;
        int iTotal = 17;
        iTotal = iTotal * iConstant + this.hour;
        iTotal = iTotal * iConstant + this.minute;
        iTotal = iTotal * iConstant + this.second;
        if (this.timeZone != null) iTotal = iTotal * iConstant + this.timeZone.hashCode();
        return iTotal;
    }

    @Override
    public int compareTo(Time o) {
        int result = 0;
        result = (this.hour == o.hour ? 0 : (this.hour > o.hour ? 1 : -1));
        if (result == 0) result = (this.minute == o.minute ? 0 : (this.minute > o.minute ? 1 : -1));
        if (result == 0) result = (this.second == o.second ? 0 : (this.second > o.second ? 1 : -1));
        if (result == 0) {
            long tz = 0;
            long otz = 0;
            if (this.timeZone != null) tz = this.timeZone.getRawOffset();
            if (o.timeZone != null) otz = o.timeZone.getRawOffset();
            result = (tz == otz ? 0 : (tz > otz ? 1 : -1));
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        if (this.hour < 10) buffer.append(0);
        buffer.append(this.hour);
        buffer.append(':');
        if (this.minute < 10) buffer.append(0);
        buffer.append(this.minute);
        buffer.append(':');
        if (this.second < 10) buffer.append(0);
        buffer.append(this.second);
        if (this.timeZone != null) {
            String tz = this.timeZone.getID();
            if (tz.equals("GMT")) buffer.append('Z'); else buffer.append(tz.substring(3));
        }
        return buffer.toString();
    }
}
