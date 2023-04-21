package onepoint.persistence.sql;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used for DB2 statements.
 *
 * @author mihai.costin
 */
public class OpDB2Statement extends OpSqlStatement {

    /**
    * The mapping of DB2 specific types.
    */
    private static final Map DB_TYPES = new HashMap();

    private static final int MAX_VARCHAR_SIZE = 32672;

    /**
    * Populate the types map.
    */
    static {
        DB_TYPES.put(new Integer(Types.VARCHAR), "VARCHAR");
        DB_TYPES.put(new Integer(Types.BIGINT), "BIGINT");
        DB_TYPES.put(new Integer(Types.FLOAT), "DOUBLE");
        DB_TYPES.put(new Integer(Types.INTEGER), "INTEGER");
        DB_TYPES.put(new Integer(Types.DOUBLE), "DOUBLE");
        DB_TYPES.put(new Integer(Types.BIT), "SMALLINT");
        DB_TYPES.put(new Integer(Types.BOOLEAN), "SMALLINT");
        DB_TYPES.put(new Integer(Types.TINYINT), "SMALLINT");
        DB_TYPES.put(new Integer(Types.SMALLINT), "SMALLINT");
        DB_TYPES.put(new Integer(Types.BLOB), "BLOB(10M)");
        DB_TYPES.put(new Integer(Types.TIMESTAMP), "TIMESTAMP");
        DB_TYPES.put(new Integer(Types.DATE), "DATE");
    }

    /**
    * Instances of this class should only be obtained via the <code>OpSqlStatementFactory</code> class.
    */
    OpDB2Statement() {
    }

    /**
    * Returns an sql statement that will change the type of a db column.
    *
    * @param tableName  a <code>String</code> representing the name of the table for which the statement is executed.
    * @param columnName a <code>String</code> representing the name of the column for which the statement is executed.
    * @param sqlType    an <code>int</code> representing new SQL type of the column.
    * @return a <code>String</code> representing a statement used to change the type of a column type.
    * @see java.sql.Types
    */
    public List<String> getAlterColumnTypeStatement(String tableName, String columnName, int sqlType) {
        StringBuffer result = new StringBuffer();
        result.append("ALTER TABLE ");
        result.append(tableName.toUpperCase());
        result.append(" ALTER COLUMN ");
        result.append(columnName.toUpperCase());
        result.append(" SET DATA TYPE ");
        String columnType = (String) DB_TYPES.get(new Integer(sqlType));
        result.append(columnType);
        result.append(";");
        List<String> resultList = new ArrayList<String>();
        resultList.add(result.toString());
        return resultList;
    }

    /**
    * Returns an sql statement that will drop the given table.
    *
    * @param tableName table to be dropped
    * @return a <code>String</code> representing a statement used to drop a table
    */
    public String getDropTableStatement(String tableName) {
        StringBuffer result = new StringBuffer();
        result.append("DROP TABLE ");
        result.append(tableName);
        result.append(";");
        return result.toString();
    }

    /**
    * Returns an sql statement that will drop a foreign key constraint on the given table.
    *
    * @param tableName        a <code>String</code> representing the name of the table on which the constraint will be dropped.
    * @param fkConstraintName a <code>String</code> representing the name of the foreign key constraint.
    * @return a <code>String</code> representing a statement used to drop a fk.
    */
    public String getDropFKConstraintStatement(String tableName, String fkConstraintName) {
        StringBuffer result = new StringBuffer();
        result.append("ALTER TABLE ");
        result.append(tableName);
        result.append(" DROP FOREIGN KEY ");
        result.append(fkConstraintName);
        result.append(";");
        return result.toString();
    }

    /**
    * Returns an sql statement that will drop an index constraint from the given table.
    *
    * @param tableName a <code>String</code> representing the name of the table on which the constraint will be dropped.
    * @param indexName a <code>String</code> representing the name of the index.
    * @return a <code>String</code> representing a statement used to drop a fk.
    */
    public String getDropIndexConstraintStatement(String tableName, String indexName, boolean unique) {
        StringBuffer result = new StringBuffer();
        if (unique) {
            result.append("ALTER TABLE ");
            result.append(tableName.toUpperCase());
            result.append(" DROP CONSTRAINT ");
            result.append(indexName.toUpperCase());
            result.append(";");
        } else {
            result.append(" DROP INDEX ");
            result.append(indexName.toUpperCase());
            result.append(";");
        }
        return result.toString();
    }

    /**
    * @see onepoint.persistence.sql.OpSqlStatement#getAlterColumnTypeStatement(String, String, int)
    */
    @Override
    public List<String> getAlterTextColumnLengthStatement(String tableName, String columnName, int newLength) {
        StringBuffer result = new StringBuffer();
        result.append("ALTER TABLE ");
        result.append(tableName.toUpperCase());
        result.append(" ALTER COLUMN ");
        result.append(columnName.toUpperCase());
        result.append(" SET DATA TYPE ");
        int length = newLength > MAX_VARCHAR_SIZE ? MAX_VARCHAR_SIZE : newLength;
        String columnType = " VARCHAR(" + length + ")";
        result.append(columnType);
        result.append(";");
        List<String> resultList = new ArrayList<String>();
        resultList.add(result.toString());
        return resultList;
    }

    @Override
    public List<String> getDropNotNullConstraintStatement(String tableName, String columnName, int sqlType) {
        StringBuffer result = new StringBuffer();
        result.append("ALTER TABLE ");
        result.append(tableName.toUpperCase());
        result.append(" ALTER COLUMN ");
        result.append(columnName.toUpperCase());
        result.append(" DROP NOT NULL;");
        List<String> resultList = new ArrayList<String>();
        resultList.add(result.toString());
        return resultList;
    }

    @Override
    public List<String> getDropUniqueConstraintStatement(String tableName, String columnName, int sqlType) {
        return null;
    }
}
