package com.liferay.portlet.calendar.action;

import com.liferay.portal.kernel.cal.DayAndPosition;
import com.liferay.portal.kernel.cal.Duration;
import com.liferay.portal.kernel.cal.Recurrence;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.TimeZoneUtil;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.calendar.EventDurationException;
import com.liferay.portlet.calendar.EventEndDateException;
import com.liferay.portlet.calendar.EventStartDateException;
import com.liferay.portlet.calendar.EventTitleException;
import com.liferay.portlet.calendar.NoSuchEventException;
import com.liferay.portlet.calendar.service.CalEventServiceUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="EditEventAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class EditEventAction extends PortletAction {

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        String cmd = ParamUtil.getString(actionRequest, Constants.CMD);
        try {
            if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
                updateEvent(actionRequest);
            } else if (cmd.equals(Constants.DELETE)) {
                deleteEvent(actionRequest);
            }
            sendRedirect(actionRequest, actionResponse);
        } catch (Exception e) {
            if (e instanceof NoSuchEventException || e instanceof PrincipalException) {
                SessionErrors.add(actionRequest, e.getClass().getName());
                setForward(actionRequest, "portlet.calendar.error");
            } else if (e instanceof EventDurationException || e instanceof EventEndDateException || e instanceof EventStartDateException || e instanceof EventTitleException) {
                SessionErrors.add(actionRequest, e.getClass().getName());
            } else {
                throw e;
            }
        }
    }

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {
        try {
            ActionUtil.getEvent(renderRequest);
        } catch (Exception e) {
            if (e instanceof NoSuchEventException || e instanceof PrincipalException) {
                SessionErrors.add(renderRequest, e.getClass().getName());
                return mapping.findForward("portlet.calendar.error");
            } else {
                throw e;
            }
        }
        return mapping.findForward(getForward(renderRequest, "portlet.calendar.edit_event"));
    }

    protected void addWeeklyDayPos(ActionRequest actionRequest, List<DayAndPosition> list, int day) {
        if (ParamUtil.getBoolean(actionRequest, "weeklyDayPos" + day)) {
            list.add(new DayAndPosition(day, 0));
        }
    }

    protected void deleteEvent(ActionRequest actionRequest) throws Exception {
        long eventId = ParamUtil.getLong(actionRequest, "eventId");
        CalEventServiceUtil.deleteEvent(eventId);
    }

    protected void updateEvent(ActionRequest actionRequest) throws Exception {
        Layout layout = (Layout) actionRequest.getAttribute(WebKeys.LAYOUT);
        long eventId = ParamUtil.getLong(actionRequest, "eventId");
        String title = ParamUtil.getString(actionRequest, "title");
        String description = ParamUtil.getString(actionRequest, "description");
        int startDateMonth = ParamUtil.getInteger(actionRequest, "startDateMonth");
        int startDateDay = ParamUtil.getInteger(actionRequest, "startDateDay");
        int startDateYear = ParamUtil.getInteger(actionRequest, "startDateYear");
        int startDateHour = ParamUtil.getInteger(actionRequest, "startDateHour");
        int startDateMinute = ParamUtil.getInteger(actionRequest, "startDateMinute");
        int startDateAmPm = ParamUtil.getInteger(actionRequest, "startDateAmPm");
        if (startDateAmPm == Calendar.PM) {
            startDateHour += 12;
        }
        int durationHour = ParamUtil.getInteger(actionRequest, "durationHour");
        int durationMinute = ParamUtil.getInteger(actionRequest, "durationMinute");
        boolean allDay = ParamUtil.getBoolean(actionRequest, "allDay");
        boolean timeZoneSensitive = ParamUtil.getBoolean(actionRequest, "timeZoneSensitive");
        String type = ParamUtil.getString(actionRequest, "type");
        int endDateMonth = ParamUtil.getInteger(actionRequest, "endDateMonth");
        int endDateDay = ParamUtil.getInteger(actionRequest, "endDateDay");
        int endDateYear = ParamUtil.getInteger(actionRequest, "endDateYear");
        boolean repeating = false;
        int recurrenceType = ParamUtil.getInteger(actionRequest, "recurrenceType");
        if (recurrenceType != Recurrence.NO_RECURRENCE) {
            repeating = true;
        }
        Locale locale = null;
        TimeZone timeZone = null;
        if (timeZoneSensitive) {
            User user = PortalUtil.getUser(actionRequest);
            locale = user.getLocale();
            timeZone = user.getTimeZone();
        } else {
            locale = LocaleUtil.getDefault();
            timeZone = TimeZoneUtil.getDefault();
        }
        Calendar startDate = CalendarFactoryUtil.getCalendar(timeZone, locale);
        startDate.set(Calendar.MONTH, startDateMonth);
        startDate.set(Calendar.DATE, startDateDay);
        startDate.set(Calendar.YEAR, startDateYear);
        startDate.set(Calendar.HOUR_OF_DAY, startDateHour);
        startDate.set(Calendar.MINUTE, startDateMinute);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);
        if (allDay) {
            startDate.set(Calendar.HOUR_OF_DAY, 0);
            startDate.set(Calendar.MINUTE, 0);
            startDate.set(Calendar.SECOND, 0);
            startDate.set(Calendar.MILLISECOND, 0);
            durationHour = 24;
            durationMinute = 0;
        }
        Recurrence recurrence = null;
        if (repeating) {
            Calendar recStartCal = null;
            if (timeZoneSensitive) {
                recStartCal = CalendarFactoryUtil.getCalendar();
                recStartCal.setTime(startDate.getTime());
            } else {
                recStartCal = (Calendar) startDate.clone();
            }
            recurrence = new Recurrence(recStartCal, new Duration(1, 0, 0, 0), recurrenceType);
            recurrence.setWeekStart(Calendar.SUNDAY);
            if (recurrenceType == Recurrence.DAILY) {
                int dailyType = ParamUtil.getInteger(actionRequest, "dailyType");
                if (dailyType == 0) {
                    int dailyInterval = ParamUtil.getInteger(actionRequest, "dailyInterval", 1);
                    recurrence.setInterval(dailyInterval);
                } else {
                    DayAndPosition[] dayPos = { new DayAndPosition(Calendar.MONDAY, 0), new DayAndPosition(Calendar.TUESDAY, 0), new DayAndPosition(Calendar.WEDNESDAY, 0), new DayAndPosition(Calendar.THURSDAY, 0), new DayAndPosition(Calendar.FRIDAY, 0) };
                    recurrence.setByDay(dayPos);
                }
            } else if (recurrenceType == Recurrence.WEEKLY) {
                int weeklyInterval = ParamUtil.getInteger(actionRequest, "weeklyInterval", 1);
                recurrence.setInterval(weeklyInterval);
                List<DayAndPosition> dayPos = new ArrayList<DayAndPosition>();
                addWeeklyDayPos(actionRequest, dayPos, Calendar.SUNDAY);
                addWeeklyDayPos(actionRequest, dayPos, Calendar.MONDAY);
                addWeeklyDayPos(actionRequest, dayPos, Calendar.TUESDAY);
                addWeeklyDayPos(actionRequest, dayPos, Calendar.WEDNESDAY);
                addWeeklyDayPos(actionRequest, dayPos, Calendar.THURSDAY);
                addWeeklyDayPos(actionRequest, dayPos, Calendar.FRIDAY);
                addWeeklyDayPos(actionRequest, dayPos, Calendar.SATURDAY);
                if (dayPos.size() == 0) {
                    dayPos.add(new DayAndPosition(Calendar.MONDAY, 0));
                }
                recurrence.setByDay(dayPos.toArray(new DayAndPosition[0]));
            } else if (recurrenceType == Recurrence.MONTHLY) {
                int monthlyType = ParamUtil.getInteger(actionRequest, "monthlyType");
                if (monthlyType == 0) {
                    int monthlyDay = ParamUtil.getInteger(actionRequest, "monthlyDay0");
                    recurrence.setByMonthDay(new int[] { monthlyDay });
                    int monthlyInterval = ParamUtil.getInteger(actionRequest, "monthlyInterval0", 1);
                    recurrence.setInterval(monthlyInterval);
                } else {
                    int monthlyPos = ParamUtil.getInteger(actionRequest, "monthlyPos");
                    int monthlyDay = ParamUtil.getInteger(actionRequest, "monthlyDay1");
                    DayAndPosition[] dayPos = { new DayAndPosition(monthlyDay, monthlyPos) };
                    recurrence.setByDay(dayPos);
                    int monthlyInterval = ParamUtil.getInteger(actionRequest, "monthlyInterval1", 1);
                    recurrence.setInterval(monthlyInterval);
                }
            } else if (recurrenceType == Recurrence.YEARLY) {
                int yearlyType = ParamUtil.getInteger(actionRequest, "yearlyType");
                if (yearlyType == 0) {
                    int yearlyMonth = ParamUtil.getInteger(actionRequest, "yearlyMonth0");
                    int yearlyDay = ParamUtil.getInteger(actionRequest, "yearlyDay0");
                    recurrence.setByMonth(new int[] { yearlyMonth });
                    recurrence.setByMonthDay(new int[] { yearlyDay });
                    int yearlyInterval = ParamUtil.getInteger(actionRequest, "yearlyInterval0", 1);
                    recurrence.setInterval(yearlyInterval);
                } else {
                    int yearlyPos = ParamUtil.getInteger(actionRequest, "yearlyPos");
                    int yearlyDay = ParamUtil.getInteger(actionRequest, "yearlyDay1");
                    int yearlyMonth = ParamUtil.getInteger(actionRequest, "yearlyMonth1");
                    DayAndPosition[] dayPos = { new DayAndPosition(yearlyDay, yearlyPos) };
                    recurrence.setByDay(dayPos);
                    recurrence.setByMonth(new int[] { yearlyMonth });
                    int yearlyInterval = ParamUtil.getInteger(actionRequest, "yearlyInterval1", 1);
                    recurrence.setInterval(yearlyInterval);
                }
            }
            int endDateType = ParamUtil.getInteger(actionRequest, "endDateType");
            if (endDateType == 1) {
                int endDateOccurrence = ParamUtil.getInteger(actionRequest, "endDateOccurrence");
                recurrence.setOccurrence(endDateOccurrence);
            } else if (endDateType == 2) {
                Calendar recEndCal = null;
                if (timeZoneSensitive) {
                    recEndCal = CalendarFactoryUtil.getCalendar();
                    recEndCal.setTime(startDate.getTime());
                } else {
                    recEndCal = (Calendar) startDate.clone();
                }
                recEndCal.set(Calendar.MONTH, endDateMonth);
                recEndCal.set(Calendar.DATE, endDateDay);
                recEndCal.set(Calendar.YEAR, endDateYear);
                recurrence.setUntil(recEndCal);
            }
        }
        String remindBy = ParamUtil.getString(actionRequest, "remindBy");
        int firstReminder = ParamUtil.getInteger(actionRequest, "firstReminder");
        int secondReminder = ParamUtil.getInteger(actionRequest, "secondReminder");
        String[] communityPermissions = actionRequest.getParameterValues("communityPermissions");
        String[] guestPermissions = actionRequest.getParameterValues("guestPermissions");
        if (eventId <= 0) {
            CalEventServiceUtil.addEvent(layout.getPlid(), title, description, startDateMonth, startDateDay, startDateYear, startDateHour, startDateMinute, endDateMonth, endDateDay, endDateYear, durationHour, durationMinute, allDay, timeZoneSensitive, type, repeating, recurrence, remindBy, firstReminder, secondReminder, communityPermissions, guestPermissions);
        } else {
            CalEventServiceUtil.updateEvent(eventId, title, description, startDateMonth, startDateDay, startDateYear, startDateHour, startDateMinute, endDateMonth, endDateDay, endDateYear, durationHour, durationMinute, allDay, timeZoneSensitive, type, repeating, recurrence, remindBy, firstReminder, secondReminder);
        }
    }
}
