package com.advancedpwr.record.methods;

import java.util.Date;

public class DateHolder {

    protected Date fieldDate;

    protected Date fieldOtherDate;

    public Date getDate() {
        return fieldDate;
    }

    public void setDate(Date date) {
        fieldDate = date;
    }

    public Date getOtherDate() {
        return fieldOtherDate;
    }

    public void setOtherDate(Date otherDate) {
        fieldOtherDate = otherDate;
    }
}
