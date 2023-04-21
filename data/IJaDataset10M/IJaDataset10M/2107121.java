package com.traxel.lumbermill.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.prefs.Preferences;
import javax.swing.JMenu;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import ucar.util.prefs.PreferencesExt;
import com.traxel.lumbermill.filter.FilterSetControl;
import com.traxel.lumbermill.log.LogControl;

public class TableControl implements ListSelectionListener {

    public static final String FIRST_SELECTED_EVENT = "First Selected Event", LAST_SELECTED_EVENT = "Last Selected Event";

    private final LogControl LOG_CONTROL;

    private final FilterSetControl FILTER_SET_CONTROL;

    private final ColumnSetControl COLUMN_SET_CONTROL;

    private final TableView TABLE_VIEW;

    private final TableStatus TABLE_STATUS = new TableStatus();

    private final Set LISTENERS = Collections.synchronizedSet(new HashSet());

    private Event _previousFirst;

    private Event _previousLast;

    public TableControl(PreferencesExt preferences) {
        FILTER_SET_CONTROL = new FilterSetControl((PreferencesExt) preferences.node("filter"));
        COLUMN_SET_CONTROL = new ColumnSetControl((PreferencesExt) preferences.node("columns"));
        LOG_CONTROL = new LogControl(preferences.node("log"));
        TABLE_VIEW = new TableView(LOG_CONTROL.getLog(), FILTER_SET_CONTROL.getFilterSet(), COLUMN_SET_CONTROL.getColumnSet());
        ListSelectionModel model;
        model = getView().getSelectionModel();
        model.addListSelectionListener(this);
    }

    TableView getView() {
        return TABLE_VIEW;
    }

    public LogControl getLogControl() {
        return LOG_CONTROL;
    }

    public FilterSetControl getFilterSetControl() {
        return FILTER_SET_CONTROL;
    }

    public TableStatus getTableStatus() {
        return TABLE_STATUS;
    }

    public JMenu getColumnMenu() {
        return COLUMN_SET_CONTROL.getMenu();
    }

    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            setElapsed();
        }
    }

    public void addPropertyListener(PropertyChangeListener l) {
        LISTENERS.add(l);
    }

    Event getFirstSelectedEvent() {
        return TABLE_VIEW.getFirstSelectedEvent();
    }

    Event getLastSelectedEvent() {
        return TABLE_VIEW.getLastSelectedEvent();
    }

    private synchronized void setElapsed() {
        boolean change = false;
        Event newFirst = getFirstSelectedEvent();
        Event newLast = getLastSelectedEvent();
        if (newFirst != _previousFirst && newFirst != null) {
            change = true;
            firePropertyChange(FIRST_SELECTED_EVENT, _previousFirst, newFirst);
            _previousFirst = newFirst;
        }
        if (newLast != _previousLast && newLast != null) {
            change = true;
            firePropertyChange(LAST_SELECTED_EVENT, _previousLast, newLast);
            _previousLast = newLast;
        }
        if (change) {
            TABLE_STATUS.setElapsed(newFirst, newLast);
        }
    }

    private void firePropertyChange(String property, Object oldValue, Object newValue) {
        synchronized (LISTENERS) {
            Iterator it;
            PropertyChangeListener listener;
            PropertyChangeEvent event;
            event = new PropertyChangeEvent(this, property, oldValue, newValue);
            it = LISTENERS.iterator();
            while (it.hasNext()) {
                listener = (PropertyChangeListener) it.next();
                listener.propertyChange(event);
            }
        }
    }
}
