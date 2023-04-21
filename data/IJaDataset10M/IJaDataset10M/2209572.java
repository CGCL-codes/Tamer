package com.technoetic.tornado;

import org.apache.log4j.Category;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class TypeConverter {

    private static final int BUFFER_SIZE = 4096;

    private static final Category log = Category.getInstance("persistence.jdbc");

    private static byte[] buffer = new byte[BUFFER_SIZE];

    TypeConverter() {
    }

    public static Object toJavaType(ResultSet rs, ColumnMapping columnMapping, int columnIndex) throws PersistenceException {
        Object value = null;
        try {
            if (columnMapping.fieldType == ColumnMapping.SINGLE_COLUMN_SUBOBJECT) {
                value = columnMapping.converter.toJavaObject(rs.getObject(columnIndex));
            } else {
                switch(columnMapping.columnType) {
                    case java.sql.Types.DOUBLE:
                        value = new Double(rs.getDouble(columnIndex));
                        break;
                    case java.sql.Types.INTEGER:
                        value = new Integer(rs.getInt(columnIndex));
                        break;
                    case java.sql.Types.BIGINT:
                        value = new Long(rs.getLong(columnIndex));
                        break;
                    case java.sql.Types.SMALLINT:
                        value = new Short(rs.getShort(columnIndex));
                        break;
                    case java.sql.Types.BIT:
                        value = new Boolean(rs.getBoolean(columnIndex));
                        break;
                    case java.sql.Types.VARCHAR:
                        value = rs.getString(columnIndex);
                        break;
                    case java.sql.Types.LONGVARBINARY:
                        try {
                            ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
                            InputStream inputStream = rs.getBinaryStream(columnIndex);
                            if (inputStream == null) {
                                value = "";
                            } else {
                                int n;
                                while ((n = inputStream.read(buffer)) != -1) {
                                    byteOutputStream.write(buffer, 0, n);
                                }
                                if (columnMapping.fieldType == String.class) {
                                    value = new String(byteOutputStream.toByteArray());
                                } else {
                                    value = byteOutputStream.toByteArray();
                                }
                                byteOutputStream.close();
                                inputStream.close();
                            }
                        } catch (IOException ex) {
                            log.error(ex);
                            value = "";
                        }
                        break;
                    case java.sql.Types.TIMESTAMP:
                        if (rs.getTimestamp(columnIndex) != null) {
                            value = new java.util.Date(rs.getTimestamp(columnIndex).getTime());
                        }
                        break;
                    default:
                        value = rs.getObject(columnIndex);
                        break;
                }
            }
            return value;
        } catch (SQLException ex) {
            Object rsObject = null;
            try {
                rsObject = rs.getObject(columnIndex);
            } catch (SQLException e) {
            }
            throw new PersistenceException("Conversion error" + ": rs.object=" + rsObject + ", fieldName=" + columnMapping.fieldName + ", fieldType=" + columnMapping.fieldType + ", columnName=" + columnMapping.columnName + ", columnType=" + columnMapping.columnType, ex);
        }
    }

    public static void bindSQLType(PreparedStatement statement, ColumnMapping columnMapping, int paramIndex, Object javaObject) throws PersistenceException {
        try {
            switch(columnMapping.columnType) {
                case java.sql.Types.LONGVARBINARY:
                    byte[] bytes;
                    if (javaObject instanceof String) {
                        bytes = ((String) javaObject).getBytes();
                    } else {
                        bytes = (byte[]) javaObject;
                    }
                    ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
                    statement.setBinaryStream(paramIndex, byteInputStream, bytes.length);
                    break;
                case java.sql.Types.SMALLINT:
                    statement.setShort(paramIndex, ((Short) javaObject).shortValue());
                    break;
                case java.sql.Types.DATE:
                    Date date;
                    if (javaObject instanceof java.util.Date) {
                        date = new java.sql.Date(((java.util.Date) javaObject).getTime());
                    } else {
                        date = (Date) javaObject;
                    }
                    statement.setDate(paramIndex, date);
                    break;
                case java.sql.Types.TIMESTAMP:
                    statement.setTimestamp(paramIndex, new Timestamp(((java.util.Date) javaObject).getTime()));
                    break;
                default:
                    if (columnMapping.fieldType == ColumnMapping.SINGLE_COLUMN_SUBOBJECT) {
                        javaObject = columnMapping.converter.toSqlObject(javaObject);
                    }
                    statement.setObject(paramIndex, javaObject);
            }
        } catch (SQLException ex) {
            throw new PersistenceException("Conversion error" + ": object=" + javaObject + ", fieldName=" + columnMapping.fieldName + ", fieldType=" + columnMapping.fieldType + ", columnName=" + columnMapping.columnName + ", columnType=" + columnMapping.columnType + ", paramIndex=" + paramIndex, ex);
        }
    }
}
