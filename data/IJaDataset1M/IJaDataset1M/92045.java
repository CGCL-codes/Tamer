package org.homemotion.ui.admin.calendar;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.homemotion.calendar.AnnualCalendar;
import org.homemotion.calendar.Calendar;
import org.homemotion.calendar.CalendarManager;
import org.homemotion.calendar.CombinedCalendar;
import org.homemotion.calendar.DailyCalendar;
import org.homemotion.calendar.HolidayCalendar;
import org.homemotion.calendar.MonthlyCalendar;
import org.homemotion.calendar.WeeklyCalendar;
import org.homemotion.dao.ItemManager;
import org.homemotion.di.Registry;
import org.homemotion.ui.state.RequestState;
import org.homemotion.ui.widgets.AbstractPageControl;

@ManagedBean
@RequestScoped
public final class CalendarControl extends AbstractPageControl<Calendar> {

    private static final long serialVersionUID = 3538678863313025476L;

    public CalendarControl() {
        super("/admin/calendar/Calendar");
    }

    @Override
    protected Calendar createNewItem() {
        Calendar calendar = new Calendar();
        calendar.setName("newCalendar");
        return calendar;
    }

    public String createNewDaily() {
        Calendar calendar = new Calendar();
        calendar.setCalendarType(new DailyCalendar());
        calendar.setName("newDailyCalendar");
        RequestState.setData(getItemName(), calendar);
        return getCreateTarget();
    }

    public String createNewWeekly() {
        Calendar calendar = new Calendar();
        calendar.setCalendarType(new WeeklyCalendar());
        calendar.setName("newWeeklyCalendar");
        RequestState.setData(getItemName(), calendar);
        return getCreateTarget();
    }

    public String createNewMonthly() {
        Calendar calendar = new Calendar();
        calendar.setCalendarType(new MonthlyCalendar());
        calendar.setName("newMonthlyCalendar");
        RequestState.setData(getItemName(), calendar);
        return getCreateTarget();
    }

    public String createNewAnnual() {
        Calendar calendar = new Calendar();
        calendar.setName("newAnnualCalendar");
        calendar.setCalendarType(new AnnualCalendar());
        RequestState.setData(getItemName(), calendar);
        return getCreateTarget();
    }

    public String createNewHoliday() {
        Calendar calendar = new Calendar();
        calendar.setCalendarType(new HolidayCalendar());
        calendar.setName("newHolidayCalendar");
        RequestState.setData(getItemName(), calendar);
        return getCreateTarget();
    }

    public String createNewCombined() {
        Calendar calendar = new Calendar();
        calendar.setCalendarType(new CombinedCalendar());
        calendar.setName("newCombinedCalendar");
        RequestState.setData(getItemName(), calendar);
        return getCreateTarget();
    }

    @Override
    protected ItemManager<Calendar> getItemManager() {
        return Registry.getInstance(CalendarManager.class);
    }
}
