package org.isqlviewer.swing.table;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.isqlviewer.util.LoggableObject;

/**
 * An enhanced rendition of the TableModel interface.
 * <p>
 * This implementation of the TableModel interface mainly address the lack of some column oriented methods, also i liked
 * some of the methods provided by the default DefaultTableModel like removeRow addRow, etc.
 * <p>
 * This is a fairly good table mode to use if you need a fast WORM type table model, i do not recommend using this class
 * if you have to make lots of changes since the data storage is only synchronized when it is set or removed and not
 * read.
 * 
 * @author Markus A. Kobold &lt;mkobold at sprintpcs dot com&gt;
 * @version 1.0
 */
public class EnhancedTableModel extends LoggableObject implements Cloneable, Sortable, TableModel, Transferable {

    /**
     * Default transfer flavor as a java object.
     */
    public static final DataFlavor FLAVOR_LOCAL_OBJECT = new DataFlavor(EnhancedTableModel.class, "java/EnhancedTableModel");

    /**
     * Default transfer flavor as a java object.
     */
    public static final DataFlavor FLAVOR_REMOTE_OBJECT = new DataFlavor(EnhancedTableModel.class, DataFlavor.javaSerializedObjectMimeType);

    /**
     * Transfer flavor for representing this as tab strings.
     * <p>
     * This data flavor seems to make DnD to native apps like Excel work.
     */
    public static final DataFlavor FLAVOR_TAB_STRINGS = new DataFlavor(String.class, "text/tab-separated-values");

    /**
     * Transfer flavor for representing plain text.
     */
    public static final DataFlavor FLAVOR_PLAIN_TEXT = new DataFlavor(String.class, "text/plain");

    /**
     * The main data collection this should be an ArrayList where the elements are also ArrayLists.
     */
    protected ArrayList<ArrayList<Object>> dataStore = new ArrayList<ArrayList<Object>>();

    /**
     * This is the collection of column names required for the TableModel interface.
     */
    protected ArrayList<String> columns = new ArrayList<String>();

    /**
     * A list of TableModel listeners this will be lazily created when a table model listener is required.
     */
    protected transient ArrayList<TableModelListener> listeners = null;

    /**
     * A marker to determine the number of rows per page.
     */
    protected int pageSize = 1024 * 4;

    /**
     * Current page offset for use in paging methods.
     */
    protected int pageOffset = 0;

    /**
     * Mapping for column class by the column number.
     * <p>
     * This makes for easier explicit declarations of what class a column rather than based on what it is. This is
     * paticularly useful when used in conjuction of JTable that is basing a renderer on an interface class.
     */
    protected HashMap<Integer, Class> classMappings = new HashMap<Integer, Class>();

    private ArrayList<Integer> filteredRows = new ArrayList<Integer>();

    private String lastFilterPattern = null;

    /**
     * Default constructor for creating an empty table model.
     * <p>
     * This method will create an empty table model with no columns or rows of any kind.
     * 
     * @see #EnhancedTableModel(String[])
     */
    public EnhancedTableModel() {
        this(null);
    }

    /**
     * Creates an TableModel with the given column names.
     * <p>
     * Creates a table model with the following column names initially, The model will have no rows of any kind though.
     * <p>
     * This will also poll the SystemConfig object to automatically set the preferred paging size based on the
     * KEY_TABLE_PAGE_SIZE property key.
     * 
     * @param columns array of strings to be column names.
     */
    public EnhancedTableModel(String[] columns) {
        setColumns(columns);
    }

    /**
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (FLAVOR_LOCAL_OBJECT.equals(flavor)) return this; else if (DataFlavor.stringFlavor.equals(flavor) || FLAVOR_PLAIN_TEXT.equals(flavor) || FLAVOR_TAB_STRINGS.equals(flavor)) return toString(); else throw new UnsupportedFlavorException(flavor);
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { FLAVOR_REMOTE_OBJECT, FLAVOR_LOCAL_OBJECT, FLAVOR_TAB_STRINGS, FLAVOR_PLAIN_TEXT, DataFlavor.stringFlavor };
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return FLAVOR_LOCAL_OBJECT.equals(flavor) || FLAVOR_REMOTE_OBJECT.equals(flavor) || FLAVOR_TAB_STRINGS.equals(flavor) || DataFlavor.stringFlavor.equals(flavor) || FLAVOR_PLAIN_TEXT.equals(flavor);
    }

    /**
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        int realRow = translateRow(rowIndex);
        synchronized (dataStore) {
            ArrayList<Object> row = dataStore.get(realRow);
            row.set(columnIndex, aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    /**
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return columns.size();
    }

    /**
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        int rc = (filteredRows.isEmpty() ? dataStore.size() : filteredRows.size());
        if (rc <= getPageSize()) return rc; else if (getPageOffset() == getPageCount() - 1) {
            return rc - (getPageOffset() * getPageSize());
        } else {
            return getPageSize();
        }
    }

    /**
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        ArrayList row = getRow(rowIndex);
        return row.get(columnIndex);
    }

    /**
     * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
     */
    public synchronized void addTableModelListener(TableModelListener l) {
        if (listeners == null) {
            listeners = new ArrayList<TableModelListener>(1);
        }
        if (!listeners.contains(l)) {
            synchronized (listeners) {
                listeners.add(l);
            }
        }
    }

    /**
     * @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
     */
    public void removeTableModelListener(TableModelListener l) {
        if (listeners != null && listeners.contains(l)) {
            synchronized (listeners) {
                listeners.remove(l);
            }
        }
    }

    /**
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int column) {
        try {
            return columns.get(column);
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class<?> getColumnClass(int columnIndex) {
        if (classMappings.containsKey(Integer.toString(columnIndex))) {
            return classMappings.get(Integer.toString(columnIndex));
        }
        try {
            ArrayList<Object> row = dataStore.get(0);
            Class clazz = row.get(columnIndex).getClass();
            return clazz;
        } catch (Throwable t) {
            return Object.class;
        }
    }

    /**
     * @see org.isqlviewer.util.Sortable#getIndexOfColumnName(String)
     */
    public int getIndexOfColumnName(String name) {
        try {
            return columns.indexOf(name);
        } catch (Throwable t) {
            return -1;
        }
    }

    /**
     * @see org.isqlviewer.util.Sortable#sort(int, boolean)
     */
    public void sort(int column, boolean asc) {
        if (column >= 0 && column < getColumnCount()) {
            try {
                synchronized (dataStore) {
                    ArrayList<Object>[] arr = dataStore.toArray(new ArrayList[dataStore.size()]);
                    Arrays.sort(arr, new RowComparator(column, asc));
                    clearData();
                    dataStore.addAll(Arrays.asList(arr));
                }
                applyFilter(lastFilterPattern);
            } catch (Throwable t) {
            } finally {
                fireTableRowsUpdated(0, getRowCount() - 1);
            }
        }
    }

    public boolean canSort(int column, boolean ascending) {
        return (column >= 0 && column < columns.size()) && dataStore.size() >= 1;
    }

    /**
     * Creates a copy of this TableModel.
     * <p>
     * This method will not copy the existing TableModelListeners to the copied TableModel. However all other members
     * values will copied and cloned.
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone() {
        EnhancedTableModel clone = new EnhancedTableModel(getColumns());
        clone.dataStore = new ArrayList<ArrayList<Object>>();
        if (filteredRows.isEmpty()) {
            synchronized (dataStore) {
                Iterator<ArrayList<Object>> itr = dataStore.iterator();
                while (itr.hasNext()) {
                    ArrayList<Object> row = itr.next();
                    clone.addRow(row);
                }
            }
        } else {
            synchronized (filteredRows) {
                Iterator<Integer> itr = filteredRows.iterator();
                while (itr.hasNext()) {
                    int rowNumber = itr.next().intValue();
                    synchronized (dataStore) {
                        ArrayList<Object> row = dataStore.get(rowNumber);
                        clone.addRow(row);
                    }
                }
            }
        }
        clone.classMappings.putAll(classMappings);
        clone.pageOffset = pageOffset;
        clone.pageSize = pageSize;
        return clone;
    }

    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer("");
        StringBuffer row = new StringBuffer("");
        for (int r = 0; r < getRowCount(); r++) {
            for (int c = 0; c < getColumnCount(); c++) {
                row.append(getValueAt(r, c));
                row.append("\t");
            }
            if (row.toString().trim().length() >= 1) {
                buff.append(row);
                buff.append(System.getProperty("line.seperator", "\n"));
            }
            row.setLength(0);
        }
        return buff.toString();
    }

    /**
     * Sets the class type for the given column.
     * <p>
     * Explicitly declares that the given column is of type clazz, this method a complement to the getColumnClass(int)
     * method. This method can help when rendering this model correctly within a JTable. Paticularly if you have
     * configured a custom renderer on an interface class e.g. CLOB/BLOB. The JTable currently doesn't aquire the
     * correct renderer if an object is just an instance of something.
     * <p>
     * e.g.
     * 
     * <pre>
     * Tablemodel mdl = new EnhancedTableModel();
     * JTable tab = new JTabel(mdl);
     * tab.setDefaultRenderer(Clob.class, new ClobRenderer());
     * mdl.addRow(new ArrayList(Arrays.asList(new MyCLOB[]{new MyClob()})));
     * </pre>
     * 
     * Currently the JTable would not get the right render for the MyCLOB class even though it is an instanceof CLOB the
     * JTable will use the DefaultRenderer for the CLOB object.
     * 
     * <pre>
     * mdl.setClassForColumn(0, Clob.class);
     * </pre>
     * 
     * invocation will cause the JTable to get the Clob.class for column 0 and get the correct renderer for the object
     * within the model. <br>
     * <em>This will be the same effect on CellEditors as well since the JTable, uses similar logic
     *  to aquire the appropriate editor</em>
     * 
     * @see javax.swing.JTable#setDefaultRenderer(java.lang.Class, javax.swing.table.TableCellRenderer)
     * @see #getColumnClass(int)
     * @param column id to declare it's class type for.
     * @param clazz class type based on the column number.
     */
    public void setClassforColumn(int column, Class clazz) {
        synchronized (classMappings) {
            classMappings.put(new Integer(column), clazz);
        }
    }

    /**
     * Removes the specifed row at the given index.
     * <p>
     * This will remove the specified data declared at rowIndex, if the rowIndex is out of range this method will return
     * normally.
     * <p>
     * upon succesfull removal a fireTableRowsDeleted() will be notified to model listeners.
     * 
     * @param rowIndex the row to be removed from this table.
     */
    public void removeRow(int rowIndex) {
        try {
            synchronized (dataStore) {
                dataStore.remove(translateRow(rowIndex));
            }
            fireTableRowsDeleted(rowIndex, rowIndex);
            applyFilter(lastFilterPattern);
        } catch (Throwable t) {
        }
    }

    /**
     * Decreases the page offset by one.
     * <p>
     * This will change the offset by -1 if it is still &gt; 0 afterwards a TableChanged event will be then be fired.
     * 
     * @see #getPageCount()
     * @see #pageDown()
     * @return boolean on success of the method.
     */
    public boolean pageUp() {
        if (pageOffset > 0) {
            pageOffset--;
            fireTableDataChanged();
            return true;
        }
        return false;
    }

    /**
     * Increases the page offset by one.
     * <p>
     * This will change the offset by +1 if it is still &lt; page count afterwards a TableChanged event will be then be
     * fired.
     * 
     * @see #getPageCount()
     * @see #pageUp()
     * @return boolean on success of the method.
     */
    public boolean pageDown() {
        if (pageOffset < getPageCount() - 1) {
            pageOffset++;
            fireTableDataChanged();
            return true;
        }
        return false;
    }

    /**
     * Current paging size for this model.
     * <p>
     * This the number of rows that determines a *page* this is the number of rows that will show in a JTable at a given
     * time.
     * 
     * @see #getPageCount()
     * @return int
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Number of pages this model contains.
     * <p>
     * This will take the true row count divided by the page size. the number returned will always be &gt;= 1
     * 
     * @see #setPageSize(int)
     * @return int
     */
    public int getPageCount() {
        double total = getTrueRowCount();
        double page = getPageSize();
        return (int) Math.ceil(total / page);
    }

    /**
     * Sets the current page size for this model.
     * <p>
     */
    public boolean setPageSize(final int newSize) {
        int localSize = newSize;
        if (localSize >= 0 && localSize != pageSize) {
            if (localSize <= getTrueRowCount()) {
                localSize = getTrueRowCount();
            }
            int old = pageSize;
            pageSize = localSize;
            if (pageSize < old) {
                fireTableRowsDeleted(pageSize, old - 1);
            } else {
                fireTableRowsInserted(old, pageSize - 1);
            }
            return true;
        }
        return false;
    }

    public int getTrueRowCount() {
        return dataStore.size();
    }

    public int getPageOffset() {
        return pageOffset;
    }

    public EnhancedTableModel subModel(int[] rows, int[] columnidxs) {
        EnhancedTableModel sub = new EnhancedTableModel(new String[0]);
        String[] headers = new String[columnidxs.length];
        for (int r = 0; r < rows.length; r++) {
            int row = rows[r];
            ArrayList<Object> data = getRow(row);
            ArrayList<Object> clone = new ArrayList<Object>(columnidxs.length);
            for (int c = 0; c < columnidxs.length; c++) {
                int column = columnidxs[c];
                clone.add(data.get(column));
            }
            sub.addRow(clone);
        }
        for (int c = 0; c < columnidxs.length; c++) {
            int column = columnidxs[c];
            headers[c] = getColumnName(column);
            Class clazz = classMappings.get(new Integer(column));
            if (clazz != null) {
                sub.setClassforColumn(c, clazz);
            }
        }
        sub.setColumns(headers);
        return sub;
    }

    /**
     * This will clear the table of all data.
     * <p>
     * This method should not be confused with the clearAll() method, this will only clear the data, and not the column
     * information.
     * <p>
     * This will fire a TableDataChanged event to TableModel listeners.
     * 
     * @see #clearAll()
     */
    public void clear() {
        clearData();
        fireTableStructureChanged();
    }

    /**
     * This clears all data and column information for this table.
     * <p>
     * This will fire a TableStructureChanged() when done and this table will be completely empty.
     * 
     * @see #clear()
     */
    public void clearAll() {
        clearAllData();
        fireTableStructureChanged();
    }

    /**
     * This will determine if there is data contained in this table.
     * <p>
     * This will return true if there is no data in the underlying data collection. the number of columns will not
     * affect the results of this methods returned value.
     * 
     * @return boolean to determine the emptiness of this tablemodel data.
     */
    public boolean isEmpty() {
        return dataStore.isEmpty();
    }

    /**
     * This will add a row to this TableModel.
     * <p>
     * This method will clone the given ArrayList and then add that reference to underlying data collection. If the
     * given ArrayList is null this method immediately returns.
     * <p>
     * There is no requirement for the size of the given list as long as it is &gt;= to specified column size you should
     * not experience any erratic behaviour. So it is ok for the given ArrayList to have a size greater than this
     * TableModel Column size.
     * <p>
     * a fireRowsInserted() will be fired to model listeners.
     * 
     * @param lst Collection of objects to add as row to this table.
     */
    public void addRow(ArrayList<? extends Object> lst) {
        if (lst == null) {
            return;
        }
        synchronized (dataStore) {
            ArrayList<Object> copy = new ArrayList<Object>(lst);
            dataStore.add(copy);
        }
        fireTableRowsInserted(getRowCount(), getRowCount());
    }

    /**
     * This will ensure that the underlying data collection can hold the row count.
     * <p>
     * If the given size is less than the current size this will remove any extra rows to get this table model down to
     * the correct row count.
     * 
     * @see ArrayList#ensureCapacity(int)
     * @see #setColumnCount(int)
     * @param size the new desire row count for this table model.
     */
    public void setRowCount(int size) {
        int current = getRowCount();
        if (size <= current) {
            for (int i = current; i > size; i++) {
                synchronized (dataStore) {
                    dataStore.remove(i);
                }
            }
        }
        synchronized (dataStore) {
            dataStore.ensureCapacity(size);
        }
        fireTableDataChanged();
    }

    /**
     * Sets the column size of this table model for all rows.
     * <p>
     * This will add blanks strings if current column count is less than the given size other wise column items will be
     * removed to fit the given size.
     * <p>
     * For each row that doesn't meet the given column count an empty Object will be put in place. other wise each row
     * will be trimmed down to the given size.
     * <p>
     * This will fire a TableStructureChanged to model listeners.
     * 
     * @param size the new desired column count.
     */
    public void setColumnCount(int size) {
        synchronized (dataStore) {
            Iterator<ArrayList<Object>> itr = dataStore.iterator();
            synchronized (columns) {
                try {
                    if (columns.size() > size) {
                        for (int i = columns.size(); i > size; i--) {
                            columns.remove(i);
                        }
                        columns.trimToSize();
                    } else {
                        for (int i = columns.size(); i < size; i++) {
                            columns.add(Integer.toString(i));
                        }
                    }
                } catch (Throwable t) {
                    return;
                }
            }
            while (itr.hasNext()) {
                try {
                    ArrayList<Object> lst = itr.next();
                    if (lst.size() > size) {
                        for (int i = columns.size(); i > size; i--) {
                            columns.remove(i);
                        }
                        lst.trimToSize();
                    } else {
                        for (int i = lst.size(); i < size; i++) {
                            columns.add("");
                        }
                    }
                } catch (Throwable t) {
                }
            }
        }
        fireTableStructureChanged();
    }

    /**
     * Updates a column name at a given index.
     * <p>
     * This will replace the exisitng column name string with the given name at the specified index. if the index is
     * invalid it will be silently ignored.
     * <p>
     * This will fire a TableStructedChanged() to model listeners.
     * 
     * @param idx the specidex column index to change
     * @param name is the new column to use, can be null.
     */
    public void setColumnName(int idx, String name) {
        synchronized (columns) {
            try {
                columns.set(idx, name);
                fireTableStructureChanged();
            } catch (Throwable t) {
                return;
            }
        }
    }

    public String[] getColumns() {
        synchronized (columns) {
            return columns.toArray(new String[columns.size()]);
        }
    }

    public synchronized Map<String, Object> getRowMap(int rowIndex) {
        HashMap<String, Object> rowMap = new HashMap<String, Object>();
        for (int i = 0; i < getColumnCount(); i++) {
            rowMap.put(getColumnName(i), getValueAt(rowIndex, i));
        }
        return rowMap;
    }

    /**
     * This will remove the desired column from this table model.
     * <p>
     * This will also ensure that all the
     * 
     * @param columnIndex
     */
    public void removeColumn(int columnIndex) {
        Iterator itr = dataStore.iterator();
        synchronized (dataStore) {
            try {
                columns.remove(columnIndex);
            } catch (Throwable t) {
                return;
            }
            while (itr.hasNext()) {
                try {
                    ArrayList lst = (ArrayList) itr.next();
                    lst.remove(columnIndex);
                } catch (Throwable t) {
                }
            }
        }
        fireTableStructureChanged();
    }

    /**
     * Notifies all listeners that all cell values in the table's rows may have changed. The number of rows may also
     * have changed and the <code>JTable</code> should redraw the table from scratch. The structure of the table (as
     * in the order of the columns) is assumed to be the same.
     * 
     * @see TableModelEvent
     */
    public void fireTableDataChanged() {
        fireTableChanged(new TableModelEvent(this));
    }

    /**
     * Notifies all listeners that the table's structure has changed. The number of columns in the table, and the names
     * and types of the new columns may be different from the previous state. If the <code>JTable</code> receives this
     * event and its <code>autoCreateColumnsFromModel</code> flag is set it discards any table columns that it had and
     * reallocates default columns in the order they appear in the model. This is the same as calling
     * <code>setModel(TableModel)</code> on the <code>JTable</code>.
     * 
     * @see TableModelEvent
     */
    public void fireTableStructureChanged() {
        fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
    }

    /**
     * Notifies all listeners that rows in the range <code>[firstRow, lastRow]</code>, inclusive, have been inserted.
     * 
     * @param firstRow the first row
     * @param lastRow the last row
     * @see TableModelEvent
     */
    public void fireTableRowsInserted(int firstRow, int lastRow) {
        fireTableChanged(new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    /**
     * Notifies all listeners that rows in the range <code>[firstRow, lastRow]</code>, inclusive, have been updated.
     * 
     * @param firstRow the first row
     * @param lastRow the last row
     * @see TableModelEvent
     */
    public void fireTableRowsUpdated(int firstRow, int lastRow) {
        fireTableChanged(new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE));
    }

    /**
     * Notifies all listeners that rows in the range <code>[firstRow, lastRow]</code>, inclusive, have been deleted.
     * 
     * @param firstRow the first row
     * @param lastRow the last row
     * @see TableModelEvent
     */
    public void fireTableRowsDeleted(int firstRow, int lastRow) {
        fireTableChanged(new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE));
    }

    /**
     * Notifies all listeners that the value of the cell at <code>[row, column]</code> has been updated.
     * 
     * @param row row of cell which has been updated
     * @param column column of cell which has been updated
     * @see TableModelEvent
     */
    public void fireTableCellUpdated(int row, int column) {
        fireTableChanged(new TableModelEvent(this, row, row, column));
    }

    /**
     * Forwards the given notification event to all <code>TableModelListeners</code> that registered themselves as
     * listeners for this table model.
     * 
     * @param e the event to be forwarded
     * @see #addTableModelListener
     * @see TableModelEvent
     */
    public void fireTableChanged(TableModelEvent e) {
        if (listeners != null) {
            Iterator itr = listeners.iterator();
            while (itr.hasNext()) {
                TableModelListener tml = (TableModelListener) itr.next();
                try {
                    tml.tableChanged(e);
                } catch (Throwable t) {
                }
            }
        }
    }

    /**
     * Returns a column given its name. Implementation is naive so this should be overridden if this method is to be
     * called often. This method is not in the <code>TableModel</code> interface and is not used by the
     * <code>JTable</code>.
     * 
     * @param columnName string containing name of column to be located
     * @return the column with <code>columnName</code>, or -1 if not found
     */
    public int findColumn(String columnName) {
        if (columnName != null) {
            for (int i = 0; i < getColumnCount(); i++) {
                if (columnName.equals(getColumnName(i))) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean matchesFilter(int rowIndex, int columnIndex) {
        try {
            if (filteredRows.isEmpty()) {
                return false;
            }
            ArrayList row = getRow(rowIndex);
            Object column = row.get(columnIndex);
            Pattern pattern = Pattern.compile(lastFilterPattern, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            return doFilter(column, pattern);
        } catch (Exception ignored) {
            return false;
        }
    }

    public int applyFilter(String text) {
        if (text == null || text.length() == 0) {
            lastFilterPattern = "";
            clearFilter();
        } else {
            Pattern pattern = null;
            try {
                pattern = Pattern.compile(text, Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
                filteredRows.clear();
                lastFilterPattern = text;
            } catch (PatternSyntaxException pse) {
                return -1;
            }
            for (int r = 0; r < getTrueRowCount(); r++) {
                ArrayList<Object> row = null;
                synchronized (dataStore) {
                    row = dataStore.get(r);
                }
                Iterator itr = row.iterator();
                int c = 0;
                while (itr.hasNext()) {
                    if (canSearchColumn(c++)) {
                        Object next = itr.next();
                        if (doFilter(next, pattern)) {
                            filteredRows.add(new Integer(r));
                            fireTableDataChanged();
                            break;
                        }
                    }
                }
            }
            if (filteredRows.isEmpty()) {
                fireTableDataChanged();
            }
            return filteredRows.size();
        }
        return -1;
    }

    public void clearFilter() {
        if (!filteredRows.isEmpty()) {
            filteredRows.clear();
            fireTableDataChanged();
        }
    }

    public int translateRow(int rowIndex) {
        if (filteredRows.isEmpty()) {
            return rowIndex + (pageOffset * pageSize);
        }
        return filteredRows.get(rowIndex).intValue();
    }

    protected boolean doFilter(Object data, Pattern pattern) {
        if (data != null) {
            return pattern.matcher(data.toString()).find();
        }
        return false;
    }

    protected boolean canSearchColumn(int column) {
        return column >= 0 && column < getColumnCount();
    }

    /**
     * Adds a full column to underlying data collection.
     * <p>
     * This method will iterate through the given collection c, and for each row in the underlying data store, the item
     * is added to the each row. If the collection does not contain the correct number of items which should correspond
     * to the current number of rows, empty object will put in place.
     * <p>
     * This method will fire a TableStructureChanged() to model listeners.
     * 
     * @param c a collection of data representing a table column.
     * @param name of the column represting this collection.
     */
    protected void addColumn(String name, Collection c) {
        Object x = new Object();
        synchronized (columns) {
            Iterator citr = c.iterator();
            columns.add(name == null ? "" : name);
            synchronized (dataStore) {
                Iterator<ArrayList<Object>> itr = dataStore.iterator();
                while (itr.hasNext()) {
                    ArrayList<Object> lst = itr.next();
                    if (citr.hasNext()) lst.add(citr.next()); else lst.add(x);
                }
            }
        }
        fireTableStructureChanged();
    }

    /**
     * This is a helper method to clear the datastore without firing a model event.
     * <p>
     * If you need to clear the data without sending a potentially misleading event extenders will want to use this
     * method versus the public clear.
     * 
     * @see #clear()
     */
    protected void clearData() {
        synchronized (dataStore) {
            dataStore.clear();
        }
    }

    /**
     * This is a helper method to clear all the data without firing a model event.
     * <p>
     * If you need to clear the data without sending a potentially misleading event extenders will want to use this
     * method versus the public clearAll.
     * 
     * @see #clearAll()
     */
    protected void clearAllData() {
        synchronized (dataStore) {
            dataStore.clear();
            synchronized (columns) {
                columns.clear();
            }
        }
    }

    /**
     * This assumes a collection of ArrayLists as a way to add a multiple of table rows to the model.
     * <p>
     * If the collection contains an object that cannot be type cast to an ArrayList then the object will be skipped, so
     * Vector and Stacks can be in the given collection since they are sub-class of the ArrayList.
     * <p>
     * Note this method does not mess with the column data information so if you are adding a bunch of rows, where the
     * column counts are off it won't show till you update the column information
     * <p>
     * This will fire a TableRowsInserted() for the number of ArrayLists actually added.
     * 
     * @param c is a collection of ArrayLists to be used as the data store for this model.
     */
    protected void setData(Collection<ArrayList<Object>> c) {
        int count = 0;
        synchronized (dataStore) {
            dataStore.clear();
            Iterator<ArrayList<Object>> itr = c.iterator();
            while (itr.hasNext()) {
                ArrayList<Object> lst = itr.next();
                dataStore.add(lst);
                count++;
            }
        }
        fireTableRowsInserted(-1, count);
    }

    protected ArrayList<Object> getRow(int rowIndex) {
        try {
            int translated = translateRow(rowIndex);
            synchronized (dataStore) {
                return dataStore.get(translated);
            }
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * Replacing existing column information with the given array of Strings.
     * <p>
     * This will clear all the exisiting column information and the new colum count will be the length of the given
     * String array.
     * <p>
     * If the given String[] is null a NullPointerException will most likely be thrown.
     * <p>
     * This will fire a TableStructureChanged() to model listeners.
     * 
     * @param s is the new set of Columns names for this model.
     */
    protected void setColumns(String[] s) {
        synchronized (columns) {
            columns.clear();
            if (s != null) columns.addAll(Arrays.asList(s));
        }
        fireTableStructureChanged();
    }

    /**
     * @param rowAffected
     * @param rowData
     */
    void insertRow(int rowIndex, Map<String, Object> rowData) {
        try {
            int translated = translateRow(rowIndex);
            boolean inserted = false;
            boolean added = false;
            synchronized (dataStore) {
                ArrayList<Object> newRow = new ArrayList<Object>(getColumnCount());
                Iterator keys = rowData.keySet().iterator();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    int idx = findColumn(key);
                    newRow.add(idx, rowData.get(key));
                }
                if (translated < dataStore.size()) {
                    dataStore.add(translated, newRow);
                    inserted = true;
                } else {
                    dataStore.add(newRow);
                    added = true;
                }
            }
            if (inserted) {
                fireTableRowsInserted(translated, translated);
            }
            if (added) {
                fireTableStructureChanged();
            }
        } catch (Throwable t) {
        }
    }
}
