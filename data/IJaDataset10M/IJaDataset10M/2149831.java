package com.ec.service;

public final class DateTime implements java.lang.Cloneable {

    public short year;

    public short month;

    public short day;

    public short hour;

    public short minute;

    public short second;

    public DateTime() {
    }

    public DateTime(short year, short month, short day, short hour, short minute, short second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public boolean equals(java.lang.Object rhs) {
        if (this == rhs) {
            return true;
        }
        DateTime _r = null;
        try {
            _r = (DateTime) rhs;
        } catch (ClassCastException ex) {
        }
        if (_r != null) {
            if (year != _r.year) {
                return false;
            }
            if (month != _r.month) {
                return false;
            }
            if (day != _r.day) {
                return false;
            }
            if (hour != _r.hour) {
                return false;
            }
            if (minute != _r.minute) {
                return false;
            }
            if (second != _r.second) {
                return false;
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int __h = 0;
        __h = 5 * __h + (int) year;
        __h = 5 * __h + (int) month;
        __h = 5 * __h + (int) day;
        __h = 5 * __h + (int) hour;
        __h = 5 * __h + (int) minute;
        __h = 5 * __h + (int) second;
        return __h;
    }

    public java.lang.Object clone() {
        java.lang.Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException ex) {
            assert false;
        }
        return o;
    }

    public void __write(IceInternal.BasicStream __os) {
        __os.writeShort(year);
        __os.writeShort(month);
        __os.writeShort(day);
        __os.writeShort(hour);
        __os.writeShort(minute);
        __os.writeShort(second);
    }

    public void __read(IceInternal.BasicStream __is) {
        year = __is.readShort();
        month = __is.readShort();
        day = __is.readShort();
        hour = __is.readShort();
        minute = __is.readShort();
        second = __is.readShort();
    }
}
