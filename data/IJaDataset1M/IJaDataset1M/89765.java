package com.avdheshyadav.p4j.jdbc.common;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * This class is Thread Safe.
 * 
 * @author Avdhesh yadav
 */
public class JdbcSqlUtil {

    /**
	 * 
	 * @param columnType int
	 * @param parameterIndex int
	 * @param obj Object
	 * @param pstmt PreparedStatement
	 * 
	 * @throws Exception
	 */
    public static void javaToSql(int columnType, int parameterIndex, Object obj, PreparedStatement pstmt) throws SQLException {
        switch(columnType) {
            case java.sql.Types.BIT:
                {
                    if (obj != null) pstmt.setBoolean(parameterIndex, ((Boolean) obj)); else pstmt.setBoolean(parameterIndex, Boolean.FALSE);
                }
                break;
            case java.sql.Types.TINYINT:
                {
                    if (obj != null) pstmt.setByte(parameterIndex, ((Byte) obj)); else pstmt.setByte(parameterIndex, ((byte) 0));
                }
                break;
            case java.sql.Types.SMALLINT:
                {
                    if (obj != null) pstmt.setShort(parameterIndex, ((Short) obj)); else pstmt.setShort(parameterIndex, ((short) 0));
                }
                break;
            case java.sql.Types.INTEGER:
                {
                    if (obj != null) pstmt.setInt(parameterIndex, ((Integer) obj)); else pstmt.setInt(parameterIndex, 0);
                }
                break;
            case java.sql.Types.BIGINT:
                {
                    if (obj != null) pstmt.setLong(parameterIndex, ((Long) obj)); else pstmt.setLong(parameterIndex, 0L);
                }
                break;
            case (java.sql.Types.FLOAT):
                {
                    if (obj != null) pstmt.setFloat(parameterIndex, ((Float) obj)); else pstmt.setFloat(parameterIndex, (float) 0.0);
                }
                break;
            case java.sql.Types.REAL:
                {
                    if (obj != null) pstmt.setFloat(parameterIndex, ((Float) obj)); else pstmt.setFloat(parameterIndex, (float) 0.0);
                }
                break;
            case java.sql.Types.DOUBLE:
                {
                    if (obj != null) pstmt.setDouble(parameterIndex, (Double) obj); else pstmt.setDouble(parameterIndex, 0.0);
                }
                break;
            case java.sql.Types.NUMERIC:
                {
                    pstmt.setBigDecimal(parameterIndex, (BigDecimal) obj);
                }
                break;
            case java.sql.Types.DECIMAL:
                {
                    pstmt.setBigDecimal(parameterIndex, (BigDecimal) obj);
                }
                break;
            case java.sql.Types.CHAR:
                {
                    pstmt.setString(parameterIndex, (String) obj);
                }
                break;
            case java.sql.Types.VARCHAR:
                {
                    pstmt.setString(parameterIndex, (String) obj);
                }
                break;
            case java.sql.Types.LONGVARCHAR:
                {
                    pstmt.setString(parameterIndex, (String) obj);
                }
                break;
            case java.sql.Types.DATE:
                {
                    pstmt.setDate(parameterIndex, (Date) obj);
                }
                break;
            case java.sql.Types.TIME:
                {
                    pstmt.setTime(parameterIndex, (Time) obj);
                }
                break;
            case java.sql.Types.TIMESTAMP:
                {
                    pstmt.setTimestamp(parameterIndex, (Timestamp) obj);
                }
                break;
            case java.sql.Types.BINARY:
                {
                    pstmt.setBinaryStream(parameterIndex, (InputStream) obj);
                }
                break;
            case java.sql.Types.JAVA_OBJECT:
                {
                    pstmt.setObject(parameterIndex, obj);
                }
                break;
            case java.sql.Types.STRUCT:
                {
                    pstmt.setObject(parameterIndex, obj);
                }
                break;
            case java.sql.Types.CLOB:
                {
                    pstmt.setClob(parameterIndex, (Clob) obj);
                }
                break;
            case java.sql.Types.BLOB:
                {
                    pstmt.setBlob(parameterIndex, (Blob) obj);
                }
                break;
            case java.sql.Types.BOOLEAN:
                {
                    if (obj != null) pstmt.setBoolean(parameterIndex, ((Boolean) obj)); else pstmt.setBoolean(parameterIndex, Boolean.FALSE);
                }
                break;
            case java.sql.Types.REF:
                {
                    pstmt.setRef(parameterIndex, (Ref) obj);
                }
                break;
        }
    }

    /**
	 * 
	 * @param resultSet ResultSet
	 * @param columnType 
	 * @param columnName String
	 * @param args  Object
	 * 
	 * @return Object
	 * 
	 * @throws Exception
	 */
    public static Object sqlToJava(ResultSet resultSet, int columnType, String columnName) throws SQLException {
        Object obj = null;
        switch(columnType) {
            case java.sql.Types.BIT:
                {
                    Boolean it = (Boolean) resultSet.getBoolean(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.TINYINT:
                {
                    Integer it = (Integer) resultSet.getInt(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.SMALLINT:
                {
                    Integer it = (Integer) resultSet.getInt(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.INTEGER:
                {
                    Integer it = (Integer) resultSet.getInt(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.BIGINT:
                {
                    Long it = (Long) resultSet.getLong(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.FLOAT:
                {
                    Float it = (Float) resultSet.getFloat(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.LONGVARCHAR:
                {
                    String it = (String) resultSet.getString(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.DOUBLE:
                {
                    Double it = (Double) resultSet.getDouble(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.CHAR:
                {
                    String it = (String) resultSet.getString(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.VARCHAR:
                {
                    String it = (String) resultSet.getString(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.DATE:
                {
                    Date it = (Date) resultSet.getDate(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.TIME:
                {
                    Time it = (Time) resultSet.getTime(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.TIMESTAMP:
                {
                    Timestamp it = (Timestamp) resultSet.getTimestamp(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.JAVA_OBJECT:
                {
                    Object it = (Object) resultSet.getObject(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.CLOB:
                {
                    Clob it = (Clob) resultSet.getClob(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.BLOB:
                {
                    Blob it = (Blob) resultSet.getBlob(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.BOOLEAN:
                {
                    Boolean it = (Boolean) resultSet.getBoolean(columnName);
                    obj = it;
                }
                break;
            case java.sql.Types.REF:
                {
                    Ref it = (Ref) resultSet.getRef(columnName);
                    obj = it;
                }
                break;
        }
        return obj;
    }
}
