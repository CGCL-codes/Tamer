package org.kazao.calendar;

import java.util.Date;

public class KazaoCalendarDate extends KazaoCalendarDateTime {

    public KazaoCalendarDate() {
        setFormat(getLocalize().getShortDateFormat());
    }

    public KazaoCalendarDate(KazaoCalendarLocal localize) {
        setFormat(getLocalize().getShortDateFormat());
        setLocalize(localize);
    }

    public String getDate() {
        return getDateTime(getLocalize().getShortDateFormat());
    }

    public String getDate(String format) {
        return getDateTime(format);
    }

    public void setDate(String date, String dateFormat) throws KazaoCalendarDateTimeException {
        setDateTime(date, dateFormat);
    }

    public void setDate(String date) throws KazaoCalendarDateTimeException {
        setDateTime(date, getLocalize().getShortDateFormat());
    }

    public void setDate(Date date) {
        super.setDate(date);
    }

    public void setDate(java.sql.Date date) {
        super.setDate(date);
    }
}
