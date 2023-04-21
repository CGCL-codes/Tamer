package com.oracle2hsqldb.dialect;

import net.sf.hibernate.MappingException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import com.oracle2hsqldb.Column;
import com.oracle2hsqldb.DefaultValue;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;

/**
 * @author Moses Hohman
 */
public class HSQLDialect extends GenericDialect {

    private static final net.sf.hibernate.dialect.Dialect HSQL_DIALECT = new net.sf.hibernate.dialect.HSQLDialect();

    private static Map TYPES_BY_NAME = new HashMap();

    private static Map TYPES_BY_TYPE = new HashMap();

    private static void registerType(String typeName, int type) {
        Integer objectType = new Integer(type);
        TYPES_BY_NAME.put(typeName, objectType);
        TYPES_BY_TYPE.put(objectType, typeName);
    }

    static {
        registerType("BIT", Types.BIT);
        registerType("INTEGER", Types.INTEGER);
        registerType("VARCHAR", Types.VARCHAR);
        registerType("CHAR", Types.CHAR);
        registerType("DATE", Types.DATE);
        registerType("NUMERIC", Types.NUMERIC);
        registerType("DECIMAL", Types.DECIMAL);
        registerType("TIMESTAMP", Types.TIMESTAMP);
        registerType("LONGVARCHAR", Types.CLOB);
        registerType("FLOAT", Types.FLOAT);
        registerType("LONGVARBINARY", Types.BLOB);
        registerType("CLOB", Types.CLOB);
    }

    public int getType(String dataTypeName) {
        if (!TYPES_BY_NAME.containsKey(dataTypeName)) throw new IllegalArgumentException("No registered type for name " + dataTypeName);
        return ((Integer) TYPES_BY_NAME.get(dataTypeName)).intValue();
    }

    public String getTypeName(int type) {
        Integer objectType = new Integer(type);
        if (!TYPES_BY_TYPE.containsKey(objectType)) throw new IllegalArgumentException("No registered type " + type);
        return (String) TYPES_BY_TYPE.get(objectType);
    }

    public String getTypeName(Column column) {
        if (column.isPrimaryKey() && (column.type() == Types.DECIMAL || column.type() == Types.NUMERIC)) {
            return "INTEGER";
        } else if (column.type() == Types.DECIMAL && column.size() == 1 && column.precision() == 0) {
            return "BIT";
        } else if (column.type() == Types.TIMESTAMP) {
            return "TIMESTAMP";
        } else if (column.type() == Types.CLOB) {
            return "VARCHAR";
        } else if (column.type() == Types.BLOB) {
            return "LONGVARBINARY";
        } else {
            try {
                return getTypeName(column.type());
            } catch (IllegalArgumentException e) {
                System.out.println("Problem getting type for column " + column.name() + " for table " + column.owner().name());
                throw e;
            }
        }
    }

    public int getSize(Column column) {
        if (column.type() == Types.FLOAT) {
            return Math.min(column.size(), 53);
        } else {
            return super.getSize(column);
        }
    }

    private static final String SYSDATE_STRING = "SYSDATE";

    public DefaultValue parseDefaultValue(String defaultValue, int type) {
        if (SYSDATE_STRING.equals(defaultValue)) return DefaultValue.NOW;
        return super.parseDefaultValue(defaultValue, type);
    }

    public String formatDefaultValue(Column column) {
        if (DefaultValue.NOW.equals(column.defaultValue())) return SYSDATE_STRING;
        return super.formatDefaultValue(column);
    }

    public boolean supportsIdentityColumns() {
        return HSQL_DIALECT.supportsIdentityColumns();
    }

    public boolean supportsViews() {
        return true;
    }

    public boolean supportsSequences() {
        return true;
    }

    public String getDriverClassName() {
        return "org.hsqldb.jdbcDriver";
    }

    public String getShutdownSql() {
        return "SHUTDOWN";
    }

    public String getNextSequenceValueSql(String sequenceName) {
        return new StringBuffer("NEXT VALUE FOR ").append(sequenceName).toString();
    }

    protected String internalGetIdentityColumnString() throws MappingException {
        return HSQL_DIALECT.getIdentityColumnString();
    }
}
