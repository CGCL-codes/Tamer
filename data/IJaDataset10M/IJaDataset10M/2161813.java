package onepoint.persistence.sql;

import java.util.List;
import onepoint.log.XLog;
import onepoint.log.XLogFactory;

/**
 * Interface representing an sql statement which is db dependent.
 *
 * @author horia.chiorean
 */
public abstract class OpSqlStatement {

    protected static final XLog logger = XLogFactory.getLogger(OpSqlStatement.class);

    /**
    * Returns an sql statement that will change the type of a db column.
    *
    * @param tableName  a <code>String</code> representing the name of the table for which the statement is executed.
    * @param columnName a <code>String</code> representing the name of the column for which the statement is executed.
    * @param sqlType    an <code>int</code> representing new SQL type of the column.
    * @return a <code>String</code> representing a statement used to change the type of a column type.
    *
    * @see java.sql.Types                                                                                                       
    */
    public abstract List<String> getAlterColumnTypeStatement(String tableName, String columnName, int sqlType);

    /**
    * @param tableName
    * @param columnName
    * @return
    */
    public abstract List<String> getDropNotNullConstraintStatement(String tableName, String columnName, int sqlType);

    public abstract List<String> getDropUniqueConstraintStatement(String tableName, String columnName, int sqlType);

    /**
    * Returns an sql statement that will drop the given table.
    *
    * @param tableName table to be dropped
    * @return a <code>String</code> representing a statement used to drop a table
    */
    public abstract String getDropTableStatement(String tableName);

    /**
    * Returns an sql statement that will drop a foreign key constraint on the given table.
    *
    * @param tableName a <code>String</code> representing the name of the table on which the constraint will be dropped.
    * @param fkConstraintName a <code>String</code> representing the name of the foreign key constraint.
    * @return a <code>String</code> representing a statement used to drop a fk.
    */
    public abstract String getDropFKConstraintStatement(String tableName, String fkConstraintName);

    /**
    * Returns an sql statement that will drop an index constraint from the given table.
    *
    * @param tableName a <code>String</code> representing the name of the table on which the constraint will be dropped.
    * @param indexName a <code>String</code> representing the name of the index.
    * @return a <code>String</code> representing a statement used to drop a fk.
    */
    public abstract String getDropIndexConstraintStatement(String tableName, String indexName, boolean unique);

    /**
    * Returns a db-specific column type for a given SQL column type
    * @param columnType a <code>int</code> an j
    * @return an <code>int</code> representing the SQL for the column
    */
    public int getColumnType(int columnType) {
        return columnType;
    }

    /**
    * Returns an sql statement that will change the type of a db column.
    *
    * @param tableName  a <code>String</code> representing the name of the table for which the statement is executed.
    * @param columnName a <code>String</code> representing the name of the column for which the statement is executed.
    * @param newLength    an <code>int</code> representing new length.
    * @return a <code>String</code> representing a statement used to change the length of a column.
    *
    * @see java.sql.Types
    */
    public abstract List<String> getAlterTextColumnLengthStatement(String tableName, String columnName, int newLength);
}
