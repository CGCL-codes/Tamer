package com.patientis.framework.scheduler.units;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.io.Serializable;
import com.patientis.model.common.DateTimeModel;

/**
 * One line class description
 *
 * 
 * <br/>  
 */
public class Second implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Seconds 
	 */
    private int seconds = 0;

    /**
	 * Milliseconds per second
	 */
    public static final int millisecondsPerSecond = 1000;

    /**
	 * Second 0-59
	 * @param seconds
	 */
    public Second(int seconds) {
        if (seconds < 0 || seconds > 59) {
            throw new IllegalTimeUnitException(String.valueOf(seconds));
        } else {
            this.seconds = seconds;
        }
    }

    /**
	 * Determine if the check time seconds equals this seconds
	 * 
	 * @param check
	 * @return true if seconds match
	 */
    public boolean matches(DateTimeModel check) {
        return seconds == check.get(Calendar.SECOND);
    }

    /**
	 * Value
	 * 
	 * @return seconds
	 */
    public int getValue() {
        return seconds;
    }

    /**
	 * Get a list of Second values from these values
	 * 
	 * @param values
	 */
    public static List<Second> getList(List<Integer> values) {
        List<Second> months = new ArrayList<Second>(values.size());
        for (Integer value : values) {
            months.add(new Second(value));
        }
        return months;
    }
}
