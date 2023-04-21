package geovista.category;

import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class ConfigureTableModel extends DefaultTableModel {

    boolean[] editables;

    Class[] types;

    protected static final Logger logger = Logger.getLogger(ConfigureTableModel.class.getName());

    public ConfigureTableModel() {
    }

    /**
     *  Constructs a <code>DefaultTableModel</code> and initializes the table
     *  by passing <code>data</code> and <code>columnNames</code>
     *  to the <code>setDataVector</code>
     *  method. The first index in the <code>Object[][]</code> array is
     *  the row index and the second is the column index.
     *
     * @param data              the data of the table
     * @param columnNames       the names of the columns
     */
    public ConfigureTableModel(Object[][] data, String[] columnNames) {
        super(data, columnNames);
    }

    public ConfigureTableModel(Vector columnNames, int rowCount) {
        super(columnNames, rowCount);
    }

    /**
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the Boolean column would contain text ("true"/"false"),
         * rather than a check box.
         */
    public Class getColumnClass(int c) {
        if (types != null) return types[c];
        Object o = getValueAt(0, c);
        if (o != null) {
            return o.getClass();
        } else {
            return super.getColumnClass(c);
        }
    }

    /**
         * Don't need to implement this method unless your table's
         * editable.
         */
    public boolean isCellEditable(int row, int col) {
        if (editables != null && col < editables.length) {
            return editables[col];
        } else return false;
    }

    public void setEditables(boolean[] editables) {
        this.editables = editables;
    }

    /**
         * set data type of each column
         * @param types
         */
    public void setTypes(Class[] types) {
        this.types = types;
    }

    public Object[] getColumnIdentifiers() {
        return columnIdentifiers.toArray();
    }

    public int getColumnIndex(String name) {
        return this.columnIdentifiers.indexOf(name);
    }

    public Object[] getRowData(int row) {
        Vector vrow = ((Vector) this.dataVector.get(row));
        Object[] arow = vrow.toArray();
        return arow;
    }

    public void setValueAt(Object aValue, int row, int column) {
        super.setValueAt(aValue, row, column);
        int rowCount2 = this.getRowCount();
        logger.finest("row count:" + rowCount2);
    }
}
