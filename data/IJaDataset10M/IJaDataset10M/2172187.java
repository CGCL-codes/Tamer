package net.jforerunning.gui;

import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class TimeFromDoubleFormat extends NumberFormat {

    private boolean doShowSeconds;

    public TimeFromDoubleFormat() {
        this(true);
    }

    public TimeFromDoubleFormat(boolean doShowSeconds) {
        this.doShowSeconds = doShowSeconds;
    }

    @Override
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        return format(Math.round(number), toAppendTo, pos);
    }

    @Override
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        while (number >= 3600) {
            number -= 3600;
            hours++;
        }
        while (number >= 60) {
            number -= 60;
            minutes++;
        }
        seconds = (int) number;
        if (hours > 0) toAppendTo.append(hours + ":");
        toAppendTo.append((hours > 0 && minutes < 10 ? "0" : "") + minutes);
        if (doShowSeconds) toAppendTo.append(":" + (seconds < 10 ? "0" : "") + seconds);
        return toAppendTo;
    }

    @Override
    public Number parse(String source, ParsePosition parsePosition) {
        return null;
    }
}
