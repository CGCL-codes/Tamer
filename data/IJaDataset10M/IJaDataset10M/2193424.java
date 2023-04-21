package com.technoetic.xplanner.domain.virtual;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyTimesheetEntry implements Serializable {

    private java.util.Date entryDate;

    private java.math.BigDecimal totalDuration = new BigDecimal(0.0);

    public static SimpleDateFormat shortDateFormat = new SimpleDateFormat("EEE d-MMM-yy");

    public DailyTimesheetEntry() {
    }

    public DailyTimesheetEntry(Date entryDate, BigDecimal duration) {
        this.setEntryDate(entryDate);
        this.setTotalDuration(duration);
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public String getEntryDateShort() {
        return shortDateFormat.format(this.getEntryDate());
    }

    public void setEntryDate(Date date) {
        this.entryDate = date;
    }

    public java.math.BigDecimal getTotalDuration() {
        return totalDuration.setScale(1, BigDecimal.ROUND_HALF_EVEN);
    }

    public void setTotalDuration(java.math.BigDecimal totalDuration) {
        this.totalDuration = totalDuration;
    }

    public void setTotalDuration(double totalDuration) {
        this.totalDuration = this.totalDuration.add(new BigDecimal(totalDuration));
    }
}
