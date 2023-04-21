package com.kni.etl;

import java.sql.SQLException;
import java.util.Date;

/**
 * Insert the type's description here. Creation date: (8/22/2006 1:50:01 PM)
 * 
 * @author: dnguyen
 */
public class ETLJobSchedule {

    /** The Month. */
    private int Month;

    /** The Month of year. */
    private int MonthOfYear;

    /** The Day. */
    private int Day;

    /** The Day of week. */
    private int DayOfWeek;

    /** The Day of month. */
    private int DayOfMonth;

    /** The Hour. */
    private int Hour;

    /** The Hour of day. */
    private int HourOfDay;

    /** The Minute. */
    private int Minute;

    /** The Minute of hour. */
    private int MinuteOfHour;

    /** The Description. */
    private String Description;

    /** The Once only date. */
    private Date OnceOnlyDate;

    /** The Enable date. */
    private Date EnableDate;

    /** The Disable date. */
    private Date DisableDate;

    /** The is validated. */
    private boolean isValidated = false;

    /**
     * Gets the month.
     * 
     * @return the month
     */
    public int getMonth() {
        return this.Month;
    }

    /**
     * Gets the month of year.
     * 
     * @return the month of year
     */
    public int getMonthOfYear() {
        return this.MonthOfYear;
    }

    /**
     * Gets the day.
     * 
     * @return the day
     */
    public int getDay() {
        return this.Day;
    }

    /**
     * Gets the day of week.
     * 
     * @return the day of week
     */
    public int getDayOfWeek() {
        return this.DayOfWeek;
    }

    /**
     * Gets the day of month.
     * 
     * @return the day of month
     */
    public int getDayOfMonth() {
        return this.DayOfMonth;
    }

    /**
     * Gets the hour.
     * 
     * @return the hour
     */
    public int getHour() {
        return this.Hour;
    }

    /**
     * Gets the hour of day.
     * 
     * @return the hour of day
     */
    public int getHourOfDay() {
        return this.HourOfDay;
    }

    /**
     * Gets the minute.
     * 
     * @return the minute
     */
    public int getMinute() {
        return this.Minute;
    }

    /**
     * Gets the minute of hour.
     * 
     * @return the minute of hour
     */
    public int getMinuteOfHour() {
        return this.MinuteOfHour;
    }

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return this.Description;
    }

    /**
     * Gets the once only date.
     * 
     * @return the once only date
     */
    public Date getOnceOnlyDate() {
        return this.OnceOnlyDate;
    }

    /**
     * Gets the enable date.
     * 
     * @return the enable date
     */
    public Date getEnableDate() {
        return this.EnableDate;
    }

    /**
     * Gets the disable date.
     * 
     * @return the disable date
     */
    public Date getDisableDate() {
        return this.DisableDate;
    }

    /**
     * Checks if is schedule validated.
     * 
     * @return true, if is schedule validated
     */
    public boolean isScheduleValidated() {
        return this.isValidated;
    }

    /**
     * ETLJobStatus constructor comment.
     * 
     * @param pMonth the month
     * @param pMonthOfYear the month of year
     * @param pDay the day
     * @param pDayOfWeek the day of week
     * @param pDayOfMonth the day of month
     * @param pHour the hour
     * @param pHourOfDay the hour of day
     * @param pMinute the minute
     * @param pMinuteOfHour the minute of hour
     * @param pDescription the description
     * @param pOnceOnlyDate the once only date
     * @param pEnableDate the enable date
     * @param pDisableDate the disable date
     */
    public ETLJobSchedule(int pMonth, int pMonthOfYear, int pDay, int pDayOfWeek, int pDayOfMonth, int pHour, int pHourOfDay, int pMinute, int pMinuteOfHour, String pDescription, Date pOnceOnlyDate, Date pEnableDate, Date pDisableDate) {
        this.Month = pMonth;
        this.MonthOfYear = pMonthOfYear;
        this.Day = pDay;
        this.DayOfWeek = pDayOfWeek;
        this.DayOfMonth = pDayOfMonth;
        this.Hour = pHour;
        this.HourOfDay = pHourOfDay;
        this.Minute = pMinute;
        this.MinuteOfHour = pMinuteOfHour;
        this.Description = pDescription;
        this.OnceOnlyDate = pOnceOnlyDate;
        this.EnableDate = pEnableDate;
        this.DisableDate = pDisableDate;
    }

    /**
     * Validate schedule.
     * 
     * @return Returns errors if it's invalid.
     * 
     * @author dnguyen 2006-08-22
     */
    public String validateSchedule() {
        try {
            this.setDefaults();
            this.isValidated = true;
            return "";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    /**
     * Sets the defaults.
     * 
     * @throws SQLException the SQL exception
     * @throws Exception the exception
     */
    private void setDefaults() throws SQLException, java.lang.Exception {
        if (this.OnceOnlyDate != null) {
            this.Month = -1;
            this.Day = -1;
            this.Hour = -1;
            this.Minute = -1;
            this.MonthOfYear = -1;
            this.DayOfMonth = -1;
            this.DayOfWeek = -1;
            this.HourOfDay = -1;
            this.MinuteOfHour = -1;
            return;
        }
        if (this.MonthOfYear >= 0) {
            if (this.MonthOfYear > 11) throw new Exception("Invalid job schedule value: Month-Of-Year values are 0-11 for Jan-Dec.");
            if ((this.MonthOfYear >= 0) && (this.Month > 0)) throw new Exception("Invalid job schedule combination: Only the Month-Increment or Month-Of-Year should be set."); else this.Month = 12;
        }
        if (this.DayOfMonth > 0) {
            if (this.DayOfMonth > 31) throw new Exception("Invalid job schedule value: Day-Of-Month values are 1-31."); else if ((this.Day > 0) || (this.DayOfWeek > 0)) throw new Exception("Invalid job schedule combination: Only the Day-Increment or Day-Of-Month or Day-Of-Week should be set."); else if (this.Month <= 0) this.Month = 1;
        }
        if (this.DayOfWeek >= 0) {
            if (this.DayOfWeek > 6) throw new Exception("Invalid job schedule value: Day-Of-Week values are 1-7 for Sun-Sat."); else if ((this.Day > 0) || (this.DayOfMonth > 0)) throw new Exception("Invalid job schedule combination: Only the Day-Increment or Day-Of-Month or Day-Of-Week should be set."); else if ((this.Month <= 0)) this.Day = 7;
        }
        if (this.HourOfDay >= 0) {
            if (this.HourOfDay > 23) throw new Exception("Invalid job schedule value: Hour-Of-Day values are 0-23."); else if (this.Hour > 0) throw new Exception("Invalid job schedule combination: Only the Hour-Increment or Hour-Of-Day should be set."); else if ((this.Day <= 0) && (this.DayOfMonth <= 0) && (this.DayOfWeek <= 0)) this.Day = 1;
        }
        if (this.MinuteOfHour >= 0) {
            if (this.MinuteOfHour > 59) throw new Exception("Invalid job schedule value: Minute-of-Hour values are 0-59."); else if (this.Minute > 0) throw new Exception("Invalid job schedule combination: Only the Minute-Increment or Minute-of-Hour should be set."); else if ((this.Hour <= 0) && (this.HourOfDay <= 0)) this.Hour = 1;
        }
    }
}
