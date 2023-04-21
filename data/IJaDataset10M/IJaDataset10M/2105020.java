package miko.gui;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

/**
 * @author orbital
 */
public class ImmutableDefaultTableModel extends DefaultTableModel {

    /**
	 * 
	 */
    public ImmutableDefaultTableModel() {
        super();
    }

    /**
	 * @param rowCount
	 * @param columnCount
	 */
    public ImmutableDefaultTableModel(int rowCount, int columnCount) {
        super(rowCount, columnCount);
    }

    /**
	 * @param columnNames
	 * @param rowCount
	 */
    public ImmutableDefaultTableModel(Object[] columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    /**
	 * @param data
	 * @param columnNames
	 */
    public ImmutableDefaultTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    /**
	 * @param columnNames
	 * @param rowCount
	 */
    public ImmutableDefaultTableModel(Vector columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    /**
	 * @param data
	 * @param columnNames
	 */
    public ImmutableDefaultTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
