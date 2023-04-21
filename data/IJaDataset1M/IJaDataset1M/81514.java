package model.connections.database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.faces.component.UIInput;
import javax.faces.event.ActionEvent;
import model.connections.database.auto._Issues;
import model.helpclasses.GeniousHelper;
import model.helpclasses.TimeEntriesModel;
import org.apache.cayenne.exp.Expression;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.query.SelectQuery;

public class Issues extends _Issues {

    private Date filterDate;

    private Users filterUser;

    public Users getFilterUser() {
        return filterUser;
    }

    public void setFilterUser(Users filterUser) {
        this.filterUser = filterUser;
    }

    public Date getFilterDate() {
        return filterDate;
    }

    public void setFilterDate(Date tmpDate) {
        this.filterDate = tmpDate;
    }

    public int getFilterWeek() {
        Calendar cal = new GregorianCalendar();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(filterDate);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public int getFilterMonth() {
        Calendar cal = new GregorianCalendar();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(filterDate);
        return cal.get(Calendar.MONTH);
    }

    public int getFilterYear() {
        Calendar cal = new GregorianCalendar();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(filterDate);
        return cal.get(Calendar.YEAR);
    }

    public Date getDateForDay(int day) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(filterDate);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, day);
        return cal.getTime();
    }

    public void addNewEntry(ActionEvent evt) {
        UIInput input = (UIInput) evt.getComponent().getParent();
        Float hours = Float.parseFloat((String) input.getValue());
        if (hours != null) {
            TimeEntries tmpEntry = new TimeEntries();
            tmpEntry.setIssue(this);
            tmpEntry.setProject(getProject());
            tmpEntry.setUser(filterUser);
            tmpEntry.setComments("");
            tmpEntry.setCreatedOn(new Date());
            tmpEntry.setUpdatedOn(new Date());
            tmpEntry.setTweek(getFilterWeek());
            tmpEntry.setTmonth(getFilterMonth());
            tmpEntry.setTyear(getFilterYear());
            tmpEntry.setActivityId(1);
            String compID = evt.getComponent().getParent().getId();
            String sCompID[] = compID.split("_");
            tmpEntry.setSpentOn(getDateForDay(Integer.parseInt(sCompID[2]) + 1));
            tmpEntry.setHours(Float.parseFloat((String) input.getValue()));
        }
    }

    /**
	 * Get Total Hours for the selected week
	 * @return double Total Hours
	 */
    public double getTotalHours() {
        double total = 0;
        for (TimeEntries t : getFilterEntries()) {
            if (GeniousHelper.getDateWeek(t.getSpentOn()) == getFilterWeek() && GeniousHelper.getDateYear(t.getSpentOn()) == getFilterYear() && t.getUser().equals(filterUser)) {
                total = total + t.getHours();
            }
        }
        return total;
    }

    /**
	 * Get a list with TimeEntries that apply to the filter rules
	 * Filters: Current User, Current Week, Last Week
	 * @return List<TimeEntries>
	 */
    public List<TimeEntries> getFilterEntries() {
        List<TimeEntries> entries = getSQLEntries();
        List<TimeEntries> tmpList = new ArrayList<TimeEntries>();
        Calendar calStart = Calendar.getInstance();
        calStart.setFirstDayOfWeek(Calendar.MONDAY);
        calStart.setTime(getFilterDate());
        calStart.set(Calendar.HOUR_OF_DAY, 0);
        calStart.set(Calendar.MINUTE, 0);
        calStart.set(Calendar.SECOND, 0);
        calStart.add(Calendar.WEEK_OF_YEAR, -1);
        calStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date startDate = calStart.getTime();
        Calendar calEnd = Calendar.getInstance();
        calEnd.setFirstDayOfWeek(Calendar.MONDAY);
        calEnd.setTime(getFilterDate());
        calEnd.set(Calendar.HOUR_OF_DAY, 0);
        calEnd.set(Calendar.MINUTE, 0);
        calEnd.set(Calendar.SECOND, 0);
        calEnd.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date endDate = calEnd.getTime();
        for (TimeEntries t : entries) {
            if (t.getUser().equals(filterUser)) {
                if ((startDate.before(t.getSpentOn()) && endDate.after(t.getSpentOn())) || (startDate.equals(t.getSpentOn()) || endDate.equals(t.getSpentOn()))) {
                    tmpList.add(t);
                }
            }
        }
        return tmpList;
    }

    /**
	 * Check is the issue has entries within the current or the last week
	 * @return boolean
	 */
    public boolean hasFilterEntries() {
        if (getFilterEntries().size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * Get All entries for a day_of_week. Filters apply to the selection.
	 * @param filterDay
	 * @return List<TimeEntries>
	 */
    public List<TimeEntries> getEntriesForWeekDay(int filterDay) {
        List<TimeEntries> tmp = new ArrayList<TimeEntries>();
        Iterator<TimeEntries> it = getFilterEntries().iterator();
        while (it.hasNext()) {
            TimeEntries t = it.next();
            if (GeniousHelper.getDateDay(t.getSpentOn()) == filterDay && GeniousHelper.getDateWeek(t.getSpentOn()) == getFilterWeek() && t.getUser().equals(filterUser)) {
                tmp.add(t);
            }
        }
        return tmp;
    }

    /**
	 * Get Entries for the current filter (date, user)
	 * @return
	 */
    public ArrayList<TimeEntriesModel> getTableEntries() {
        ArrayList<TimeEntriesModel> tmpEntries = new ArrayList<TimeEntriesModel>();
        for (int i = 0; i < 7; i++) {
            List<TimeEntries> dayList = getEntriesForWeekDay(i + 1);
            for (int b = 0; b < dayList.size(); b++) {
                if (tmpEntries.size() < b + 1) {
                    tmpEntries.add(new TimeEntriesModel());
                }
                tmpEntries.get(b).setEntryForDay(dayList.get(b), i);
            }
        }
        return tmpEntries;
    }

    public List getSQLEntries() {
        Date[] filterDates = GeniousHelper.getWeekFilterDates(filterDate);
        Expression exp = ExpressionFactory.matchExp("user", filterUser);
        exp = exp.andExp(ExpressionFactory.matchExp("issue", this));
        exp = exp.andExp(ExpressionFactory.betweenExp("spentOn", filterDates[0], filterDates[1]));
        SelectQuery query = new SelectQuery(TimeEntries.class, exp);
        List<TimeEntries> entries = this.getDataContext().performQuery(query);
        return entries;
    }
}
