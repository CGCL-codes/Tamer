package com.jrefinery.finance.conventions;

import com.jrefinery.common.date.*;
import com.jrefinery.finance.data.reference.*;

/**
 * This class implements the "following" date roll convention, using the definition from
 * "Money Market & Bond Calculations" by Stigum & Robinson (1996).
 */
public class Following implements DateRollConvention {

    /**
     * Returns a date that is the result of applying the date roll convention to the specified
     * date using the specified calendar.
     */
    public SerialDate rollDate(SerialDate date, BusinessDayCalendar calendar) {
        while (!calendar.isBusinessDay(date)) {
            date.addDays(1);
        }
        return date;
    }
}
