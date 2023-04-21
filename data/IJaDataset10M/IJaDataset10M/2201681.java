package com.xtech.common.ui;

import java.awt.Color;
import java.awt.Component;
import java.util.Date;
import java.util.Hashtable;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import com.xtech.common.RightHand;
import com.xtech.common.entities.ProjectTask;

/**
 * @author jscruz
 * @since XERP
 */
public class ProjectTaskCellRenderer extends DefaultTableCellRenderer {

    EntitiesModel model;

    Hashtable bgColors;

    Hashtable fgColors;

    public ProjectTaskCellRenderer(EntitiesModel model) {
        this.model = model;
        bgColors = new Hashtable();
        fgColors = new Hashtable();
        bgColors.put(new Integer(ProjectTask.STATUS_NONE), Color.LIGHT_GRAY);
        bgColors.put(new Integer(ProjectTask.STATUS_PENDING), ColorSuite.SOFT_YELLOW);
        fgColors.put(new Integer(ProjectTask.STATUS_PENDING), Color.BLACK);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof Date) {
            Date d = (Date) value;
            value = RightHand.dateFormat.format(d);
        }
        JComponent c = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        ProjectTask task = (ProjectTask) model.getEntityAt(model.sort2Model(row));
        c.setOpaque(true);
        c.setToolTipText(task.getName());
        Integer i = new Integer(task.getStatus());
        if (bgColors.containsKey(i)) {
            c.setBackground((Color) bgColors.get(i));
        } else {
            c.setBackground(table.getBackground());
        }
        if (fgColors.containsKey(i)) {
            c.setForeground((Color) fgColors.get(i));
        } else {
            c.setForeground(table.getForeground());
        }
        if (isSelected) {
            c.setBackground(c.getBackground().darker());
        }
        return c;
    }
}
