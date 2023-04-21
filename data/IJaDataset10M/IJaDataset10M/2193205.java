package com.fastcoreproject.common.db;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * Класс предназначен для установления соответствия типов, используемых в Java и СУБД
 */
public class FDBTypeMapper {

    private String[] currentTypeMapper = null;

    @SuppressWarnings("unchecked")
    private static final Class[] javaTypes = new Class[] { null, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Boolean.class, String.class };

    private static final String[] postgresTypeMapper = new String[] { "int", "smallint", "smallint", "integer", "bigint", "double precision", "double precision", "boolean", "text" };

    public FDBTypeMapper(String dbType) throws SQLFeatureNotSupportedException {
        if (dbType == null) throw new SQLFeatureNotSupportedException("Arrays are not supported for selected DB engine");
        if (dbType.equalsIgnoreCase(FDB.DB_SERVER_TYPE_POSTGRE)) {
            currentTypeMapper = postgresTypeMapper;
        } else {
            throw new SQLFeatureNotSupportedException("Arrays are not supported for selected DB engine");
        }
    }

    @SuppressWarnings("unchecked")
    public String getTypeName(Class classToMap) throws SQLException {
        int typeIdx = -1;
        for (int i = 0; i < javaTypes.length; i++) if (classToMap == javaTypes[i] || classToMap.equals(javaTypes[i])) {
            typeIdx = i;
            break;
        }
        if (typeIdx < 0) throw new SQLException("Class " + classToMap + " can't be used in array ");
        try {
            return currentTypeMapper[typeIdx];
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static String getTypeName(String dbType, Class classToMap) throws SQLException, SQLFeatureNotSupportedException {
        return new FDBTypeMapper(dbType).getTypeName(classToMap);
    }
}
