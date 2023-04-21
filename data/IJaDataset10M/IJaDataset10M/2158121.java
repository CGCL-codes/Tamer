package uk.co.fortunecookie.timesheet.data.entities.comparators;

import java.util.Comparator;
import uk.co.fortunecookie.timesheet.data.entities.TimesheetSummaryView;

public class TimesheetSummaryViewEmployeeIdComparator implements Comparator<TimesheetSummaryView> {

    @Override
    public int compare(TimesheetSummaryView thisObj, TimesheetSummaryView thatObj) {
        int cmp = thisObj.getEmployeeId().compareTo(thatObj.getEmployeeId());
        if (cmp != 0) return cmp;
        return thisObj.getTimesheetId().compareTo(thatObj.getTimesheetId());
    }
}
