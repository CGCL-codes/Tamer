package org.rapla.components.treetable;

import javax.swing.JTable;

public interface TableToolTipRenderer {

    public String getToolTipText(JTable table, int row, int column);
}
