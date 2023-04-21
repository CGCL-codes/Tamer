package net.sourceforge.volunteer.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import net.sourceforge.volunteer.ui.TableRowModel;

/**
 * {description}
 * 
 * @author Vasiliy Gagin
 * 
 * @version $Revision$
 * @param <R> record type
 */
public class BetterTableModel<R> extends AbstractTableModel {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    /**
     * The <code>Vector</code> of <code>Vectors</code> of <code>Object</code> values.
     */
    private List<R> records = new ArrayList<R>();

    private final TableRowModel<R> tableRowModel;

    private final R newRecord;

    /**
     * Constructs a <code>DefaultTableModel</code> and initializes the table by passing <code>data</code> and
     * <code>columnNames</code> to the <code>setDataVector</code> method. The first index in the <code>Object[][]</code> array is
     * the row index and the second is the column index.
     * @param tableRowModel 
     * @param newRecord 
     */
    public BetterTableModel(TableRowModel<R> tableRowModel, R newRecord) {
        this.tableRowModel = tableRowModel;
        this.newRecord = newRecord;
    }

    /**
     * @param activities the dataVector to set
     */
    public void setRecords(List<R> activities) {
        if (activities == null) {
            activities = new ArrayList<R>();
        }
        this.records = activities;
        fireTableStructureChanged();
    }

    /**
     * Adds a row to the end of the model. The new row will contain <code>null</code> values unless <code>rowData</code> is
     * specified. Notification of the row being added will be generated.
     * 
     * @param rowData optional data of the row being added
     */
    public void addRow(R rowData) {
        int row = records.size();
        records.add(rowData);
        fireTableRowsInserted(row, row);
    }

    /**
     * Inserts a row at <code>row</code> in the model. The new row will contain <code>null</code> values unless
     * <code>rowData</code> is specified. Notification of the row being added will be generated.
     * 
     * @param row the row index of the row to be inserted
     * @param rowData optional data of the row being added
     * @exception ArrayIndexOutOfBoundsException if the row was invalid
     */
    public void insertRow(int row, R rowData) {
        records.add(row, rowData);
        fireTableRowsInserted(row, row);
    }

    private static int gcd(int i, int j) {
        return (j == 0) ? i : gcd(j, i % j);
    }

    private static <R> void rotate(List<R> dataVector2, int a, int b, int shift) {
        int size = b - a;
        int r = size - shift;
        int g = gcd(size, r);
        for (int i = 0; i < g; i++) {
            int to = i;
            R tmp = dataVector2.get(a + to);
            for (int from = (to + r) % size; from != i; from = (to + r) % size) {
                dataVector2.set(a + to, dataVector2.get(a + from));
                to = from;
            }
            dataVector2.set(a + to, tmp);
        }
    }

    /**
     * Moves one or more rows from the inclusive range <code>start</code> to <code>end</code> to the <code>to</code> position in
     * the model. After the move, the row that was at index <code>start</code> will be at index <code>to</code>. This method will
     * send a <code>tableChanged</code> notification message to all the listeners. <p>
     * 
     * <pre> Examples of moves: <p> 1. moveRow(1,3,5); a|B|C|D|e|f|g|h|i|j|k - before a|e|f|g|h|B|C|D|i|j|k - after <p> 2.
     * moveRow(6,7,1); a|b|c|d|e|f|G|H|i|j|k - before a|G|H|b|c|d|e|f|i|j|k - after <p> </pre>
     * 
     * @param start the starting row index to be moved
     * @param end the ending row index to be moved
     * @param to the destination of the rows to be moved
     * @exception ArrayIndexOutOfBoundsException if any of the elements would be moved out of the table's range
     * 
     */
    public void moveRow(int start, int end, int to) {
        int shift = to - start;
        int first, last;
        if (shift < 0) {
            first = to;
            last = end;
        } else {
            first = start;
            last = to + end - start;
        }
        rotate(records, first, last + 1, shift);
        fireTableRowsUpdated(first, last);
    }

    /**
     * Removes the row at <code>row</code> from the model. Notification of the row being removed will be sent to all the
     * listeners.
     * 
     * @param row the row index of the row to be removed
     * @exception ArrayIndexOutOfBoundsException if the row was invalid
     */
    public void removeRow(int row) {
        records.remove(row);
        fireTableRowsDeleted(row, row);
    }

    /**
     * Returns the number of rows in this data table.
     * 
     * @return the number of rows in the model
     */
    public int getRowCount() {
        return records.size() + 1;
    }

    /**
     * Returns the number of columns in this data table.
     * 
     * @return the number of columns in the model
     */
    public int getColumnCount() {
        return tableRowModel.getPropertyCount();
    }

    /**
     * Returns the column name.
     * 
     * @return a name for this column using the string value of the appropriate member in <code>columnIdentifiers</code>. If
     *         <code>columnIdentifiers</code> does not have an entry for this index, returns the default name provided by the
     *         superclass.
     */
    @Override
    public String getColumnName(int column) {
        throw new UnsupportedOperationException("Column model is expected to provide this information");
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns true regardless of parameter values.
     * 
     * @param row the row whose value is to be queried
     * @param column the column whose value is to be queried
     * @return true
     * @see #setValueAt
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        if (row == records.size()) {
            return column == 0;
        } else {
            return true;
        }
    }

    /**
     * Returns an attribute value for the cell at <code>row</code> and <code>column</code>.
     * 
     * @param row the row whose value is to be queried
     * @param column the column whose value is to be queried
     * @return the value Object at the specified cell
     * @exception ArrayIndexOutOfBoundsException if an invalid row or column was given
     */
    public Object getValueAt(int row, int column) {
        R record;
        if (row == records.size()) {
            record = newRecord;
        } else {
            record = records.get(row);
        }
        return tableRowModel.getProperty(record, column);
    }

    /**
     * Sets the object value for the cell at <code>column</code> and <code>row</code>. <code>aValue</code> is the new value. This
     * method will generate a <code>tableChanged</code> notification.
     * 
     * @param aValue the new value; this can be null
     * @param row the row whose value is to be changed
     * @param column the column whose value is to be changed
     * @exception ArrayIndexOutOfBoundsException if an invalid row or column was given
     */
    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (row == records.size()) {
            @SuppressWarnings("unchecked") Class<R> recordType = (Class<R>) newRecord.getClass();
            R record = createRecord(recordType);
            tableRowModel.setProperty(record, column, aValue);
            records.add(record);
            fireTableRowsUpdated(row, row);
            fireTableRowsInserted(row + 1, row + 1);
        } else {
            R activity = records.get(row);
            tableRowModel.setProperty(activity, column, aValue);
            fireTableCellUpdated(row, column);
        }
    }

    /**
     * @param recordType
     * @return
     */
    private R createRecord(Class<R> recordType) {
        try {
            return recordType.newInstance();
        } catch (InstantiationException exc) {
            throw new RuntimeException("Can't create instance of record type [" + recordType + "]", exc);
        } catch (IllegalAccessException exc) {
            throw new RuntimeException("Can't create instance of record type [" + recordType + "]", exc);
        }
    }
}
