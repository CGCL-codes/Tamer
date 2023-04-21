package nz.ac.vuw.ecs.kcassell.callgraph.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.table.AbstractTableModel;

/** A table where each row contains the metric data for a class. */
class MetricsTableModel extends AbstractTableModel {

    protected static final long serialVersionUID = 6567001073132109312L;

    /** The class name followed by the various metrics. */
    protected String[] columnNames = null;

    /** Each row contains the metric data for a class. */
    protected Object[][] data = null;

    /** The Eclipse handles allow access to the code entities corresponding
	 * to the data rows in the table. */
    protected String[] handles = null;

    /**
	 * @return the columnNames
	 */
    public String[] getColumnNames() {
        return columnNames;
    }

    /**
	 * @param columnNames
	 *            the columnNames to set
	 */
    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    /**
	 * @return the data
	 */
    public Object[][] getData() {
        return data;
    }

    /**
	 * @param data
	 *            the data to set
	 */
    public void setData(Object[][] data) {
        this.data = data;
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return data.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Class<? extends Object> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public String[] getHandles() {
        return handles;
    }

    public void setHandles(String[] handles) {
        this.handles = handles;
    }

    /**
	 * Prints the metrics table to a comma-separated-value (CSV) file.
	 * @param file the file location
	 * @throws IOException
	 */
    public void tableToCSVFile(File file) throws IOException {
        String SEP = ",";
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
        PrintWriter printWriter = new PrintWriter(bufferedWriter);
        int columnCount = getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            printWriter.write(getColumnName(i));
            if (i < columnCount - 1) {
                printWriter.write(SEP);
            }
        }
        printWriter.write("\n");
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < columnCount; j++) {
                printWriter.write(getValueAt(i, j).toString());
                if (j < columnCount - 1) {
                    printWriter.write(SEP);
                }
            }
            printWriter.write("\n");
        }
        printWriter.close();
    }
}
