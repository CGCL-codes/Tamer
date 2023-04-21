package org.intellij.ibatis.model;

import java.util.Map;
import java.util.HashMap;
import java.sql.Types;

/**
 * jdbc type in iBATIS
 *
 * @author Jacky
 */
public class JdbcType {

    public static Map<String, Integer> TYPES = new HashMap<String, Integer>();

    private String name;

    static {
        TYPES = new HashMap<String, Integer>();
        TYPES.put("ARRAY", Types.ARRAY);
        TYPES.put("BIGINT", Types.BIGINT);
        TYPES.put("BINARY", Types.BINARY);
        TYPES.put("BIT", Types.BIT);
        TYPES.put("BLOB", Types.BLOB);
        TYPES.put("BOOLEAN", Types.BOOLEAN);
        TYPES.put("CHAR", Types.CHAR);
        TYPES.put("CLOB", Types.CLOB);
        TYPES.put("DATALINK", Types.DATALINK);
        TYPES.put("DATE", Types.DATE);
        TYPES.put("DECIMAL", Types.DECIMAL);
        TYPES.put("DISTINCT", Types.DISTINCT);
        TYPES.put("DOUBLE", Types.DOUBLE);
        TYPES.put("FLOAT", Types.FLOAT);
        TYPES.put("INTEGER", Types.INTEGER);
        TYPES.put("JAVA_OBJECT", Types.JAVA_OBJECT);
        TYPES.put("LONGVARBINARY", Types.LONGVARBINARY);
        TYPES.put("LONGVARCHAR", Types.LONGVARCHAR);
        TYPES.put("NULL", Types.NULL);
        TYPES.put("NUMERIC", Types.NUMERIC);
        TYPES.put("OTHER", Types.OTHER);
        TYPES.put("REAL", Types.REAL);
        TYPES.put("REF", Types.REF);
        TYPES.put("SMALLINT", Types.SMALLINT);
        TYPES.put("STRUCT", Types.STRUCT);
        TYPES.put("TIME", Types.TIME);
        TYPES.put("TIMESTAMP", Types.TIMESTAMP);
        TYPES.put("TINYINT", Types.TINYINT);
        TYPES.put("VARBINARY", Types.VARBINARY);
        TYPES.put("VARCHAR", Types.VARCHAR);
        TYPES.put("CH", Types.CHAR);
        TYPES.put("VC", Types.VARCHAR);
        TYPES.put("DT", Types.DATE);
        TYPES.put("TM", Types.TIME);
        TYPES.put("TS", Types.TIMESTAMP);
        TYPES.put("NM", Types.NUMERIC);
        TYPES.put("II", Types.INTEGER);
        TYPES.put("BI", Types.BIGINT);
        TYPES.put("SI", Types.SMALLINT);
        TYPES.put("TI", Types.TINYINT);
        TYPES.put("DC", Types.DECIMAL);
        TYPES.put("DB", Types.DOUBLE);
        TYPES.put("FL", Types.FLOAT);
        TYPES.put("ORACLECURSOR", -10);
    }

    /**
     * construct jdbc type
     *
     * @param name jdbc type name in iBATIS
     */
    public JdbcType(String name) {
        this.name = name;
    }

    /**
     * get jdbc type name  in iBATIS
     *
     * @return jdbc type name
     */
    public String getName() {
        return this.name;
    }

    public boolean equals(Object jdbcType) {
        return jdbcType instanceof JdbcType && ((JdbcType) jdbcType).getName().equals(name);
    }

    /**
     * get integer value for jdbc type, please reference java.sql.Types
     *
     * @return integer value
     */
    public int getValue() {
        return TYPES.containsKey(name) ? TYPES.get(name) : Integer.MIN_VALUE;
    }
}
