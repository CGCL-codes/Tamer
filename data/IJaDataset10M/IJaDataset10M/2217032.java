package uk.org.ogsadai.converters.databaseschema;

/**
 * Metadata of a database table.
 *
 * @author The OGSA-DAI Team.
 */
public interface TableMetaData {

    /**
     * Returns the name of this table.
     *
     * @return name.
     */
    public abstract String getName();

    /**
     * Returns the schema name of this table.
     *
     * @return schema name.
     */
    public abstract String getSchemaName();

    /**
     * Returns the catalog name of this table.
     *
     * @return catalog name.
     */
    public abstract String getCatalogName();

    /**
     * Returns the number of columns in this table.
     *
     * @return number of columns.
     */
    public abstract int getColumnCount();

    /**
     * Returns the type information of the column at the specified index.
     *
     * @param index 
     *     The column index.
     * @return column type information.
     */
    public abstract ColumnMetaData getColumn(int index);

    /**
     * Returns a set of columns which form the (composite) primary key
     * of this table.
     *
     * @return set of column names
     */
    public abstract String[] getPrimaryKeys();

    /**
     * Returns the imported keys of this table.
     *
     * @return imported keys.
     */
    public abstract KeyMetaData[] getImportedKeys();

    /**
     * Returns the exported keys of this table.
     *
     * @return imported keys.
     */
    public abstract KeyMetaData[] getExportedKeys();
}
