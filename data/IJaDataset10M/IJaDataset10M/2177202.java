package com.sitescape.team.calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.document.DateTools;
import org.joda.time.Interval;
import com.sitescape.util.cal.CalendarUtil;

public abstract class AbstractIntervalView {

    public static class VisibleIntervalFormattedDates {

        public String startDate;

        public String endDate;

        public VisibleIntervalFormattedDates(String startDate, String endDate) {
            super();
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }

    protected Interval interval;

    protected Interval visibleInterval;

    public VisibleIntervalFormattedDates getVisibleInterval() {
        return new VisibleIntervalFormattedDates(DateTools.dateToString(visibleInterval.getStart().toDate(), DateTools.Resolution.MINUTE), DateTools.dateToString(visibleInterval.getEnd().toDate(), DateTools.Resolution.MINUTE));
    }

    public boolean dateInView(Date dateToTest) {
        if (dateToTest == null) {
            return false;
        }
        return visibleInterval.contains(dateToTest.getTime());
    }

    /**
	 * 
	 * @param startDate
	 * @param endDate
	 * @return true if at least a part of event overlaps visible view.
	 */
    public boolean intervalInView(long startDate, long endDate) {
        return this.visibleInterval.overlaps(new Interval(startDate, endDate));
    }

    public Map getCurrentDateMonthInfo() {
        Map result = new HashMap();
        result.put("year", interval.getStart().getYear());
        result.put("month", interval.getStart().getMonthOfYear() - 1);
        result.put("beginView", visibleInterval.getStart().toDate());
        result.put("endView", visibleInterval.getEnd().minusDays(1).toDate());
        int daysInMonthView = CalendarUtil.fullDaysBetween(visibleInterval.getEnd().toDate(), visibleInterval.getStart().toDate());
        if (daysInMonthView == 27 || daysInMonthView == 29) {
            daysInMonthView = 28;
        }
        if (daysInMonthView == 34 || daysInMonthView == 36) {
            daysInMonthView = 35;
        }
        if (daysInMonthView == 41 || daysInMonthView == 43) {
            daysInMonthView = 42;
        }
        result.put("numberOfDaysInView", daysInMonthView);
        return result;
    }

    public Date getStart() {
        return interval.getStart().toDate();
    }

    public Date getEnd() {
        return interval.getEnd().toDate();
    }

    public Date getVisibleStart() {
        return this.visibleInterval.getStart().toDate();
    }

    public Date getVisibleEnd() {
        return this.visibleInterval.getEnd().toDate();
    }
}
