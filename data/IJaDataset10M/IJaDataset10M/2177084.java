package com.risertech.xdav.webdav.type;

/**
 * From rfc 2518 section 9.8
 * 
 * TimeType = ("Second-" DAVTimeOutVal | "Infinite" | Other)
 * DAVTimeOutVal = 1*digit
 * Other = "Extend" field-value   ; See section 4.2 of [RFC2068]
 * 
 * @author phil
 */
public class Time {

    private final int seconds;

    /**
	 * -1 for Infinite
	 * @param seconds
	 */
    public Time(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }
}
