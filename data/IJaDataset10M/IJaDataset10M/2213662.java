package com.mtgi.xml;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/** SimpleDateFormat doesn't give us a time zone option that meets W3C standards, so we provide our own */
public class XmlDateFormat extends SimpleDateFormat {

    private static final long serialVersionUID = -5310271700921914349L;

    public XmlDateFormat() {
        this("yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    public XmlDateFormat(String format) {
        super(format);
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
        StringBuffer ret = super.format(date, toAppendTo, pos);
        int value = (calendar.get(Calendar.ZONE_OFFSET) + calendar.get(Calendar.DST_OFFSET)) / 60000;
        if (value < 0) {
            ret.append('-');
            value *= -1;
        } else {
            ret.append('+');
        }
        appendTwoDigit(toAppendTo, value / 60);
        toAppendTo.append(':');
        appendTwoDigit(toAppendTo, value % 60);
        return ret;
    }

    private static final void appendTwoDigit(StringBuffer buf, int value) {
        buf.append(value / 10).append(value % 10);
    }
}
