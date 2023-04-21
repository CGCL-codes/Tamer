package org.dbunit.dataset;

/**
 * Represents table metadata.
 *
 * @author Manuel Laflamme
 * @version $Revision: 741 $
 * @since Feb 17, 2002
 */
public interface ITableMetaData {

    /**
     * Returns this table name.
     * @return this table name
     */
    public String getTableName();

    /**
     * Returns this table columns as recognized by dbunit. In cases where columns are resolved 
     * using database metadata it can happen that an empty array is returned when a table does
     * not have a single column that is recognized by the configured 
     * {@link org.dbunit.dataset.datatype.IDataTypeFactory}.
     * Note that it is <b>not</b> an exceptional case within dbunit when a {@link ITableMetaData}
     * does not have a column. 
     * @return The columns for this table
     * @throws DataSetException
     */
    public Column[] getColumns() throws DataSetException;

    /**
     * Returns this table primary key columns.
     * @return this table primary key columns.
     * @throws DataSetException
     */
    public Column[] getPrimaryKeys() throws DataSetException;

    /**
	 * Returns the column's array index of the column with the given name within this table metadata.
	 * @param columnName The name of the column that is searched
	 * @return The index of the given column within this metadata, starting with 0 for the first column
	 * @throws NoSuchColumnException if the given column has not been found
	 * @throws DataSetException if something goes wrong when trying to retrieve the columns
	 * @since 2.3.0
	 */
    public int getColumnIndex(String columnName) throws DataSetException;
}
