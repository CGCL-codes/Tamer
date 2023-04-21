package prisms.osql;

/** Represents a table structure which may be queried, updated, or deleted */
public abstract class Table {

    /** @return The database connection that this table is from */
    public abstract Connection getConnection();

    /** @return All columns in this table */
    public abstract Column<?>[] getColumns();

    /**
	 * @param name The name of the column to get
	 * @return The column in this table with the given name, or null if no such column exists
	 */
    public Column<?> getColumn(String name) {
        for (Column<?> column : getColumns()) if (column.getName().equalsIgnoreCase(name)) return column;
        return null;
    }

    /**
	 * @param <T> The java type of the column
	 * @param name The name of the column to get
	 * @param type The type of the column to get
	 * @return The column in this table with the given name, or null if no such column exists
	 * @throws PrismsSqlException If the column with the given name does not match the given data
	 *         type
	 */
    public <T> Column<T> getColumn(String name, Class<T> type) throws PrismsSqlException {
        Column<?> column = getColumn(name);
        if (column == null) return null;
        if (type.isAssignableFrom(column.getDataType().getJavaType())) return (Column<T>) column; else throw new PrismsSqlException("Type " + type.getName() + " is not valid for column " + name + " (type " + column.getDataType().getPrettyName());
    }

    /** @return All foreign keys constraining this table's data */
    public abstract ForeignKey[] getForeignKeys();

    /**
	 * @param table The table to get the foreign key links to
	 * @return All foreign keys constraining this table's data by the given table's data
	 */
    public ForeignKey[] getForeignKeys(Table table) {
        java.util.ArrayList<ForeignKey> ret = new java.util.ArrayList<ForeignKey>();
        for (ForeignKey key : getForeignKeys()) if (key.getImportKey().getTable().equals(table)) ret.add(key);
        return ret.toArray(new ForeignKey[ret.size()]);
    }

    /**
	 * Prints this table's SQL representation
	 * 
	 * @param ret The string builder to print the representation into
	 */
    public abstract void toSQL(StringBuilder ret);
}
