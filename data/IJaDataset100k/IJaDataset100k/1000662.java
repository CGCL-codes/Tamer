package org.hswgt.teachingbox.gridworldeditor.gui;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

public class QValuePanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5424845598235187017L;

    QTable table = null;

    SpreadsheetRowHeader rowHeader;

    JScrollPane scrollPane;

    public QValuePanel() {
        this.setLayout(new BorderLayout());
        table = new QTable(this);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setEnabled(false);
        rowHeader = new SpreadsheetRowHeader(table);
        scrollPane = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setRowHeaderView(rowHeader);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
	 * Returns the row header table of the grid table
	 * @return row header table
	 */
    public SpreadsheetRowHeader getRowHeader() {
        return rowHeader;
    }

    /**
	 * Returns the grid table
	 * @return grid table
	 */
    public QTable getQTable() {
        return table;
    }
}
