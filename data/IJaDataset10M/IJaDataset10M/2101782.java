package com.dukesoftware.utils.common;

import java.util.Date;

public class DateRange {

    private final Date from;

    private final Date to;

    public DateRange(Date from, Date to) {
        if (from.after(to)) {
            throw new IllegalArgumentException("");
        }
        Assert.assertNotNull(from, "");
        Assert.assertNotNull(to, "");
        this.from = from;
        this.to = to;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

    public long days() {
        return DateUtils.countDaysBetween(from, to);
    }

    @Override
    public String toString() {
        return "'" + from + "'-'" + to + "'";
    }
}
