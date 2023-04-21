package net.sourceforge.squirrel_sql.fw.dialects;

import java.sql.Types;
import net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.sql.ITableInfo;
import net.sourceforge.squirrel_sql.fw.sql.TableColumnInfo;

/**
 * An extension to the standard Hibernate dialect
 * 
 * TODO: Progress specification says that the sum of all column lengths of a 
 *       table row may not exceed 31960.  Need to add an interface method to 
 *       the HibernateDialect interface that takes an array of lengths and 
 *       and checks the sum of columns for a row.  This maximum number may be 
 *       different across databases so this check method needs to be 
 *       implemented by each dialect.
 */
public class ProgressDialect extends org.hibernate.dialect.Dialect implements HibernateDialect {

    public ProgressDialect() {
        super();
        registerColumnType(Types.BIGINT, "integer");
        registerColumnType(Types.BINARY, 2000, "binary($l)");
        registerColumnType(Types.BIT, "bit");
        registerColumnType(Types.BLOB, 999999999, "lvarbinary($l)");
        registerColumnType(Types.BOOLEAN, "bit");
        registerColumnType(Types.CHAR, 2000, "char($l)");
        registerColumnType(Types.CLOB, 31982, "varchar($l)");
        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.DECIMAL, "numeric($p,2)");
        registerColumnType(Types.DOUBLE, "double precision");
        registerColumnType(Types.FLOAT, "float($p)");
        registerColumnType(Types.INTEGER, "integer");
        registerColumnType(Types.LONGVARBINARY, 999999999, "lvarbinary($l)");
        registerColumnType(Types.LONGVARCHAR, 31982, "varchar($l)");
        registerColumnType(Types.NUMERIC, "numeric($p,2)");
        registerColumnType(Types.REAL, "real");
        registerColumnType(Types.SMALLINT, "smallint");
        registerColumnType(Types.TIME, "date");
        registerColumnType(Types.TIMESTAMP, "timestamp");
        registerColumnType(Types.TINYINT, "tinyint");
        registerColumnType(Types.VARBINARY, 31982, "varbinary($l)");
        registerColumnType(Types.VARCHAR, 31982, "varchar($l)");
    }

    public boolean canPasteTo(IDatabaseObjectInfo info) {
        return true;
    }

    public boolean supportsSchemasInTableDefinition() {
        return true;
    }

    public String getLengthFunction(int dataType) {
        return "length";
    }

    public String getMaxFunction() {
        return "max";
    }

    public int getMaxPrecision(int dataType) {
        if (dataType == Types.FLOAT) {
            return 15;
        } else {
            return 32;
        }
    }

    public int getMaxScale(int dataType) {
        return getMaxPrecision(dataType);
    }

    public int getPrecisionDigits(int columnSize, int dataType) {
        return columnSize;
    }

    public int getColumnLength(int columnSize, int dataType) {
        return columnSize;
    }

    /**
     * The string which identifies this dialect in the dialect chooser.
     * 
     * @return a descriptive name that tells the user what database this dialect
     *         is design to work with.
     */
    public String getDisplayName() {
        return "Progress";
    }

    /**
     * Returns boolean value indicating whether or not this dialect supports the
     * specified database product/version.
     * 
     * @param databaseProductName the name of the database as reported by 
     * 							  DatabaseMetaData.getDatabaseProductName()
     * @param databaseProductVersion the version of the database as reported by
     *                              DatabaseMetaData.getDatabaseProductVersion()
     * @return true if this dialect can be used for the specified product name
     *              and version; false otherwise.
     */
    public boolean supportsProduct(String databaseProductName, String databaseProductVersion) {
        if (databaseProductName == null) {
            return false;
        }
        if (databaseProductName.trim().toLowerCase().startsWith("progress")) {
            return true;
        }
        return false;
    }

    /**
     * Returns the SQL statement to use to add a column to the specified table
     * using the information about the new column specified by info.
     * @param info information about the new column such as type, name, etc.
     * 
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         adding columns after a table has already been created.
     */
    public String[] getColumnAddSQL(TableColumnInfo info) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This database dialect doesn't support adding columns to tables");
    }

    /**
     * Returns a boolean value indicating whether or not this dialect supports
     * adding comments to columns.
     * 
     * @return true if column comments are supported; false otherwise.
     */
    public boolean supportsColumnComment() {
        return false;
    }

    /**
     * Returns the SQL statement to use to add a comment to the specified 
     * column of the specified table.
     * 
     * @param tableName the name of the table to create the SQL for.
     * @param columnName the name of the column to create the SQL for.
     * @param comment the comment to add.
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         annotating columns with a comment.
     */
    public String getColumnCommentAlterSQL(String tableName, String columnName, String comment) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("This database dialect doesn't support adding comments to columns");
    }

    /**
     * Returns a boolean value indicating whether or not this database dialect
     * supports dropping columns from tables.
     * 
     * @return true if the database supports dropping columns; false otherwise.
     */
    public boolean supportsDropColumn() {
        return true;
    }

    /**
     * Returns the SQL that forms the command to drop the specified colum in the
     * specified table.
     * 
     * @param tableName the name of the table that has the column
     * @param columnName the name of the column to drop.
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         dropping columns. 
     */
    public String getColumnDropSQL(String tableName, String columnName) {
        return DialectUtils.getColumnDropSQL(tableName, columnName);
    }

    /**
     * Returns the SQL that forms the command to drop the specified table.  If
     * cascade contraints is supported by the dialect and cascadeConstraints is
     * true, then a drop statement with cascade constraints clause will be 
     * formed.
     * 
     * @param tableName the table to drop
     * @param cascadeConstraints whether or not to drop any FKs that may 
     * reference the specified table.
     * 
     * @return the drop SQL command.
     */
    public String getTableDropSQL(String tableName, boolean cascadeConstraints) {
        return DialectUtils.getTableDropSQL(tableName, true, cascadeConstraints);
    }

    /**
     * Returns the SQL that forms the command to add a primary key to the 
     * specified table composed of the given column names.
     * 
     * @param pkName the name of the constraint
     * @param columnNames the columns that form the key
     * @return
     */
    public String[] getAddPrimaryKeySQL(String pkName, TableColumnInfo[] columnNames, ITableInfo ti) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Returns the SQL statement to use to add a comment to the specified 
     * column of the specified table.
     * @param info information about the column such as type, name, etc.
     * @return
     * @throws UnsupportedOperationException if the database doesn't support 
     *         annotating columns with a comment.
     */
    public String getColumnCommentAlterSQL(TableColumnInfo info) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Returns the SQL used to alter the specified column to not allow null 
     * values
     * 
     * @param info the column to modify
     * @return the SQL to execute
     */
    public String getColumnNullableAlterSQL(TableColumnInfo info) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Returns a boolean value indicating whether or not this database dialect
     * supports renaming columns.
     * 
     * @return true if the database supports changing the name of columns;  
     *         false otherwise.
     */
    public boolean supportsRenameColumn() {
        return true;
    }

    /**
     * Returns the SQL that is used to change the column name.
     * 
     * 
     * @param from the TableColumnInfo as it is
     * @param to the TableColumnInfo as it wants to be
     * 
     * @return the SQL to make the change
     */
    public String getColumnNameAlterSQL(TableColumnInfo from, TableColumnInfo to) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Returns a boolean value indicating whether or not this dialect supports 
     * modifying a columns type.
     * 
     * @return true if supported; false otherwise
     */
    public boolean supportsAlterColumnType() {
        return true;
    }

    /**
     * Returns the SQL that is used to change the column type.
     * 
     * @param from the TableColumnInfo as it is
     * @param to the TableColumnInfo as it wants to be
     * 
     * @return the SQL to make the change
     * @throw UnsupportedOperationException if the database doesn't support 
     *         modifying column types. 
     */
    public String getColumnTypeAlterSQL(TableColumnInfo from, TableColumnInfo to) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Not Yet Implemented");
    }

    /**
     * Returns a boolean value indicating whether or not this database dialect
     * supports changing a column from null to not-null and vice versa.
     * 
     * @return true if the database supports dropping columns; false otherwise.
     */
    public boolean supportsAlterColumnNull() {
        return false;
    }

    /**
     * Returns a boolean value indicating whether or not this database dialect
     * supports changing a column's default value.
     * 
     * @return true if the database supports modifying column defaults; false 
     *         otherwise
     */
    public boolean supportsAlterColumnDefault() {
        return true;
    }

    /**
     * Returns the SQL command to change the specified column's default value
     *   
     * @param info the column to modify and it's default value.
     * @return SQL to make the change
     */
    public String getColumnDefaultAlterSQL(TableColumnInfo info) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Returns the SQL command to drop the specified table's primary key.
     * 
     * @param pkName the name of the primary key that should be dropped
     * @param tableName the name of the table whose primary key should be 
     *                  dropped
     * @return
     */
    public String getDropPrimaryKeySQL(String pkName, String tableName) {
        return DialectUtils.getDropPrimaryKeySQL(pkName, tableName, false, false);
    }
}
