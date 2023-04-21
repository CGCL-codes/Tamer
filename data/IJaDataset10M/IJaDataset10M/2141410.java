package com.googlecode.avgas.expense.action;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import com.googlecode.avgas.core.model.CalendarCell;
import com.googlecode.avgas.core.model.CalendarGrid;
import com.googlecode.avgas.core.model.TimeInterval;
import com.googlecode.avgas.core.model.TimeIntervalType;
import com.googlecode.avgas.core.utils.CalendarUtils;
import com.googlecode.avgas.expense.model.Expense;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

/**
 * Create Date: 2009/6/1
 *
 * @author Alan She
 */
public class ExpenseCalendarAction extends BaseExpenseAction {

    private TimeInterval timeInterval;

    private CalendarGrid calendarGrid;

    private double monthlyTotal;

    public TimeInterval getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(TimeInterval timeInterval) {
        this.timeInterval = timeInterval;
    }

    public CalendarGrid getCalendarGrid() {
        return calendarGrid;
    }

    public double getMonthlyTotal() {
        return monthlyTotal;
    }

    public Resolution show() {
        if (timeInterval == null) {
            Calendar c = CalendarUtils.getCleanDateCalendar();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            timeInterval = TimeIntervalType.convert("bm," + year + "," + month);
        }
        List<Expense> expenses = expenseDao.findExpenses(getCurrentUser(), timeInterval);
        expensesToCalendar(expenses);
        return new ForwardResolution("expenseCalendar.ftl");
    }

    private void expensesToCalendar(List<Expense> expenses) {
        calendarGrid = new CalendarGrid();
        Calendar c = CalendarUtils.getCleanDateCalendar();
        c.setTime(timeInterval.getStartTime());
        int firstDayOfWeek = c.getFirstDayOfWeek();
        while (c.get(Calendar.DAY_OF_WEEK) != firstDayOfWeek) {
            c.add(Calendar.DATE, -1);
        }
        for (int w = 0; w < 6; w++) {
            for (int d = 0; d < 7; d++) {
                Date date = c.getTime();
                CalendarCell cell = new CalendarCell(date);
                if (timeInterval.isDateBetweenInterval(date)) {
                    double dailyTotal = sumExpenses(expenses, date);
                    cell.setDisplayValue(true);
                    cell.setValue(dailyTotal);
                    monthlyTotal += dailyTotal;
                }
                calendarGrid.setCell(cell, w, d);
                c.add(Calendar.DATE, 1);
            }
        }
    }

    private double sumExpenses(List<Expense> expenses, Date date) {
        double total = 0;
        for (Expense e : expenses) {
            if (e.getTime().equals(date)) {
                total += e.getAmount();
            }
        }
        return total;
    }
}
