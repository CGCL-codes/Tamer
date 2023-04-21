package org.quickfix.banzai.ui;

import org.quickfix.banzai.ExecutionTableModel;
import org.quickfix.banzai.Execution;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;

public class ExecutionTable extends JTable {

    public ExecutionTable(ExecutionTableModel executionTableModel) {
        super(executionTableModel);
    }

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Execution execution = (Execution) ((ExecutionTableModel) dataModel).getExecution(row);
        DefaultTableCellRenderer r = (DefaultTableCellRenderer) renderer;
        r.setForeground(Color.black);
        if ((row % 2) == 0) r.setBackground(Color.white); else r.setBackground(Color.lightGray);
        return super.prepareRenderer(renderer, row, column);
    }
}
