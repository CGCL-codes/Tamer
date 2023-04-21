package uk.org.sgj.YAT.Tests;

import javax.swing.*;
import javax.swing.table.*;

class TestResultsTable extends JTable {

    TestResultsTable(TestResultsTableModel model) {
        super(model);
        setColWidths();
    }

    private void setColWidths() {
        TableColumn column = null;
        for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
            column = getColumnModel().getColumn(i);
            if ((i > 0) && (i < 5)) {
                column.setPreferredWidth(50);
                column.setMaxWidth(75);
            } else if (i == 0) {
                column.setPreferredWidth(200);
                column.setMinWidth(100);
            } else if (i == 5) {
                column.setPreferredWidth(100);
                column.setMinWidth(50);
            }
        }
    }
}
