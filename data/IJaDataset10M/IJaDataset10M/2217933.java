package net.sourceforge.squirrel_sql.plugins.dbcopy.dialects;

import java.sql.Types;
import net.sourceforge.squirrel_sql.fw.sql.DatabaseObjectType;
import net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo;
import org.hibernate.dialect.Oracle9Dialect;

/**
 * A description of this class goes here...
 */
public class Oracle9iDialect extends Oracle9Dialect implements HibernateDialect {

    public Oracle9iDialect() {
        super();
        registerColumnType(Types.BIGINT, "number($p)");
        registerColumnType(Types.BINARY, 2000, "raw($l)");
        registerColumnType(Types.BINARY, "blob");
        registerColumnType(Types.BIT, "smallint");
        registerColumnType(Types.BLOB, "blob");
        registerColumnType(Types.BOOLEAN, "smallint");
        registerColumnType(Types.CHAR, 2000, "char($l)");
        registerColumnType(Types.CHAR, 4000, "varchar2($l)");
        registerColumnType(Types.CHAR, "clob");
        registerColumnType(Types.CLOB, "clob");
        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.DECIMAL, "decimal($p)");
        registerColumnType(Types.DOUBLE, "float($p)");
        registerColumnType(Types.FLOAT, "float($p)");
        registerColumnType(Types.INTEGER, "int");
        registerColumnType(Types.LONGVARBINARY, "blob");
        registerColumnType(Types.LONGVARCHAR, 4000, "varchar2($l)");
        registerColumnType(Types.LONGVARCHAR, "clob");
        registerColumnType(Types.NUMERIC, "number($p)");
        registerColumnType(Types.REAL, "real");
        registerColumnType(Types.SMALLINT, "smallint");
        registerColumnType(Types.TIME, "date");
        registerColumnType(Types.TIMESTAMP, "timestamp");
        registerColumnType(Types.TINYINT, "smallint");
        registerColumnType(Types.VARBINARY, "blob");
        registerColumnType(Types.VARCHAR, 4000, "varchar2(4000)");
        registerColumnType(Types.VARCHAR, "clob");
        registerColumnType(Types.OTHER, 4000, "varchar2(4000)");
        registerColumnType(Types.OTHER, "clob");
    }

    public boolean canPasteTo(IDatabaseObjectInfo info) {
        boolean result = true;
        DatabaseObjectType type = info.getDatabaseObjectType();
        if (type.getName().equalsIgnoreCase("database")) {
            result = false;
        }
        return result;
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
        if (dataType == Types.DOUBLE || dataType == Types.FLOAT) {
            return 53;
        } else {
            return 38;
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
        return "Oracle";
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
        if (databaseProductName.trim().toLowerCase().startsWith("oracle")) {
            return true;
        }
        return false;
    }
}
