package org.in4ama.datasourcemanager;

import java.util.ArrayList;
import org.in4ama.datasourcemanager.exception.DataSourceException;

/** Common data set operations */
public interface DataSet {

    /** Defines the name for the current column iterations */
    static final String CURRENTCOLUMN_ITER = "<current>";

    static final String CURRENTROW_ITER = "<row>";

    /** Gets the owner data source object */
    DataSource getDataSource();

    /** Gets the name of this data set */
    String getName();

    /** Gets the internal DataSet object for the specified name */
    DataSet getDataSet(String dataSetName) throws DataSourceException;

    /** Gets the internal DataSet object for the specified name,
	 * the second argument specifies the default value. */
    DataSet getDataSet(String dataSetName, Object defaultValue) throws DataSourceException;

    /** Gets the internal DataSet object located at the specified position */
    DataSet getDataSet(int idx) throws DataSourceException;

    /** Rebuilds the specified data set and returns, the name of the data set can be provided in canonical form,
	 * only the data set specified by the last part is rebuild.
	 */
    DataSet restoreDataSet(String dataSetName) throws DataSourceException;

    /**
	 * Moves the cursor to the next position in this data set. When this data
	 * set contains no data the first invocation of this method will return
	 * false.
	 * 
	 * @return true if the operation succeeded, false otherwise.
	 */
    boolean nextRow() throws DataSourceException;

    /**
	 * Moves the iteration index to the next position in the data set.
	 * @return true if the operation succeeded, false otherwise.
	 */
    boolean nextColumn() throws DataSourceException;

    /**
	 * Indicates whether this data set contains any data (e.g. data set may not
	 * contain any data if the query return zero rows).
	 * 
	 * @return true if there are some data, false otherwise.
	 */
    boolean hasData();

    /** Gets the available column names */
    ArrayList<String> getColumnNames() throws DataSourceException;

    /** Gets the value of the current cell in the iteration context */
    Object getValue() throws DataSourceException;

    /** This method is invoked when the data set is about to be removed */
    void close() throws DataSourceException;

    /** Returns the number of columns in this data set object. */
    int getColumnCount() throws DataSourceException;

    /** Returns the number of cells in this data set object. */
    int getRowCount() throws DataSourceException;

    /** Gets the name of the column at the specified index */
    String getColumnName(int colIdx) throws DataSourceException;

    /** Gets the index of the current column */
    int getCurrentColumn() throws DataSourceException;

    /** Gets the index of the current row */
    int getCurrentRow() throws DataSourceException;
}
