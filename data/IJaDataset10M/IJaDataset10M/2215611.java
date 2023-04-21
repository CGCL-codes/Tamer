package gov.sns.apps.jeri.tools.database.swing;

import com.cosylab.gui.components.ProgressEvent;
import com.cosylab.gui.components.ProgressListener;
import gov.sns.application.Application;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.table.AbstractTableModel;
import gov.sns.tools.swing.SwingWorker;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.DatabaseMetaData;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 * Provides a table model that can be populated with a query, and can edit the 
 * data returned by the query. This class uses threads to make the UI more
 * responsive.
 * 
 * @author Chris Fowlkes
 */
public class EditableQueryTableModel extends AbstractTableModel implements DatabaseTableModel {

    /**
   * Holds the <CODE>Connection</CODE> used to connect to the RDB.
   */
    private Connection connection;

    /**
   * The sql used to populate the model.
   */
    private String query;

    /**
   * The parameters to use in the query when getting the data from the RDB.
   */
    private Object[] parameters;

    /**
   * Holds the data from the RDB.
   */
    private ArrayList<Object[]> rows = new ArrayList();

    /**
   * Provides a lock for methods that access the RDB.
   */
    private ReentrantLock rdbLock = new ReentrantLock();

    /**
   * Holds the names of the schema for the columns in the table.
   */
    private String[] schemaNames;

    /**
   * Holds the names of the RDB tables for the columns in the table.
   */
    private String[] tableNames;

    /**
   * Holds the names of the columns in the table.
   */
    private String[] columnNames;

    /**
   * Holds the names of the schema for the columns in the table from the time 
   * they are provided by the caller till they are needed when the data is 
   * reloaded.
   */
    private String[] newSchemaNames;

    /**
   * Holds the names of the RDB tables for the columns in the table from the 
   * time they are provided by the caller till they are needed when the data is 
   * reloaded.
   */
    private String[] newTableNames;

    /**
   * Holds the names of the columns in the table from the time they are provided 
   * by the caller till they are needed when the data is reloaded.
   */
    private String[] newColumnNames;

    /**
   * Holds the primary keys for the RDB tables in the model. These are needed to
   * resolve any changes made to the data.
   */
    private HashMap<String, String[]> primaryKeys = new HashMap();

    private transient ArrayList progressListeners = new ArrayList(2);

    /**
   * Holds the data for the row being edited.
   */
    private Object[] editRowData;

    /**
   * Holds the index of the row being edited. <CODE>-1</CODE> by default.
   */
    private int editRow = -1;

    /**
   * Holds the indexes of the columns in the edit row that have been changed.
   */
    private ArrayList<Integer> editedColumns = new ArrayList();

    /**
   * Flag used to determine if changes are pending.
   */
    private boolean commitNeeded = false;

    /**
   * Holds the columns that are read only.
   */
    private ArrayList<String> readOnlyColumns = new ArrayList();

    private transient ArrayList propertyChangeListeners = new ArrayList(2);

    /**
   * Flag used to determine if the model should try to resolve the names and 
   * tables for the columns returned by the query.
   */
    private boolean columnNamesChanged = false;

    /**
   * Cache that holds the column names for each table.
   */
    private HashMap<String, ArrayList<String>> cachedTableColumns = new HashMap();

    /**
   * Gets the row count for the table model.
   * 
   * @return The number of rows in the model.
   */
    public int getRowCount() {
        return rows.size();
    }

    /**
   * Gets the number of columns in the model.
   * 
   * @return The number of columns in the model.
   */
    public int getColumnCount() {
        int columnCount;
        if (columnNames == null) columnCount = 0; else columnCount = columnNames.length;
        return columnCount;
    }

    /**
   * Gets the value for the given cell.
   * 
   * @param rowIndex The row index of the cell of which to return the value.
   * @param columnIndex The column index of the cell of which to return the value.
   * @return The value of the given cell.
   */
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex == editRow && editedColumns.contains(columnIndex)) return editRowData[columnIndex];
        return rows.get(rowIndex)[columnIndex];
    }

    /**
   * Reloads the data from the RDB. This method needs to be invoked to load the
   * data from the RDB.
   */
    public void refresh() {
        refresh("Loading Table Data");
    }

    /**
   * Reloads the data from the RDB. This method needs to be invoked to load the
   * data from the RDB.
   * 
   * @param message The message to use in the progress events that are fired by the method.
   */
    public void refresh(final String message) {
        int rowCount = getRowCount();
        rows.clear();
        fireTableRowsDeleted(0, rowCount);
        SwingWorker worker = new SwingWorker() {

            @Override
            public Object construct() {
                try {
                    rdbLock.lock();
                    fireTaskStarted(new ProgressEvent(this, message, 0, -1));
                    PreparedStatement statement = EditableQueryTableModel.this.getConnection().prepareStatement(getQuery());
                    try {
                        Object[] parameters = getParameters();
                        if (parameters != null) for (int i = 0; i < parameters.length; i++) statement.setObject(i + 1, parameters[i]);
                        ResultSet data = statement.executeQuery();
                        try {
                            ResultSetMetaData metaData = data.getMetaData();
                            int columnCount = metaData.getColumnCount();
                            if (columnNamesChanged) {
                                if (newSchemaNames == null) {
                                    newSchemaNames = new String[columnCount];
                                    newTableNames = new String[columnCount];
                                    newColumnNames = new String[columnCount];
                                    for (int i = 0; i < columnCount; i++) {
                                        newSchemaNames[i] = metaData.getSchemaName(i + 1);
                                        newTableNames[i] = metaData.getTableName(i + 1);
                                        newColumnNames[i] = metaData.getColumnName(i + 1);
                                    }
                                } else if (newColumnNames == null) {
                                    HashMap<String, String[]> columnTables = mapColumnsToNewTables();
                                    newSchemaNames = new String[columnCount];
                                    newTableNames = new String[columnCount];
                                    newColumnNames = new String[columnCount];
                                    for (int i = 0; i < columnCount; i++) {
                                        newColumnNames[i] = metaData.getColumnName(i + 1);
                                        String[] columnTableInformation = columnTables.get(newColumnNames[i]);
                                        newSchemaNames[i] = columnTableInformation[0];
                                        newTableNames[i] = columnTableInformation[1];
                                    }
                                }
                                updateColumnNamesLater();
                            }
                            while (data.next()) {
                                final Object[] currentRow = new Object[columnCount];
                                for (int i = 0; i < currentRow.length; i++) currentRow[i] = data.getObject(i + 1);
                                addRowLater(currentRow);
                            }
                        } finally {
                            data.close();
                        }
                    } finally {
                        statement.close();
                    }
                    editedColumns.clear();
                    editRow = -1;
                    fireTaskComplete(new ProgressEvent(this, null, 0, -1));
                } catch (java.sql.SQLException ex) {
                    ex.printStackTrace();
                    fireTaskInterrupted(new ProgressEvent(this, ex.getMessage(), 0, -1));
                } finally {
                    rdbLock.unlock();
                }
                return null;
            }
        };
        worker.start();
    }

    /**
   * Takes the values in the <CODE>newSchemaNames</CODE> and 
   * <CODE>newTableNames</CODE> arrays and returns a 
   * <CODE>HashMap<String><String[]></CODE> with all column names found for 
   * those tables as the keys, and <CODE>String[]</CODE> as the values, where 
   * <CODE>String[0]</CODE> is the schema name for the column and 
   * <CODE>String[1]</CODE> is the table name.
   * 
   * @return The columns in the tables held in the <CODE>newTableNames</CODE> array in the format HashMap<COLUMNNAME, String[]{SCHEMA_NAME, TABLE_NAME}>.
   * @throws java.sql.SQLException Thrown on SQL error.
   */
    private HashMap<String, String[]> mapColumnsToNewTables() throws java.sql.SQLException {
        HashMap<String, String[]> mappedColumnTables = new HashMap();
        DatabaseMetaData metaData = getConnection().getMetaData();
        for (int i = 0; i < newTableNames.length; i++) {
            StringBuffer buffer = new StringBuffer(newSchemaNames[i]);
            buffer.append(".");
            buffer.append(newTableNames[i]);
            String cachedTableName = buffer.toString();
            ArrayList<String> cachedColumns = cachedTableColumns.get(cachedTableName);
            if (cachedColumns == null) {
                ResultSet columnsData = metaData.getColumns(null, newSchemaNames[i], newTableNames[i], null);
                cachedColumns = new ArrayList();
                while (columnsData.next()) cachedColumns.add(columnsData.getString("COLUMN_NAME"));
                cachedTableColumns.put(cachedTableName, cachedColumns);
            }
            int cachedColumnCount = cachedColumns.size();
            for (int j = 0; j < cachedColumnCount; j++) mappedColumnTables.put(cachedColumns.get(j), new String[] { newSchemaNames[i], newTableNames[i] });
        }
        return mappedColumnTables;
    }

    /**
   * Handles the SQL exception in the event thread.
   * 
   * @param ex The <CODE>SQLException</CODE> thrown.
   */
    private void handleSQLExceptionLater(final java.sql.SQLException ex) {
        ex.printStackTrace();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Application.displayApplicationError("SQL Error", "Error fetching data from RDB.", ex);
            }
        });
    }

    /**
   * Updates the given column name.
   */
    private void updateColumnNamesLater() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                schemaNames = newSchemaNames;
                tableNames = newTableNames;
                columnNames = newColumnNames;
                fireTableStructureChanged();
            }
        });
    }

    /**
   * Adds the given data to the model as a row. This method can be invoked from 
   * a seperate thread.
   * 
   * @param currentRow The data that comprises the row added.
   */
    private void addRowLater(final Object[] currentRow) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                int rowIndex = rows.size();
                rows.add(currentRow);
                fireTableRowsInserted(rowIndex, rowIndex);
            }
        });
    }

    /**
   * Filters are not supported by this class, so this method always returns 
   * <CODE>false</CODE>.
   * 
   * @return <CODE>false</CODE> because filters are not supported by this class.
   */
    public boolean isFiltered() {
        return false;
    }

    /**
   * Commits any pending changes. This method will also post pending changes.
   */
    public void commit() {
        SwingWorker worker = new SwingWorker() {

            @Override
            public Object construct() {
                boolean posted = false;
                try {
                    rdbLock.lock();
                    if (isChanged()) {
                        postChanges();
                        posted = true;
                    }
                    getConnection().commit();
                    setCommitNeeded(false);
                    return posted;
                } catch (java.sql.SQLException ex) {
                    handleSQLExceptionLater(ex);
                } finally {
                    rdbLock.unlock();
                }
                return posted;
            }

            @Override
            public void finished() {
                if (((Boolean) get()).booleanValue()) changesPosted();
            }
        };
        worker.start();
    }

    /**
   * Rolls back any pending changes.
   */
    public void rollback() {
        SwingWorker worker = new SwingWorker() {

            @Override
            public Object construct() {
                try {
                    rdbLock.lock();
                    getConnection().rollback();
                } catch (java.sql.SQLException ex) {
                    handleSQLExceptionLater(ex);
                } finally {
                    rdbLock.unlock();
                }
                return null;
            }
        };
        worker.start();
    }

    /**
   * Returns <CODE>true</CODE> if there are uncommitted changes pending.
   * 
   * @return <CODE>true</CODE> if there are uncommitted changes pending.
   */
    public boolean isCommitNeeded() {
        return commitNeeded;
    }

    /**
   * Sets the value of the commitNeeded flag.
   * 
   * @param commitNeeded Pass as <CODE>true</CODE> if a commit is needed, <CODE>false</CODE> otherwise.
   */
    public void setCommitNeeded(boolean commitNeeded) {
        boolean oldValue = this.commitNeeded;
        if (oldValue ^ commitNeeded) {
            this.commitNeeded = commitNeeded;
            firePropertyChange("commitNeeded", oldValue, commitNeeded);
        }
    }

    /**
   * Returns <CODE>true</CODE> if the data in the model has been changed.
   * 
   * @return <CODE>true</CODE> if the data has been changed, <CODE>false</CODE> otherwise.
   */
    public boolean isChanged() {
        return editedColumns.size() > 0;
    }

    /**
   * Sets the value at the given cell index.
   * 
   * @param aValue The value to set at the given cell.
   * @param rowIndex The row index of the cell for which to set the value.
   * @param columnIndex The column index of the cell for which to set the value.
   */
    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        if (compare(aValue, getValueAt(rowIndex, columnIndex))) return;
        if (isChanged() && editRow != rowIndex) {
            SwingWorker worker = new SwingWorker() {

                @Override
                public Object construct() {
                    try {
                        rdbLock.lock();
                        postChanges();
                    } finally {
                        rdbLock.unlock();
                    }
                    return null;
                }

                @Override
                public void finished() {
                    changesPosted();
                    updateValue(aValue, rowIndex, columnIndex);
                }
            };
            worker.start();
        } else updateValue(aValue, rowIndex, columnIndex);
    }

    /**
   * Call this method in the event thread after changes are posted to the RDB.
   */
    private void changesPosted() {
        if (isChanged()) {
            Object[] dataRow = rows.get(editRow);
            int editedColumnCount = editedColumns.size();
            for (int i = 0; i < editedColumnCount; i++) {
                int column = editedColumns.get(i).intValue();
                dataRow[column] = editRowData[column];
            }
            editRow = -1;
            editedColumns.clear();
            firePropertyChange("changed", true, false);
        }
    }

    /**
   * Updates the value for the given cell.
   */
    private void updateValue(Object aValue, int rowIndex, int columnIndex) {
        boolean changed = isChanged();
        if (!changed) {
            editRowData = new Object[getColumnCount()];
            editRow = rowIndex;
        }
        editRowData[columnIndex] = aValue;
        editedColumns.add(columnIndex);
        fireTableCellUpdated(rowIndex, columnIndex);
        firePropertyChange("changed", changed, isChanged());
    }

    /**
   * Sets the <CODE>Connection</CODE> used to connect to the RDB. This 
   * connection is not closed by the model. It needs to be closed by the calling 
   * class.
   * 
   * @param connection The <CODE>Connection</CODE> to use to connect to the RDB.
   */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
   * The <CODE>Connection</CODE> used to connect to the RDB.
   * 
   * @return The <CODE>Connection</CODE> used to connect to the RDB.
   */
    public Connection getConnection() {
        return connection;
    }

    /**
   * Sets the query used to reload the data. If this method is used to pass in 
   * the query, the <CODE>refresh()</CODE> method will try to resolve the schema
   * and table names for the columns returned by the query, but most RDBs, 
   * including Oracle, do not provide this information to JDBC. If this is not
   * supported by the JDBC driver used the <CODE>refresh()</CODE> method will 
   * throw an exception.
   * 
   * @param query The query to use to load the data from the RDB.
   */
    private void setQuery(String query) {
        setQuery(query, null, null, null);
    }

    /**
   * Sets the query used to reload the data. The tables given can not have 
   * columns with the same name because the method will not be able to resolve 
   * which column is used. Both arrays need to have the same number of items. If 
   * they don't a <CODE>IllegalArgumentException</CODE> will be thrown.
   * 
   * @param query The query to use to load the data from the RDB.
   * @param schemaNames The names of the schemas for the tables loaded by the query.
   * @param tableNames The names of the tables loaded by the query.
   */
    public void setQuery(String query, String[] schemaNames, String[] tableNames) {
        setQuery(query, schemaNames, tableNames, null);
    }

    /**
   * Sets the query used to get data from the RDB. The arrays must all have the 
   * same number of items, one for each column returned by the query. If these 
   * are <CODE>null</CODE>, the model will try to resolve this information but
   * most RDBs, including Oracle, don't provide it with the result set. If the
   * information isn't provided and it can't be resolved an error will be thrown
   * when the data is loaded.
   * 
   * @param query The query used to get the data from the database.
   */
    public void setQuery(String query, String[] schemaNames, String[] tableNames, String[] columnNames) {
        if (schemaNames != null) {
            if (tableNames == null || tableNames.length != schemaNames.length || (columnNames != null && columnNames.length != schemaNames.length)) throw new java.lang.IllegalArgumentException("Arrays passed in need to be the same length.");
        } else {
            if (tableNames != null || columnNames != null) throw new java.lang.IllegalArgumentException("Arrays passed in need to be the same length.");
        }
        this.query = query;
        newSchemaNames = schemaNames;
        newTableNames = tableNames;
        newColumnNames = columnNames;
        columnNamesChanged = true;
    }

    /**
   * Gets the query used to get data from the RDB.
   * 
   * @return The query used to get the data from the database.
   */
    public String getQuery() {
        return query;
    }

    /**
   * Sets the values used in the query that retrieves the data from the RDB.
   * This model uses a prepared statement to load the data from the RDB, which
   * can contain variables. This method needs to be called before the 
   * <CODE>refresh()</CODE> method if parameters are used.
   * 
   * @param parameters The values to use when loading the data from the RDB.
   */
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    /**
   * Gets the parameters used to get the data from the RDB.
   * 
   * @return The parameters used to load the data from the database.
   */
    public Object[] getParameters() {
        return parameters;
    }

    /**
   * Gets the name of the column at the given index.
   * 
   * @param column The index of the column for which to return the name.
   * @return The name of the given column.
   */
    @Override
    public String getColumnName(int column) {
        String columnName = columnNames[column];
        if (columnName == null) return ""; else return columnName;
    }

    public void insert() {
    }

    public void delete(int[] rows) {
    }

    /**
   * Posts the changes back to the RDB.
   */
    public void post() {
        if (isChanged()) {
            SwingWorker worker = new SwingWorker() {

                @Override
                public Object construct() {
                    try {
                        rdbLock.lock();
                        postChanges();
                    } finally {
                        rdbLock.unlock();
                    }
                    return null;
                }

                @Override
                public void finished() {
                    changesPosted();
                }
            };
            worker.start();
        }
    }

    /**
   * Posts the changes in the current thread.
   */
    private void postChanges() {
        if (isChanged()) try {
            rdbLock.lock();
            int editedColumnCount = editedColumns.size();
            fireTaskStarted(new ProgressEvent(this, "Posting Changes", 0, editedColumnCount));
            ArrayList updatedTables = new ArrayList();
            for (int i = 0; i < editedColumnCount; i++) {
                int editedColumnIndex = editedColumns.get(i).intValue();
                if (!updatedTables.contains(tableNames[editedColumnIndex])) {
                    postTableChanges(schemaNames[editedColumnIndex], tableNames[editedColumnIndex]);
                    updatedTables.add(tableNames[editedColumnIndex]);
                }
                fireProgress(new ProgressEvent(this, "Posting Changes", i + 1, editedColumnCount));
            }
            fireTaskComplete(new ProgressEvent(this, null, 0, -1));
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            fireTaskInterrupted(new ProgressEvent(this, ex.getMessage(), 0, -1));
        } finally {
            rdbLock.unlock();
        }
    }

    /**
   * Updates the RDB with the changesto the given table. Only columns for the 
   * given table will be updated.
   * 
   * @param schemaName The schema name of the table to update.
   * @param tableName The name of the table to update.
   * @throws java.sql.SQLException Thrown on SQL error.
   */
    private void postTableChanges(String schemaName, String tableName) throws java.sql.SQLException {
        try {
            rdbLock.lock();
            StringBuffer buffer = new StringBuffer(schemaName);
            buffer.append(".");
            buffer.append(tableName);
            String schemaTableName = buffer.toString();
            buffer = new StringBuffer("UPDATE ");
            buffer.append(schemaTableName);
            buffer.append(" SET ");
            int editedColumnCount = editedColumns.size();
            ArrayList queryValues = new ArrayList();
            for (int i = 0; i < editedColumnCount; i++) {
                int editedColumnIndex = editedColumns.get(i).intValue();
                if (schemaName.equals(schemaNames[editedColumnIndex]) && tableName.equals(tableNames[editedColumnIndex])) {
                    if (queryValues.size() > 0) buffer.append(", ");
                    buffer.append(columnNames[editedColumnIndex]);
                    buffer.append(" = ?");
                    queryValues.add(editRowData[editedColumnIndex]);
                }
            }
            buffer.append(" WHERE ");
            String[] primaryKeys = getPrimaryKeys(schemaName, tableName);
            Object[] originalRow = rows.get(editRow);
            for (int i = 0; i < primaryKeys.length; i++) {
                if (i > 0) buffer.append(" AND ");
                buffer.append(primaryKeys[i]);
                buffer.append(" = ?");
                int keyColumnIndex = findColumnIndex(schemaName, tableName, primaryKeys[i]);
                queryValues.add(originalRow[keyColumnIndex]);
            }
            PreparedStatement query = getConnection().prepareStatement(buffer.toString());
            try {
                int valueCount = queryValues.size();
                for (int i = 0; i < valueCount; i++) query.setObject(i + 1, queryValues.get(i));
                query.execute();
                setCommitNeeded(true);
            } finally {
                query.close();
            }
        } finally {
            rdbLock.unlock();
        }
    }

    /**
   * Looks for and returns the first column to meet the given criteria.
   * 
   * @param schemaName The schema name for the column to return.
   * @param tableName The table name for the column to return.
   * @param columnName The name of the column to return.
   * @return The index of the column that meets the given criteria, or <CODE>-1</CODE> if no column is returned.
   */
    private int findColumnIndex(String schemaName, String tableName, String columnName) {
        for (int i = 0; i < columnNames.length; i++) {
            if (schemaName.equals(schemaNames[i]) && tableName.equals(tableNames[i]) && columnName.equals(columnNames[i])) return i;
        }
        return -1;
    }

    /**
   * Cancels the changes.
   */
    public void cancel() {
        editRow = -1;
        editedColumns.clear();
    }

    /**
   * Sets the primary keys for the RDB tables in the model. These are needed to
   * resolve any changes made to the data back to the RDB. If these are not 
   * provided, an attempt will be made to determine them when needed, but 
   * providing them before hand will eliminate uneccessary trips to the RDB and
   * greatly improve performance.
   * 
   * @param schemaName The schema name for the RDB table.
   * @param tableName The name of the RDB table to which the keys pertain.
   * @param keys The keys for the given RDB table.
   */
    public void setPrimaryKeys(String schemaName, String tableName, String[] keys) {
        StringBuffer schemaTableName = new StringBuffer(schemaName);
        schemaTableName.append(".");
        schemaTableName.append(tableName);
        primaryKeys.put(schemaTableName.toString(), keys);
    }

    /**
   * Gets the primary keys for the RDB tables in the model.
   * 
   * @param schemaName The schema name for the RDB table.
   * @param tableName The name of the RDB table to which the keys pertain.
   * @return The keys for the given RDB table.
   */
    public String[] getPrimaryKeys(String schemaName, String tableName) throws java.sql.SQLException {
        StringBuffer schemaTableName = new StringBuffer(schemaName);
        schemaTableName.append(".");
        schemaTableName.append(tableName);
        String[] keys = primaryKeys.get(schemaTableName.toString());
        if (keys == null) {
            ResultSet result = getConnection().getMetaData().getPrimaryKeys(null, schemaName, tableName);
            try {
                ArrayList rdbKeys = new ArrayList();
                while (result.next()) rdbKeys.add(result.getString("COLUMN_NAME"));
                keys = (String[]) rdbKeys.toArray(new String[rdbKeys.size()]);
                setPrimaryKeys(schemaName, tableName, keys);
            } finally {
                result.close();
            }
        }
        return keys;
    }

    /**
   * Clears the data in the table.
   */
    public void clear() {
        columnNames = null;
        fireTableStructureChanged();
    }

    /**
   * Fires a progress change event. 
   * 
   * @param e The <CODE>ProgressEvent</CODE> to fire.
   */
    protected void fireProgress(final ProgressEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                List listeners = (List) progressListeners.clone();
                int count = listeners.size();
                for (int i = 0; i < count; i++) {
                    ((ProgressListener) listeners.get(i)).progress(e);
                }
            }
        });
    }

    /**
   * Fires a task started event. This is done in a thread safe manner using
   * <CODE>SwingUtilities.invokeLater</CODE>.
   * 
   * @param e The <CODE>ProgressEvent</CODE> to fire.
   */
    protected void fireTaskStarted(final ProgressEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                List listeners = (List) progressListeners.clone();
                int count = listeners.size();
                for (int i = 0; i < count; i++) {
                    ((ProgressListener) listeners.get(i)).taskStarted(e);
                }
            }
        });
    }

    /**
   * Fires a task started event. This is done in a thread safe manner using
   * <CODE>SwingUtilities.invokeLater</CODE>.
   * 
   * @param e The <CODE>ProgressEvent</CODE> to fire.
   */
    protected void fireTaskInterrupted(final ProgressEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                List listeners = (List) progressListeners.clone();
                int count = listeners.size();
                for (int i = 0; i < count; i++) {
                    ((ProgressListener) listeners.get(i)).taskInterruped(e);
                }
            }
        });
    }

    /**
   * Fires a task started event. This is done in a thread safe manner using
   * <CODE>SwingUtilities.invokeLater</CODE>.
   * 
   * @param e The <CODE>ProgressEvent</CODE> to fire.
   */
    protected void fireTaskComplete(final ProgressEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                List listeners = (List) progressListeners.clone();
                int count = listeners.size();
                for (int i = 0; i < count; i++) {
                    ((ProgressListener) listeners.get(i)).taskComplete(e);
                }
            }
        });
    }

    /**
   * Adds the given <CODE>ProgressListener</CODE> to the module.
   * 
   * @param l The <CODE>ProgressListener</CODE> to add to the module.
   */
    public synchronized void addProgressListener(ProgressListener l) {
        if (!progressListeners.contains(l)) {
            progressListeners.add(l);
        }
    }

    /**
   * Removes the given <CODE>ProgressListener</CODE> from the module.
   * 
   * @param l The <CODE>ProgressListener</CODE> to remove from the module.
   */
    public synchronized void removeProgressListener(ProgressListener l) {
        progressListeners.remove(l);
    }

    /**
   * Gets the instances of <CODE>ProgressListener</CODE> that have been added to
   * the module.
   * 
   * @return The instances of <CODE>ProgressListener</CODE> that have been added to the module.
   */
    protected ArrayList getProgressListeners() {
        return progressListeners;
    }

    /**
   * Adds the given <CODE>PropertyChangeListener</CODE> to the module.
   * 
   * @param l The <CODE>PropertyChangeListener</CODE> to add to the module.
   */
    public synchronized void addPropertyChangeListener(PropertyChangeListener l) {
        if (!propertyChangeListeners.contains(l)) {
            propertyChangeListeners.add(l);
        }
    }

    /**
   * Removes the given <CODE>PropertyChangeListener</CODE> from the module.
   * 
   * @param l The <CODE>PropertyChangeListener</CODE> to remove from the module.
   */
    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        propertyChangeListeners.remove(l);
    }

    /**
   * Creates a <CODE>PropertyChangeEvent</CODE> and fires it.
   * 
   * @param propertyName The name of the property changed.
   * @param oldValue The value of the property before the change.
   * @param newValue The new value of the property.
   */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        firePropertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
    }

    /**
   * Fires the given property change event. This method should be called if the 
   * title property changes.
   * 
   * @param e The <CODE>PropertyChangeEvent</CODE> to fire.
   */
    protected void firePropertyChange(final PropertyChangeEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                List listeners = (List) propertyChangeListeners.clone();
                int count = listeners.size();
                for (int i = 0; i < count; i++) {
                    ((PropertyChangeListener) listeners.get(i)).propertyChange(e);
                }
            }
        });
    }

    /**
   * Compares two objects with the <CODE>equals</CODE> method. This method also 
   * considers two <CODE>null</CODE> objects equal as well.
   * 
   * @param object1 The first <CODE>Object</CODE> to compare.
   * @param object2 The <CODE>Object</CODE> to compare to.
   * @return <CODE>true</CODE> if the two items are equal.
   */
    public boolean compare(Object object1, Object object2) {
        if ((object1 == null || object2 == null) && (object1 instanceof String || object2 instanceof String)) return compare((String) object1, (String) object2);
        if (object1 == null) {
            if (object2 != null) return false;
        } else if (!object1.equals(object2)) return false;
        return true;
    }

    /**
   * Compares two instances of <CODE>String</CODE>. This method considers 
   * <CODE>null</CODE> and empty string to be equal and does not consider 
   * leading or trailing whitespace.
   * 
   * @param string1 The first <CODE>String</CODE> to compare.
   * @param string2 The <CODE>String</CODE> to compare to.
   * @return <CODE>true</CODE> if the two items are equal.
   */
    public boolean compare(String string1, String string2) {
        if (string1 == null) string1 = "";
        if (string2 == null) string2 = "";
        return string1.trim().equals(string2.trim());
    }

    /**
   * Determines if the given cell is editable.
   * 
   * @param rowIndex The row index of the cell.
   * @param columnIndex The column index of the cell.
   * @return The value of the cell at the given index.
   */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /**
   * Makes the given column read only or editable. By default all columns are 
   * editable.
   * 
   * @param schemaName The schamea name for the column.
   * @param tableName The table name for the column.
   * @param columnName The name of the column.
   * @param readOnly Pass as <CODE>true</CODE> to make a column read only, <CODE>false</CODE> otherwise.
   */
    public void setColumnReadOnly(String schemaName, String tableName, String columnName, boolean readOnly) {
        StringBuffer buffer = new StringBuffer(schemaName);
        buffer.append(".");
        buffer.append(tableName);
        buffer.append(".");
        buffer.append(columnName);
        String columnReference = buffer.toString();
        if (readOnly) {
            if (readOnlyColumns.contains(columnReference)) readOnlyColumns.add(columnReference);
        } else readOnlyColumns.remove(columnReference);
    }
}
