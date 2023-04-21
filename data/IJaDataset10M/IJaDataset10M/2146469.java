package org.digitall.projects.gdigitall.lib.components;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Observable;

public class ComboItemListener extends Observable implements ItemListener {

    ComboItemListener(JCalendario calendar) {
        this.calendar = calendar;
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == 1) {
            int month = calendar.monthCombo.getSelectedIndex();
            calendar.setMonth(new Integer(month + 1).toString());
            Integer year = (Integer) calendar.yearCombo.getSelectedItem();
            calendar.setYear(calendar.yearCombo.getSelectedItem().toString());
            calendar.showCalendarForDate(year.intValue(), month + 1);
            calendar.initializeCalendar();
            calendar.updateUI();
        }
    }

    JCalendario calendar;
}
