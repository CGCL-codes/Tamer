package org.ekstrabilet.search.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.Time;

/**  
 * Class provides component which is used to choose Time with The use of Comboboxes
 * implements methods which returns time for sql-searching and printing
 */
public class TimeChooser {

    private JComboBox hourCB;

    private JComboBox minCB;

    private String[] hours = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" };

    private String[] mins = { "00", "15", "30", "45" };

    public TimeChooser() {
        hourCB = new JComboBox(hours);
        minCB = new JComboBox(mins);
    }

    /**  
	 * Returns Time from TimeChooser in String format
	 */
    public String getTime() {
        return (hourCB.getSelectedItem().toString() + ":" + minCB.getSelectedItem().toString() + ":00");
    }

    /**  
	 * Returns Time from TimeChooser in sql-style
	 */
    public Time getSqlTime() {
        int h = Integer.parseInt(hourCB.getSelectedItem().toString());
        int m = Integer.parseInt(minCB.getSelectedItem().toString());
        return new Time(h, m, 0);
    }

    /**  
	 * Returns TimeChooser component 
	 */
    public Box getTimeChooser() {
        Box timeChooser = Box.createHorizontalBox();
        timeChooser.add(hourCB);
        timeChooser.add(minCB);
        return timeChooser;
    }
}
