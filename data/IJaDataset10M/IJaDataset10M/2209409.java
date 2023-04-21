package com.incendiaryblue.awt.calendar;

public class CalendarServiceException extends Exception {

    Exception cause;

    public CalendarServiceException(Exception e) {
        this.cause = e;
    }

    public CalendarServiceException(String message) {
        super(message);
    }

    public CalendarServiceException(String message, Exception e) {
        super(message);
        this.cause = e;
    }

    public Exception getCause() {
        return this.cause;
    }
}
