package com.electionpredictor.ui.table;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 * Table for presenting data
 * 
 * @author Niels Stchedroff
 */
public class DataTable extends JTable {

    /**
     * 
     */
    private static final long serialVersionUID = 9126861891989797133L;

    /**
	 * Constructor
	 * 
	 * @param pTest
	 */
    public DataTable(final DataTableModel pTest) {
        super(pTest);
    }

    /**
	 * Get the model height
	 * 
	 * @return The height
	 */
    public int getModelHeight() {
        final int rowCount = getModel().getRowCount();
        return rowCount * this.getRowHeight();
    }

    /**
	 * Make the table columns fit the headings
	 */
    protected int fitColumnToHeaders() {
        final int spacing = 12;
        final TableModel theModel = getModel();
        final int columnCount = theModel.getColumnCount();
        final int widths[] = new int[theModel.getColumnCount()];
        int width = 0;
        for (int counter = 0; counter < columnCount; counter++) {
            widths[counter] = (int) getDefaultRenderer(String.class).getTableCellRendererComponent(this, getColumnName(counter), false, false, -1, counter).getPreferredSize().getWidth() + spacing;
            width = width + widths[counter];
        }
        final TableColumnModel columnModel = getColumnModel();
        for (int counter = 0; counter < columnCount; counter++) {
            columnModel.getColumn(counter).setPreferredWidth(widths[counter]);
        }
        return width;
    }
}
