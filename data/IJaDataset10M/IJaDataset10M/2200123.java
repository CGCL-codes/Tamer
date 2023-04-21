package org.eledge.domain.recurrence;

import static org.eledge.Eledge.currentEngine;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.eledge.components.SelectableClass;
import org.eledge.domain.Assignment;
import org.eledge.domain.AssignmentDate;
import org.eledge.domain.User;

/**
 * @author robertz
 * 
 */
public class WeeklyRangeRecurrenceStrategy implements RecurrenceStrategy, SelectableClass {

    List<Integer> selectedDays;

    public WeeklyRangeRecurrenceStrategy() {
        selectedDays = new ArrayList<Integer>(7);
    }

    public void initialize(String initString) {
        selectedDays.clear();
        String[] days = initString.split(",");
        for (String element : days) {
            if (element == null || element.equals("")) {
                continue;
            }
            selectedDays.add(Integer.valueOf(element));
        }
    }

    public String save() {
        StringBuffer buf = new StringBuffer();
        for (Integer integer : selectedDays) {
            buf.append(integer).append(",");
        }
        return buf.toString();
    }

    private void setCalendar(int month, int day, int year, int hour, int minute, int second, Calendar c) {
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
    }

    private void setCalendar(int hour, int minute, int second, Calendar c) {
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
    }

    public AssignmentDate applicableAssignmentDate(AssignmentDate date, Date currentDate) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        Date availableDate;
        Date dueDate;
        if (date == null) {
            return null;
        }
        if (selectedDays.isEmpty()) {
            return date;
        }
        if (c.getTime().after(date.getDateDue())) {
            dueDate = date.getDateDue();
            c.setTime(dueDate);
            c.add(Calendar.DAY_OF_WEEK, -1);
            if (findLastApplicableDay(c)) {
                c.add(Calendar.DAY_OF_WEEK, 1);
                setCalendar(0, 0, 0, c);
            } else {
                c.setTime(date.getDateAvailable());
            }
            availableDate = c.getTime();
        } else if (c.getTime().before(date.getDateAvailable())) {
            availableDate = date.getDateAvailable();
            c.setTime(availableDate);
            if (findFirstApplicableDay(c)) {
                setCalendar(23, 59, 59, c);
            } else {
                c.setTime(date.getDateDue());
            }
            dueDate = c.getTime();
        } else {
            if (findFirstApplicableDay(c)) {
                setCalendar(23, 59, 59, c);
            } else {
                c.setTime(date.getDateDue());
            }
            dueDate = c.getTime();
            c.add(Calendar.DAY_OF_WEEK, -1);
            if (findLastApplicableDay(c)) {
                c.add(Calendar.DAY_OF_WEEK, 1);
            } else {
                c.setTime(date.getDateAvailable());
            }
            setCalendar(0, 0, 0, c);
            availableDate = c.getTime();
        }
        if (availableDate.before(date.getDateAvailable())) {
            availableDate = date.getDateAvailable();
        }
        if (dueDate.after(date.getDateDue())) {
            dueDate = date.getDateDue();
        }
        AssignmentDate ret = new AssignmentDate();
        ret.setDateDue(dueDate);
        ret.setDateAvailable(availableDate);
        return ret;
    }

    private boolean findFirstApplicableDay(Calendar c) {
        int i = c.get(Calendar.DAY_OF_WEEK);
        int start = i;
        if (!selectedDays.isEmpty()) {
            while (true) {
                if (selectedDays.contains(new Integer(i - 1))) {
                    return true;
                }
                c.add(Calendar.DAY_OF_WEEK, 1);
                i = c.get(Calendar.DAY_OF_WEEK);
                if (i == start) {
                    break;
                }
            }
        }
        return false;
    }

    private boolean findLastApplicableDay(Calendar c) {
        int i = c.get(Calendar.DAY_OF_WEEK);
        int start = i;
        while (true) {
            if (selectedDays.contains(new Integer(i - 1))) {
                return true;
            }
            c.add(Calendar.DAY_OF_WEEK, -1);
            i = c.get(Calendar.DAY_OF_WEEK);
            if (i == start) {
                break;
            }
        }
        return false;
    }

    public boolean assignmentDoable(User u, Date currentDate, Assignment a) {
        AssignmentDate d = a.lookupAssignmentDate(u);
        return currentDate.before(d.getDateDue()) && currentDate.after(d.getDateAvailable()) && RecurrenceUtils.passesMultipleAttemptsCheck(u, currentDate, a);
    }

    private int getIndex(Object o) {
        return ((Integer) o).intValue();
    }

    public IPropertySelectionModel getPropertySelectionModel() {
        return new LocalizedWeeklyRecurrenceModel(currentEngine().getLocale());
    }

    public List<Integer> getSelectedOptions() {
        return selectedDays;
    }

    public String getExplanation() {
        return "The journal assignment will be due every week on the days " + "selected from the date available up until the " + "assignment due date.  It will be available from the day after a due date up to the day of a due date." + " This differs from the normal \"Weekly\" strategy in which the journal is available only on the day it is due.<br />" + "Select days of the week for a journal submission to be due:";
    }

    public String getDisplayName() {
        return "Weekly - eg: sometime before Friday night";
    }
}
