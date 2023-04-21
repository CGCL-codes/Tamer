package com.jcalendar.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;

/**
 * The Class JCalModel.
 * 
 * @author Suman
 */
public class JCalModel extends AbstractTableModel implements Serializable {

    /** Serial version UID for model. */
    private static final long serialVersionUID = 6985678097099535135L;

    /** The model list. */
    private ArrayList<String> modelList;

    /** The model. */
    private GregorianCalendar model;

    /** The current day. */
    private int currentMonth = -1, currentYear = -1, currentDay = -1;

    /** The month list. */
    private String[] monthList = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    /** The year model. */
    private SpinnerModel yearModel;

    /**
	 * Instantiates a new j cal model.
	 */
    public JCalModel() {
        model = (GregorianCalendar) Calendar.getInstance();
        initModel();
    }

    /**
	 * Instantiates a new j cal model.
	 * 
	 * @param timeZone
	 *            the time zone
	 */
    public JCalModel(TimeZone timeZone) {
        this();
        model = (GregorianCalendar) Calendar.getInstance(timeZone);
        initModel();
    }

    /**
	 * Returns the number of columns that needs to be displayed in the table.
	 * This method will always return 7.
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
    @Override
    public int getColumnCount() {
        return 7;
    }

    /**
	 * Returns the number of row that the calendar table needs to display. This
	 * method will always return 5
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
    @Override
    public int getRowCount() {
        return 5;
    }

    /**
	 * This method returns the value to be displayed in the Table from the
	 * calendar model.
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(rowIndex) {
            case 0:
                return modelList.get(columnIndex);
            case 1:
                return modelList.get(7 + columnIndex);
            case 2:
                return modelList.get(14 + columnIndex);
            case 3:
                return modelList.get(21 + columnIndex);
            case 4:
                return modelList.get(28 + columnIndex);
            default:
                return "";
        }
    }

    /**
	 * This method is overridden to return the column names that needs to be
	 * displayed.
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return "Sun";
            case 1:
                return "Mon";
            case 2:
                return "Tue";
            case 3:
                return "Wed";
            case 4:
                return "Thu";
            case 5:
                return "Fri";
            case 6:
                return "Sat";
            default:
                return "";
        }
    }

    /**
	 * Initializes the model.
	 */
    private void initModel() {
        modelList = new ArrayList<String>();
        currentMonth = model.get(Calendar.MONTH);
        currentYear = model.get(Calendar.YEAR);
        currentDay = model.get(Calendar.DAY_OF_MONTH);
        yearModel = new SpinnerNumberModel(currentYear, model.getMinimum(Calendar.YEAR), model.getMaximum(Calendar.YEAR), 1);
        String space = " ";
        int days = model.getActualMaximum(Calendar.DAY_OF_MONTH);
        int today = model.get(Calendar.DAY_OF_MONTH);
        model.set(Calendar.DAY_OF_MONTH, 1);
        int firstDay = model.get(Calendar.DAY_OF_WEEK);
        model.set(Calendar.DAY_OF_MONTH, today);
        int dayCount = 1;
        for (int i = 0; i < 37; i++) {
            if ((i + 1) < firstDay) {
                modelList.add(space);
            } else if (dayCount <= days) {
                modelList.add(new Integer(dayCount).toString());
                dayCount++;
            } else {
                modelList.add(space);
            }
        }
    }

    /**
	 * Gets the current month.
	 * 
	 * @return the current month
	 */
    public int getCurrentMonth() {
        return currentMonth;
    }

    /**
	 * Gets the current year.
	 * 
	 * @return the current year
	 */
    public int getCurrentYear() {
        return currentYear;
    }

    /**
	 * Gets the current day.
	 * 
	 * @return the current day
	 */
    public int getCurrentDay() {
        return currentDay;
    }

    /**
	 * Gets the current date.
	 * 
	 * @return the current date
	 */
    public Date getCurrentDate() {
        return model.getTime();
    }

    /**
	 * Re init model.
	 */
    public void reInitModel() {
        initModel();
        fireTableDataChanged();
    }

    /**
	 * Re init model.
	 * 
	 * @param month
	 *            the month
	 */
    public void reInitModel(int month) {
        model.set(Calendar.MONTH, month);
        initModel();
        fireTableDataChanged();
    }

    /**
	 * Re init model.
	 * 
	 * @param month
	 *            the month
	 * @param year
	 *            the year
	 */
    public void reInitModel(int month, int year) {
        model.set(Calendar.MONTH, month);
        model.set(Calendar.YEAR, year);
        initModel();
        fireTableDataChanged();
    }

    /**
	 * Sets the current month.
	 * 
	 * @param month
	 *            the new current month
	 */
    public void setCurrentMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid Month");
        } else {
            this.currentMonth = month;
        }
    }

    /**
	 * Sets the day.
	 * 
	 * @param day
	 *            the new day
	 */
    public void setDay(int day) {
        this.currentDay = day;
        model.set(Calendar.DAY_OF_MONTH, day);
    }

    /**
	 * Sets the current year.
	 * 
	 * @param year
	 *            the new current year
	 */
    public void setCurrentYear(int year) {
        this.currentYear = year;
    }

    /**
	 * Populate model list.
	 */
    public void populateModelList() {
        model.set(Calendar.MONTH, currentMonth);
        model.set(Calendar.YEAR, currentYear);
    }

    /**
	 * Gets the month list.
	 * 
	 * @return the month list
	 */
    public String[] getMonthList() {
        return monthList;
    }

    /**
	 * Gets the year model.
	 * 
	 * @return the year model
	 */
    public SpinnerModel getYearModel() {
        return yearModel;
    }
}
