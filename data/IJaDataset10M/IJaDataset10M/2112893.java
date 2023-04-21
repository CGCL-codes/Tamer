package in.co.daffodil.db.jdbc;

import java.sql.*;
import java.math.BigDecimal;
import com.daffodilwoods.daffodildb.utils.field.*;
import com.daffodilwoods.database.resource.*;

/**
 * Correspondance between Java Primitive Types and JDBC Types
 */
public class DTConvertor {

    static final int BOOLEAN = 16;

    static DTConvertor dtConvertor;

    static {
        dtConvertor = new DTConvertor();
    }

    private DTConvertor() {
    }

    public static DTConvertor getInstance() {
        return dtConvertor;
    }

    public Object getObject(Object obj, int type) throws SQLException {
        if (obj == null) return null;
        try {
            if (obj.getClass().getName().equalsIgnoreCase("com.daffodilwoods.daffodildb.utils.field.FieldBase")) {
                throw new SQLException("Invalid Object  is passed for Conversion");
            }
            switch(type) {
                case Types.TINYINT:
                case Types.SMALLINT:
                case Types.INTEGER:
                    return getInteger(obj);
                case Types.BIGINT:
                    return getBigInt(obj);
                case Types.REAL:
                    return getReal(obj);
                case Types.FLOAT:
                    return getFloat(obj);
                case Types.DOUBLE:
                    return getDouble(obj);
                case Types.DECIMAL:
                    Object a = getDecimal(obj);
                    return a;
                case Types.NUMERIC:
                    return getNumeric(obj);
                case BOOLEAN:
                    return getBoolean(obj);
                case Types.BIT:
                    return getBit(obj);
                case Types.CHAR:
                    return getChar(obj);
                case Types.VARCHAR:
                    return getVarchar(obj);
                case Types.LONGVARCHAR:
                    return getLongVarchar(obj);
                case Types.BINARY:
                    return getBinary(obj);
                case Types.VARBINARY:
                    return getVarBinary(obj);
                case Types.LONGVARBINARY:
                    return getLongVarBinary(obj);
                case Types.DATE:
                    return getDate(obj);
                case Types.TIME:
                    return getTime(obj);
                case Types.TIMESTAMP:
                    return getTimeStamp(obj);
                case Types.CLOB:
                    return getClob(obj);
                case Types.BLOB:
                    return getBlob(obj);
                case Types.JAVA_OBJECT:
                    return getJavaObject(obj);
            }
            return obj;
        } catch (SQLException sqe) {
            throw sqe;
        }
    }

    private Byte getTinyInt(Object obj) throws SQLException {
        if (obj instanceof Byte) return ((Byte) obj);
        if (obj instanceof Short) return new Byte(((Short) obj).byteValue());
        if (obj instanceof Integer) return new Byte(((Integer) obj).byteValue());
        if (obj instanceof Long) return new Byte(((Long) obj).byteValue());
        if (obj instanceof Float) return new Byte(((Float) obj).byteValue());
        if (obj instanceof Double) return new Byte(((Double) obj).byteValue());
        if (obj instanceof BigDecimal) return new Byte(((BigDecimal) obj).byteValue());
        if (obj instanceof Boolean) return Boolean.TRUE.equals(obj) ? new Byte((byte) 1) : new Byte((byte) 0);
        if (obj instanceof String) {
            String st = ((String) obj).trim();
            return new Byte(st);
        }
        throw new SQLException(obj.getClass() + " can't be conveted to Byte");
    }

    private Short getSmallInt(Object obj) throws SQLException {
        if (obj instanceof Byte) return new Short(((Byte) obj).shortValue());
        if (obj instanceof Short) return ((Short) obj);
        if (obj instanceof Integer) return new Short(((Integer) obj).shortValue());
        if (obj instanceof Long) return new Short(((Long) obj).shortValue());
        if (obj instanceof Float) return new Short(((Float) obj).shortValue());
        if (obj instanceof Double) return new Short(((Double) obj).shortValue());
        if (obj instanceof BigDecimal) return new Short(((BigDecimal) obj).shortValue());
        if (obj instanceof Boolean) return Boolean.TRUE.equals(obj) ? new Short((short) 1) : new Short((short) 0);
        if (obj instanceof String) {
            String st = ((String) obj).trim();
            return new Short(st);
        }
        throw new SQLException(obj.getClass() + " can't be conveted to Short");
    }

    private Integer getInteger(Object obj) throws SQLException {
        if (obj instanceof Byte) return new Integer(((Byte) obj).intValue());
        if (obj instanceof Short) return new Integer(((Short) obj).intValue());
        if (obj instanceof Integer) return (Integer) obj;
        if (obj instanceof Long) return new Integer(((Long) obj).intValue());
        if (obj instanceof Float) return new Integer(((Float) obj).intValue());
        if (obj instanceof Double) return new Integer(((Double) obj).intValue());
        if (obj instanceof BigDecimal) return new Integer(((BigDecimal) obj).intValue());
        if (obj instanceof Boolean) return Boolean.TRUE.equals(obj) ? new Integer((int) 1) : new Integer((int) 0);
        if (obj instanceof String) {
            String st = ((String) obj).trim();
            return new Integer(st);
        }
        throw new SQLException(obj.getClass() + " can't be conveted to Integer");
    }

    private Long getBigInt(Object obj) throws SQLException {
        if (obj instanceof Byte) return new Long(((Byte) obj).longValue());
        if (obj instanceof Short) return new Long(((Short) obj).longValue());
        if (obj instanceof Integer) return new Long(((Integer) obj).longValue());
        if (obj instanceof Long) return new Long(((Long) obj).longValue());
        if (obj instanceof Float) return new Long(((Float) obj).longValue());
        if (obj instanceof Double) return new Long(((Double) obj).longValue());
        if (obj instanceof BigDecimal) return new Long(((BigDecimal) obj).longValue());
        if (obj instanceof Boolean) return Boolean.TRUE.equals(obj) ? new Long((long) 1) : new Long((long) 0);
        if (obj instanceof String) {
            String st = ((String) obj).trim();
            return new Long(st);
        }
        throw new SQLException(obj.getClass() + " can't be conveted to Long");
    }

    private Float getReal(Object obj) throws SQLException {
        if (obj instanceof Byte) return new Float(((Byte) obj).floatValue());
        if (obj instanceof Short) return new Float(((Short) obj).floatValue());
        if (obj instanceof Integer) return new Float(((Integer) obj).floatValue());
        if (obj instanceof Long) return new Float(((Long) obj).floatValue());
        if (obj instanceof Float) return ((Float) obj);
        if (obj instanceof Double) return new Float(((Double) obj).floatValue());
        if (obj instanceof BigDecimal) return new Float(((BigDecimal) obj).floatValue());
        if (obj instanceof Boolean) return Boolean.TRUE.equals(obj) ? new Float((float) 1) : new Float((float) 0);
        if (obj instanceof String) {
            String st = ((String) obj).trim();
            return new Float(st);
        }
        throw new SQLException(obj.getClass() + " can't be conveted to Float");
    }

    private Double getFloat(Object obj) throws SQLException {
        if (obj instanceof Byte) return new Double(((Byte) obj).doubleValue());
        if (obj instanceof Short) return new Double(((Short) obj).doubleValue());
        if (obj instanceof Integer) return new Double(((Integer) obj).doubleValue());
        if (obj instanceof Long) return new Double(((Long) obj).doubleValue());
        if (obj instanceof Float) return new Double(((Float) obj).doubleValue());
        if (obj instanceof Double) return ((Double) obj);
        if (obj instanceof BigDecimal) return new Double(((BigDecimal) obj).doubleValue());
        if (obj instanceof Boolean) return Boolean.TRUE.equals(obj) ? new Double((double) 1) : new Double((double) 0);
        if (obj instanceof String) {
            String st = ((String) obj).trim();
            return new Double(st);
        }
        throw new SQLException(obj.getClass() + " can't be conveted to Double");
    }

    private Double getDouble(Object obj) throws SQLException {
        if (obj instanceof Byte) return new Double(((Byte) obj).doubleValue());
        if (obj instanceof Short) return new Double(((Short) obj).doubleValue());
        if (obj instanceof Integer) return new Double(((Integer) obj).doubleValue());
        if (obj instanceof Long) return new Double(((Long) obj).doubleValue());
        if (obj instanceof Float) return new Double(((Float) obj).doubleValue());
        if (obj instanceof Double) return ((Double) obj);
        if (obj instanceof BigDecimal) return new Double(((BigDecimal) obj).doubleValue());
        if (obj instanceof Boolean) return Boolean.TRUE.equals(obj) ? new Double((double) 1) : new Double((double) 0);
        if (obj instanceof String) {
            String st = ((String) obj).trim();
            return new Double(st);
        }
        throw new SQLException(obj.getClass() + " can't be conveted to Double");
    }

    private BigDecimal getDecimal(Object obj) throws SQLException {
        if (obj instanceof Byte) return new BigDecimal(((Byte) obj).doubleValue());
        if (obj instanceof Short) return new BigDecimal(((Short) obj).doubleValue());
        if (obj instanceof Integer) return new BigDecimal(((Integer) obj).doubleValue());
        if (obj instanceof Long) return new BigDecimal(((Long) obj).doubleValue());
        if (obj instanceof Float) return new BigDecimal(((Float) obj).doubleValue());
        if (obj instanceof Double) return new BigDecimal(((Double) obj).doubleValue());
        if (obj instanceof BigDecimal) return (BigDecimal) obj;
        if (obj instanceof Boolean) return Boolean.TRUE.equals(obj) ? new BigDecimal((double) 1) : new BigDecimal((double) 0);
        if (obj instanceof String) {
            String st = ((String) obj).trim();
            return new BigDecimal(st);
        }
        throw new SQLException(obj.getClass() + " can't be conveted to BigDecimal");
    }

    private BigDecimal getNumeric(Object obj) throws SQLException {
        if (obj instanceof Byte) return new BigDecimal(((Byte) obj).doubleValue());
        if (obj instanceof Short) return new BigDecimal(((Short) obj).doubleValue());
        if (obj instanceof Integer) return new BigDecimal(((Integer) obj).doubleValue());
        if (obj instanceof Long) return new BigDecimal(((Long) obj).doubleValue());
        if (obj instanceof Float) return new BigDecimal(((Float) obj).doubleValue());
        if (obj instanceof Double) return new BigDecimal(((Double) obj).doubleValue());
        if (obj instanceof BigDecimal) return (BigDecimal) obj;
        if (obj instanceof Boolean) return Boolean.TRUE.equals(obj) ? new BigDecimal((double) 1) : new BigDecimal((double) 0);
        if (obj instanceof String) {
            String st = ((String) obj).trim();
            return new BigDecimal(st);
        }
        throw new SQLException(obj.getClass() + " can't be conveted to BigDecimal");
    }

    private Boolean getBit(Object obj) throws SQLException {
        if (obj instanceof Byte) return Utilities.getBooleanValue(obj.hashCode() != 0);
        if (obj instanceof Short) return new Boolean(obj.hashCode() != 0);
        if (obj instanceof Integer) return new Boolean(obj.hashCode() != 0);
        if (obj instanceof Long) return new Boolean(obj.hashCode() != 0);
        if (obj instanceof Float) return new Boolean(obj.hashCode() != 0);
        if (obj instanceof Double) return new Boolean(obj.hashCode() != 0);
        if (obj instanceof BigDecimal) {
            BigDecimal bd = (BigDecimal) obj;
            BigDecimal bd1 = new BigDecimal(0);
            return new Boolean(bd.compareTo(bd1) != 0);
        }
        if (obj instanceof Boolean) return ((Boolean) obj);
        if (obj instanceof String) {
            String str = (String) obj;
            str = str.trim();
            return new Boolean(!(str.equalsIgnoreCase("false") || str.equalsIgnoreCase("0")));
        }
        throw new SQLException(obj.getClass() + " can't be conveted to Boolean");
    }

    private Boolean getBoolean(Object obj) throws SQLException {
        if (obj instanceof Byte) return Utilities.getBooleanValue(obj.hashCode() != 0);
        if (obj instanceof Short) return new Boolean(obj.hashCode() != 0);
        if (obj instanceof Integer) return new Boolean(obj.hashCode() != 0);
        if (obj instanceof Long) return new Boolean(obj.hashCode() != 0);
        if (obj instanceof Float) return new Boolean(obj.hashCode() != 0);
        if (obj instanceof Double) return new Boolean(obj.hashCode() != 0);
        if (obj instanceof BigDecimal) {
            BigDecimal bd = (BigDecimal) obj;
            BigDecimal bd1 = new BigDecimal(0);
            return new Boolean(bd.compareTo(bd1) != 0);
        }
        if (obj instanceof Boolean) return ((Boolean) obj);
        if (obj instanceof String) {
            String str = (String) obj;
            str = str.trim();
            return new Boolean(!(str.equalsIgnoreCase("false") || str.equalsIgnoreCase("0")));
        }
        throw new SQLException(obj.getClass() + " can't be conveted to Boolean");
    }

    private String getChar(Object obj) throws SQLException {
        if (obj instanceof Byte) return ((Byte) obj).toString();
        if (obj instanceof Short) return ((Short) obj).toString();
        if (obj instanceof Integer) return ((Integer) obj).toString();
        if (obj instanceof Long) return ((Long) obj).toString();
        if (obj instanceof Float) return ((Float) obj).toString();
        if (obj instanceof Double) return ((Double) obj).toString();
        if (obj instanceof BigDecimal) return ((BigDecimal) obj).toString();
        if (obj instanceof Boolean) return ((Boolean) obj).toString();
        if (obj instanceof String) return (String) obj;
        if (obj instanceof java.sql.Date) return ((Date) obj).toString();
        if (obj instanceof java.sql.Time) return ((Time) obj).toString();
        if (obj instanceof java.sql.Timestamp) return ((Timestamp) obj).toString();
        if (obj instanceof Clob) {
            return ((Clob) obj).getSubString(1, (int) ((Clob) obj).length());
        }
        throw new SQLException(obj.getClass() + " can't be conveted to String");
    }

    private String getVarchar(Object obj) throws SQLException {
        if (obj instanceof Byte) return ((Byte) obj).toString();
        if (obj instanceof Short) return ((Short) obj).toString();
        if (obj instanceof Integer) return ((Integer) obj).toString();
        if (obj instanceof Long) return ((Long) obj).toString();
        if (obj instanceof Float) return ((Float) obj).toString();
        if (obj instanceof Double) return ((Double) obj).toString();
        if (obj instanceof BigDecimal) return ((BigDecimal) obj).toString();
        if (obj instanceof Boolean) return ((Boolean) obj).toString();
        if (obj instanceof String) return (String) obj;
        if (obj instanceof java.sql.Date) return ((Date) obj).toString();
        if (obj instanceof java.sql.Time) return ((Time) obj).toString();
        if (obj instanceof java.sql.Timestamp) return ((Timestamp) obj).toString();
        if (obj instanceof Clob) {
            return ((Clob) obj).getSubString(1, (int) ((Clob) obj).length());
        }
        if (obj instanceof Blob) {
            return ((Blob) obj).getClass() + "";
        }
        throw new SQLException(obj.getClass() + " can't be conveted to String");
    }

    private String getLongVarchar(Object obj) throws SQLException {
        if (obj instanceof Byte) return ((Byte) obj).toString();
        if (obj instanceof Short) return ((Short) obj).toString();
        if (obj instanceof Integer) return ((Integer) obj).toString();
        if (obj instanceof Long) return ((Long) obj).toString();
        if (obj instanceof Float) return ((Float) obj).toString();
        if (obj instanceof Double) return ((Double) obj).toString();
        if (obj instanceof BigDecimal) return ((BigDecimal) obj).toString();
        if (obj instanceof Boolean) return ((Boolean) obj).toString();
        if (obj instanceof String) return (String) obj;
        if (obj instanceof java.sql.Date) return ((Date) obj).toString();
        if (obj instanceof java.sql.Time) return ((Time) obj).toString();
        if (obj instanceof java.sql.Timestamp) return ((Timestamp) obj).toString();
        if (obj instanceof Clob) {
            return ((Clob) obj).getSubString(1, (int) ((Clob) obj).length());
        }
        throw new SQLException(obj.getClass() + " can't be conveted to String");
    }

    private byte[] getBinary(Object obj) throws SQLException {
        if (obj instanceof String) return ((String) obj).getBytes();
        if (obj instanceof byte[]) return (byte[]) obj;
        if (obj instanceof Blob) return ((Blob) obj).getBytes(1, (int) ((Blob) obj).length());
        if (obj instanceof Clob) return ((Clob) obj).getSubString(1, (int) ((Clob) obj).length()).getBytes();
        throw new SQLException(obj.getClass() + " can't be conveted to byte[]");
    }

    private byte[] getVarBinary(Object obj) throws SQLException {
        if (obj instanceof String) return ((String) obj).getBytes();
        if (obj instanceof byte[]) return (byte[]) obj;
        if (obj instanceof Blob) return ((Blob) obj).getBytes(1, (int) ((Blob) obj).length());
        if (obj instanceof Clob) return ((Clob) obj).getSubString(1, (int) ((Clob) obj).length()).getBytes();
        throw new SQLException(obj.getClass() + " can't be conveted to byte[]");
    }

    private byte[] getLongVarBinary(Object obj) throws SQLException {
        if (obj instanceof String) return ((String) obj).getBytes();
        if (obj instanceof byte[]) return (byte[]) obj;
        if (obj instanceof Blob) return ((Blob) obj).getBytes(1, (int) ((Blob) obj).length());
        if (obj instanceof Clob) return ((Clob) obj).getSubString(1, (int) ((Clob) obj).length()).getBytes();
        throw new SQLException(obj.getClass() + " can't be conveted to byte[]");
    }

    private Date getDate(Object obj) throws SQLException {
        if (obj instanceof String) {
            String st = ((String) obj).trim();
            return Date.valueOf(st);
        }
        if (obj instanceof Date) return ((Date) obj);
        if (obj instanceof java.sql.Timestamp) {
            String sMfgStringVal = obj.toString();
            sMfgStringVal = sMfgStringVal.substring(0, sMfgStringVal.indexOf(' '));
            return java.sql.Date.valueOf(sMfgStringVal);
        }
        throw new SQLException(obj.getClass() + " can't be conveted to Date");
    }

    private Time getTime(Object obj) throws SQLException {
        if (obj instanceof String) {
            String st = ((String) obj).trim();
            return Time.valueOf(st);
        }
        if (obj instanceof java.sql.Time) return ((Time) obj);
        if (obj instanceof java.sql.Timestamp) return new Time(((Timestamp) obj).getTime());
        throw new SQLException(obj.getClass() + " can't be conveted to Time");
    }

    private Timestamp getTimeStamp(Object obj) throws SQLException {
        if (obj instanceof String) {
            String st = ((String) obj).trim();
            return Timestamp.valueOf(st);
        }
        if (obj instanceof Date) return new Timestamp(((Date) obj).getTime());
        if (obj instanceof Time) return new Timestamp(((Time) obj).getTime());
        if (obj instanceof java.sql.Timestamp) return (Timestamp) obj;
        throw new SQLException(obj.getClass() + " can't be conveted to Timestamp");
    }

    private Clob getClob(Object obj) throws SQLException {
        if (obj instanceof java.sql.Clob) return ((Clob) obj);
        throw new SQLException(obj.getClass() + " can't be conveted to Clob");
    }

    private Blob getBlob(Object obj) throws SQLException {
        if (obj instanceof java.sql.Blob) return ((Blob) obj);
        throw new SQLException(obj.getClass() + " can't be conveted to Blob");
    }

    private Object getJavaObject(Object obj) {
        return obj;
    }
}
