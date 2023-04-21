package com.traxel.lumbermill;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import ucar.util.prefs.PreferencesExt;
import com.traxel.PreferencesUtils;
import com.traxel.lumbermill.event.EventsPanel;
import com.traxel.lumbermill.event.EventListenerStatus;
import com.traxel.lumbermill.event.TableStatus;
import com.traxel.lumbermill.filter.FilterSetView;
import com.traxel.lumbermill.log.LogActiveStateView;
import com.traxel.lumbermill.log.LogStatus;

public class MillView extends JPanel {

    public MillView(LogActiveStateView logActiveStateView, LogStatus logStatus, TableStatus tableStatus, EventsPanel eventsPanel, FilterSetView filterSetView, final PreferencesExt preferences) {
        JPanel logStateStatusPanel;
        logStateStatusPanel = new JPanel();
        logStateStatusPanel.add(logActiveStateView);
        logStateStatusPanel.add(logStatus);
        logStateStatusPanel.add(tableStatus);
        final JSplitPane treeEventsSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, filterSetView, eventsPanel);
        setLayout(new BorderLayout());
        add(BorderLayout.NORTH, logStateStatusPanel);
        add(BorderLayout.CENTER, treeEventsSplit);
        PreferencesUtils.register(treeEventsSplit, preferences);
    }

    public void setEventListenerStatus(EventListenerStatus status) {
        add(BorderLayout.SOUTH, status);
    }
}
