package com.anthonyeden.lib.gui;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.Calendar;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

/** Panel for selecting a date (month, day, year).

    @author Anthony Eden
*/
public class DateSelectPanel extends JPanel {

    private final DateFormat df = new SimpleDateFormat("MMMMM-dd-yyyy");

    private final String[] years = { "1999", "2000", "2001", "2002" };

    private Integer[] days;

    private JComboBox monthCombo;

    private JComboBox dayCombo;

    private JComboBox yearCombo;

    /** Construct a new DateSelectPanel. */
    public DateSelectPanel() {
        init();
    }

    /** Set the list of years which are displayed in the combo box.
    
        @param years
    */
    public void setYears(String[] years) {
        yearCombo.setModel(new DefaultComboBoxModel(years));
    }

    /** Return the Date represented by the date selected in the panel.
    
        @return The Date
        @throws ParseException Thrown if there is an error parsing the date
    */
    public Date getDate() throws ParseException {
        StringBuffer buffer = new StringBuffer();
        buffer.append(monthCombo.getSelectedItem().toString());
        buffer.append("-");
        buffer.append(dayCombo.getSelectedItem().toString());
        buffer.append("-");
        buffer.append(yearCombo.getSelectedItem().toString());
        return df.parse(buffer.toString());
    }

    /** Set the date selected in the panel.
    
        @param date The new Date
    */
    public void setDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        dayCombo.setSelectedIndex(c.get(Calendar.DAY_OF_MONTH) - 1);
        monthCombo.setSelectedIndex(c.get(Calendar.MONTH));
        yearCombo.setSelectedItem(Integer.toString(c.get(Calendar.YEAR)));
    }

    /** Initialize the panel's UI. */
    private void init() {
        DateFormatSymbols symbols = new DateFormatSymbols();
        monthCombo = new JComboBox(symbols.getMonths());
        add(monthCombo);
        dayCombo = new JComboBox(getDays());
        add(dayCombo);
        yearCombo = new JComboBox(years);
        add(yearCombo);
        setDate(new Date());
    }

    /** Get an array of Integer objects from 1 to 31.
    
        @return An array of days
    */
    private Integer[] getDays() {
        if (days == null) {
            days = new Integer[31];
            for (int i = 1; i <= 31; i++) {
                days[i - 1] = new Integer(i);
            }
        }
        return days;
    }
}
