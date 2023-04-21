package oracle.otnsamples.jdbc.longraw;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 * This class maintains the data required for a JTable handling.
 * It can be used as the table model, by any application using JTables.
 * It encapsulates the maintenance of the JTable data, and also provides a
 * few additional member functions:
 *    1) populateFromResultSet: takes the result set from a query and
 *       handles the population of the table data from the data query.
 *    2) insertRow: takes a vector containing a new row values, and
 *       creates a new row in the table.
 *    3) deleteRow: deletes the row specified from the displayed rows.
 *    4) getRow:    returns a vector containing the row data.
 *    5) updateRow: Replaces the vector present in the row number specfied with
 *       the new vector passed.
 *    6) clearTable: Clears table data
 *
 * The data itself is maintained in a vector, and hence the table data is
 * maintained efficiently, as the vector can grow and shrink as and when the
 * table data changes.
 *
 * The constructor takes an array of columnNames, number of rows to be created
 * initially. Also it takes an array of default value object which may be
 * heterogeneous. This helps in deciding the column type.
 *
 * If more functionality is required, like setting columns as non-editable
 * and changing cell-renderers, this class can be extended.
 */
public class GenTableModel extends AbstractTableModel {

    /** Holds the table data */
    Vector data;

    /** Holds the column names */
    String[] columnNames;

    /**
   * Constructor: Initializes the table structure, including nuumber of columns
   * and column headings. Also initializes table data with default values.
   * Parameters-  columns: array of column titles.
   *              defaultv: array of default value objects for each column
   *              rows: number of rows initially
   **/
    public GenTableModel(String columns[], Object defaultv[], int rows) {
        columnNames = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            columnNames[i] = new String(columns[i]);
        }
        data = new Vector(rows);
        for (int i = 0; i < rows; i++) {
            Vector cols = new Vector();
            for (int j = 0; j < columns.length; j++) {
                cols.addElement(defaultv[j]);
            }
            data.addElement(cols);
        }
    }

    /**
   * Repopulates the table data. The table is populated with the rows returned
   * by the ResultSet
   **/
    private void populateFromResultSet(ResultSet rset) {
        data = new Vector();
        try {
            while (rset.next()) {
                Vector cols = new Vector();
                for (int i = 0; i < columnNames.length; i++) {
                    cols.addElement(rset.getObject(i + 1));
                }
                data.addElement(cols);
            }
            rset.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        super.fireTableDataChanged();
    }

    /**
   *  Overrides AbstractTableModel method. Returns the number of columns in table
   */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
   * Overrides AbstractTableModel method. Returns the number of rows in table
   */
    public int getRowCount() {
        return data.size();
    }

    /**
   * Overrides AbstractTableModel method. Returns the column name for the
   * specified column
   */
    public String getColumnName(int col) {
        return columnNames[col];
    }

    /**
   * Overrides AbstractTableModel method. Returns the value at the specified cell
   */
    public Object getValueAt(int row, int col) {
        Vector colvector = (Vector) data.elementAt(row);
        return colvector.elementAt(col);
    }

    /**
   * Overrides AbstractTableModel method. Returns the class for the
   * specified column
   */
    public Class getColumnClass(int col) {
        return getValueAt(0, col).getClass();
    }

    /**
   * Overrides AbstractTableModel method. Sets the value at the specified cell
   * to obj
   */
    public void setValueAt(Object obj, int row, int col) {
        Vector colvector = (Vector) data.elementAt(row);
        colvector.setElementAt(obj, col);
    }

    /**
   * Adds a new row to the table
   */
    public void insertRow(Vector newrow) {
        data.addElement(newrow);
        super.fireTableDataChanged();
    }

    /**
   * Deletes the specified row from the table
   */
    private void deleteRow(int row) {
        data.removeElementAt(row);
        super.fireTableDataChanged();
    }

    /**
   * Returns the values at the specified row as a vector
   */
    private Vector getRow(int row) {
        return (Vector) data.elementAt(row);
    }

    /**
   * Updates the specified row. It replaces the row vector at the specified
   * row with the new vector.
   */
    private void updateRow(Vector updatedRow, int row) {
        data.setElementAt(updatedRow, row);
        super.fireTableDataChanged();
    }

    /**
   * Clears the table data
   */
    public void clearTable() {
        data = new Vector();
        super.fireTableDataChanged();
    }
}
