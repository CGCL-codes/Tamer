package org.ms150hams.trackem.ui.models;

import java.util.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.ms150hams.trackem.db.DatabaseChangedEvent;
import org.ms150hams.trackem.model.*;
import org.ms150hams.trackem.ui.*;

public class RsStatusModel extends AbstractTableModel implements EventHandler {

    String[] columnNames = { "Location", "Call Sign", "Status", "Riders Waiting" };

    RestStop[] restStops;

    int[] restStopQueue;

    private GuiGlue glue;

    public RsStatusModel(GuiGlue glue) {
        this.glue = glue;
        glue.reflector.addEventHandler(this);
        restStops = RestStop.allRestStops();
        handleTrackemEvent(new DatabaseChangedEvent());
    }

    public int getRowCount() {
        return restStops.length;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int arg0) {
        return columnNames[arg0];
    }

    public Class getColumnClass(int arg0) {
        return String.class;
    }

    public boolean isCellEditable(int arg0, int arg1) {
        return false;
    }

    public Object getValueAt(int row, int col) {
        switch(col) {
            case 0:
                return restStops[row].getName();
            case 1:
                return restStops[row].getCallsign();
            case 2:
                return StatusEvent.statusLables[restStops[row].getStatus()];
            case 3:
                return Integer.toString(restStopQueue[row]);
        }
        return null;
    }

    public void setValueAt(Object arg0, int arg1, int arg2) {
    }

    private ArrayList listeners = new ArrayList();

    public void handleTrackemEvent(Event ev) {
        if (ev instanceof DatabaseChangedEvent) {
            restStopQueue = glue.database.getRiderCountsAtRestStops();
            fireTableDataChanged();
        } else if (ev instanceof RestStopStatusEvent && ev.getStation() instanceof RestStop) {
            RestStop rs = (RestStop) ev.getStation();
            int row = rs.getNumber() - 1;
            fireTableRowsUpdated(row, row);
        } else if (ev instanceof ParticipantPickupRequest) {
            ParticipantPickupRequest ppr = (ParticipantPickupRequest) ev;
            if (ppr.getLocation() instanceof ObjectLocation) {
                ObjectLocation loc = (ObjectLocation) ppr.getLocation();
                if (loc.getObject() instanceof RestStop) {
                    int index = ((RestStop) loc.getObject()).getNumber() - 1;
                    restStopQueue[index]++;
                    fireTableRowsUpdated(index, index);
                }
            }
        } else if (ev instanceof ParticipantPickupCancel) {
            ParticipantPickupRequest ppr = ((ParticipantPickupCancel) ev).getOriginalRequest();
            if (ppr.getLocation() instanceof ObjectLocation) {
                ObjectLocation loc = (ObjectLocation) ppr.getLocation();
                if (loc.getObject() instanceof RestStop) {
                    int index = ((RestStop) loc.getObject()).getNumber() - 1;
                    restStopQueue[index]--;
                    if (restStopQueue[index] < 0) restStopQueue[index] = 0;
                    fireTableRowsUpdated(index, index);
                }
            }
        } else if (ev instanceof ParticipantTransport) {
            ParticipantTransport ppt = (ParticipantTransport) ev;
            if (ppt.getStation() instanceof RestStop) {
                int index = ((RestStop) ppt.getStation()).getNumber() - 1;
                restStopQueue[index]--;
                if (restStopQueue[index] < 0) restStopQueue[index] = 0;
                fireTableRowsUpdated(index, index);
            }
        }
    }
}
