package com.tirsen.hanoi.designer;

import com.tirsen.hanoi.builder.transition.*;
import com.tirsen.hanoi.engine.Activity;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.Arrays;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 *
 * <!-- $Id: ActivityTableModel.java,v 1.6 2002/08/13 14:52:39 tirsen Exp $ -->
 * <!-- $Author: tirsen $ -->
 *
 * @author Jon Tirs&eacute;n (tirsen@users.sourceforge.net)
 * @version $Revision: 1.6 $
 */
public class ActivityTableModel extends AbstractTableModel implements TransitionBuilderListener {

    private static final Log logger = LogFactory.getLog(ActivityTableModel.class);

    private TransitionBuilder builder;

    private Activity[] activities;

    public ActivityTableModel() {
    }

    public void setBuilder(TransitionBuilder newBuilder) {
        if (builder != null) builder.removeTransitionBuilderListener(this);
        builder = newBuilder;
        if (newBuilder != null) {
            newBuilder.addTransitionBuilderListener(this);
            activities = newBuilder.getActivities();
        } else {
            activities = new Activity[0];
        }
        fireTableDataChanged();
    }

    public Activity[] getActivities() {
        return activities;
    }

    public int getRowCount() {
        return activities == null ? 0 : activities.length;
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int column) {
        switch(column) {
            case 0:
                return "ID";
            case 1:
                return "Class";
        }
        return super.getColumnName(column);
    }

    public Class getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Class.class;
        }
        return super.getColumnClass(columnIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Activity activity = activities[rowIndex];
        switch(columnIndex) {
            case 0:
                return activity.getId();
            case 1:
                return activity.getClass();
        }
        return null;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Activity activity = activities[rowIndex];
        switch(columnIndex) {
            case 0:
                activity.setId((String) aValue);
                break;
            case 1:
                {
                    builder.changeClass(activity, (Class) aValue);
                    break;
                }
            default:
                assert false;
        }
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 1;
    }

    public void builderChanged(TransitionBuilderEvent event) {
        if (event.getType() == TransitionBuilderEvent.ACTIVITY_ADDED) {
            activities = builder.getActivities();
            logger.debug("adding activity table row");
            fireTableRowsInserted(event.getIndex(), event.getIndex());
        } else if (event.getType() == TransitionBuilderEvent.ACTIVITY_REMOVED) {
            activities = builder.getActivities();
            fireTableRowsDeleted(event.getIndex(), event.getIndex());
        }
    }
}
