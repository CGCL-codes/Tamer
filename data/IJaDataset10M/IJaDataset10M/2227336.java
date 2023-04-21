package org.esk.dablog.web.forms;

import org.springframework.web.servlet.mvc.ParameterizableViewController;
import org.springframework.web.servlet.ModelAndView;
import org.esk.dablog.service.EntryManager;
import org.esk.dablog.web.elements.CalendarBean;
import org.esk.dablog.ApplicationConstants;
import org.esk.dablog.model.entries.Ride;
import org.apache.commons.lang.StringUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

/**
 * This class
 * User: jc
 * Date: 22.11.2006
 * Time: 12:32:34
 * $Id:$
 */
public class CalendarForm extends ParameterizableViewController {

    private EntryManager entryManager;

    private static final int LAST_ENTRIES_COUNT = 5;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView result = new ModelAndView(getViewName());
        List<Integer> dates = entryManager.listMonthCalendar(Ride.class, Calendar.getInstance());
        Date calendarDate = extractDate(request);
        List lastEntries = null;
        if (calendarDate != null) {
            lastEntries = entryManager.listEntriesByDate(Ride.class, calendarDate);
            result.addObject("calendarDate", ApplicationConstants.getInstance().getSystemDateFormat().format(calendarDate));
        } else {
            lastEntries = entryManager.listEntries(Ride.class, LAST_ENTRIES_COUNT);
        }
        result.addObject("dates", dates);
        result.addObject("entries", lastEntries);
        CalendarBean calendarBean = new CalendarBean();
        calendarBean.setCurrentDate(Calendar.getInstance());
        calendarBean.setDates(dates);
        calendarBean.setDayHref(request.getContextPath() + "/calendar.form");
        result.addObject("calendar", calendarBean);
        return result;
    }

    /**
     * extracts date from request path, i.e. /calendar.form/2006-01-01
     *
     * @param request
     * @return
     */
    private Date extractDate(HttpServletRequest request) {
        String req = request.getRequestURI();
        String[] result = StringUtils.split(req, '/');
        String date = result[result.length - 1];
        try {
            return ApplicationConstants.getInstance().getSystemDateFormat().parse(date);
        } catch (Exception ex) {
            return null;
        }
    }

    public void setEntryManager(EntryManager entryManager) {
        this.entryManager = entryManager;
    }
}
