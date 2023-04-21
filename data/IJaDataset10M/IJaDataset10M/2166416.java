package uk.co.fortunecookie.timesheet.data.entities.comparators;

import java.util.Comparator;
import uk.co.fortunecookie.timesheet.data.entities.Activity;

public class ActivityNormalRateComparator implements Comparator<Activity> {

    @Override
    public int compare(Activity thisObj, Activity thatObj) {
        int cmp = thisObj.getNormalRate().compareTo(thatObj.getNormalRate());
        if (cmp != 0) return cmp;
        return thisObj.getActivityId().compareTo(thatObj.getActivityId());
    }
}
