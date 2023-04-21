package org.columba.calendar.ui.base;

import org.columba.calendar.model.DateRange;
import org.columba.calendar.model.api.IEvent;
import org.columba.calendar.model.api.IEventInfo;
import com.miginfocom.calendar.activity.Activity;
import com.miginfocom.calendar.activity.DefaultActivity;
import com.miginfocom.util.dates.DateRangeI;
import com.miginfocom.util.dates.ImmutableDateRange;

public class CalendarHelper {

    public static Activity createActivity(IEventInfo model) {
        long startMillis = model.getDtStart().getTimeInMillis();
        long endMillis = model.getDtEnt().getTimeInMillis();
        ImmutableDateRange dr = new ImmutableDateRange(startMillis, endMillis, false, null, null);
        Activity act = new DefaultActivity(dr, model.getId());
        act.setSummary(model.getSummary());
        String calendar = model.getCalendar();
        act.setCategoryIDs(new Object[] { calendar });
        return act;
    }

    public static Activity createActivity(IEvent model) {
        long startMillis = model.getDtStart().getTimeInMillis();
        long endMillis = model.getDtEnt().getTimeInMillis();
        ImmutableDateRange dr = new ImmutableDateRange(startMillis, endMillis, false, null, null);
        Activity act = new DefaultActivity(dr, model.getId());
        act.setSummary(model.getSummary());
        act.setLocation(model.getLocation());
        act.setDescription(model.getDescription());
        String calendar = model.getCalendar();
        act.setCategoryIDs(new Object[] { calendar });
        return act;
    }

    public static void updateDateRange(final Activity activity, IEvent model) {
        DateRangeI dateRange = activity.getDateRangeForReading();
        DateRange cRange = new DateRange(dateRange.getStartMillis(), dateRange.getEndMillis(false));
        model.setDtStart(cRange.getStartTime());
        model.setDtEnt(cRange.getEndTime());
    }
}
