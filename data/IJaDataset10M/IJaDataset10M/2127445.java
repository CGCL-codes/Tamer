package com.patientis.framework.scheduler.units;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * One line class description
 *
 * 
 * <br/>  
 */
public class Hour implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Seconds 
	 */
    private int hours = 0;

    /**
	 * Milliseconds per hour
	 */
    public static final int millisecondsPerHour = 60 * Minute.millisecondsPerMinute;

    /**
	 * Second 0-59
	 * @param hours
	 */
    public Hour(int hours) {
        if (hours < 0 || hours > 23) {
            throw new IllegalTimeUnitException(String.valueOf(hours));
        } else {
            this.hours = hours;
        }
    }

    /**
	 * Value
	 * 
	 * @return hour
	 */
    public int getValue() {
        return hours;
    }

    /**
	 * Get a list of Hour values from these values
	 * 
	 * @param values
	 */
    public static List<Hour> getList(List<Integer> values) {
        List<Hour> months = new ArrayList<Hour>(values.size());
        for (Integer value : values) {
            months.add(new Hour(value));
        }
        return months;
    }
}
