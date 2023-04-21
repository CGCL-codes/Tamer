package com.ek.mitapp.ui.table;

import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import com.ek.mitapp.*;

/**
 * TODO: Class description.
 * <br>
 * Id: $Id$
 * 
 * @author dirwin
 */
public class TimePeriodRenderer extends JComboBox implements TableCellRenderer {

    /**
     * Default no-argument constructor.
     */
    public TimePeriodRenderer() {
        super();
        addItem(IntersectionData.PeakPeriod.AM);
        addItem(IntersectionData.PeakPeriod.PM);
    }

    /** 
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value.equals(IntersectionData.PeakPeriod.AM.toString())) {
            setSelectedIndex(0);
        } else if (value.equals(IntersectionData.PeakPeriod.PM.toString())) {
            setSelectedIndex(1);
        } else {
            System.out.println("Unsupported class");
        }
        return this;
    }
}
