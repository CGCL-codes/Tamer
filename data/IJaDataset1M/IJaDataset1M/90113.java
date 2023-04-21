package org.avaje.ebean.server.persist;

import java.io.ByteArrayInputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.PersistenceException;
import org.avaje.ebean.server.plugin.PluginProperties;
import org.avaje.ebean.server.type.ScalarType;
import org.avaje.ebean.server.type.TypeManager;
import org.avaje.ebean.util.BindParams;
import org.avaje.ebean.util.Message;
import org.avaje.lib.log.LogFactory;

/**
 * Binds bean values to a PreparedStatement.
 */
public class Binder {

    private static final Logger logger = LogFactory.get(Binder.class);

    private final TypeManager typeManager;

    /**
	 * Set the PreparedStatement with which to bind variables to.
	 */
    public Binder(PluginProperties props, TypeManager typeManager) {
        this.typeManager = typeManager;
    }

    /**
	 * Bind the values to the Prepared Statement.
	 */
    public void bind(BindValues bindValues, PreparedStatement pstmt, StringBuilder bindBuf) throws SQLException {
        int index = 0;
        String logPrefix = "";
        ArrayList<BindValues.Value> list = bindValues.values();
        for (int i = 0; i < list.size(); i++) {
            BindValues.Value bindValue = (BindValues.Value) list.get(i);
            if (bindValue.isComment()) {
                if (bindBuf != null) {
                    bindBuf.append(bindValue.getName());
                    if (logPrefix.equals("")) {
                        logPrefix = ", ";
                    }
                }
            } else {
                Object val = bindValue.getValue();
                int dt = bindValue.getDbType();
                bindObject(++index, val, dt, pstmt);
                if (bindBuf != null) {
                    bindBuf.append(logPrefix);
                    if (logPrefix.equals("")) {
                        logPrefix = ", ";
                    }
                    bindBuf.append(bindValue.getName());
                    bindBuf.append("=");
                    if (isLob(dt)) {
                        bindBuf.append("[LOB]");
                    } else {
                        bindBuf.append(String.valueOf(val));
                    }
                }
            }
        }
    }

    /**
	 * Bind the list of positionedParameters in BindParams.
	 */
    public String bind(BindParams bindParams, int index, PreparedStatement pstmt) throws SQLException {
        StringBuilder bindLog = new StringBuilder();
        bind(bindParams, index, pstmt, bindLog);
        return bindLog.toString();
    }

    /**
	 * Bind the list of positionedParameters in BindParams.
	 */
    public void bind(BindParams bindParams, int index, PreparedStatement pstmt, StringBuilder bindLog) throws SQLException {
        bind(bindParams.positionedParameters(), index, pstmt, bindLog);
    }

    /**
	 * Bind the list of parameters..
	 */
    public void bind(List<BindParams.Param> list, int index, PreparedStatement pstmt, StringBuilder bindLog) throws SQLException {
        CallableStatement cstmt = null;
        if (pstmt instanceof CallableStatement) {
            cstmt = (CallableStatement) pstmt;
        }
        Object value = null;
        try {
            for (int i = 0; i < list.size(); i++) {
                BindParams.Param param = list.get(i);
                if (param.isOutParam() && cstmt != null) {
                    cstmt.registerOutParameter(++index, param.getType());
                    if (param.isInParam()) {
                        index--;
                    }
                }
                if (param.isInParam()) {
                    value = param.getInValue();
                    if (bindLog != null) {
                        bindLog.append(value);
                        bindLog.append(", ");
                    }
                    if (value == null) {
                        bindObject(++index, null, param.getType(), pstmt);
                    } else {
                        bindObject(++index, value, pstmt);
                    }
                }
            }
        } catch (SQLException ex) {
            logger.warning(Message.msg("fetch.bind.error", "" + (index - 1), value));
            throw ex;
        }
    }

    /**
	 * Bind an Object with unknown data type.
	 */
    public void bindObject(int index, Object value, PreparedStatement pstmt) throws SQLException {
        if (value == null) {
            bindObject(index, null, Types.OTHER, pstmt);
        } else {
            ScalarType type = typeManager.getScalarType(value.getClass());
            if (type == null) {
                String msg = "No ScalarType registered for " + value.getClass();
                throw new PersistenceException(msg);
            } else if (!type.isJdbcNative()) {
                value = type.toJdbcType(value);
            }
            int dbType = type.getJdbcType();
            bindObject(index, value, dbType, pstmt);
        }
    }

    /**
	 * bind a single value.
	 * <p>
	 * Note that java.math.BigInteger is supported by converting it to a Long.
	 * </p>
	 * <p>
	 * Note if we get a java.util.Date or java.util.Calendar then these have
	 * been anonymously passed in (UpdateSql etc). There is a global setting to
	 * convert then to a java.sql.Date or java.sql.Timestamp for binding. The
	 * default is that both are converted to java.sql.Timestamp.
	 * </p>
	 */
    public void bindObject(int index, Object data, int dbType, PreparedStatement pstmt) throws SQLException {
        if (data == null) {
            pstmt.setNull(index, dbType);
            return;
        }
        switch(dbType) {
            case java.sql.Types.LONGVARCHAR:
                bindLongVarChar(index, data, pstmt);
                break;
            case java.sql.Types.LONGVARBINARY:
                bindLongVarBinary(index, data, pstmt);
                break;
            case java.sql.Types.CLOB:
                bindClob(index, data, pstmt);
                break;
            case java.sql.Types.BLOB:
                bindBlob(index, data, pstmt);
                break;
            default:
                bindSimpleData(index, dbType, data, pstmt);
        }
    }

    /**
	 * Ensure the data is of dataType type. This will return an object that is
	 * converted to the appropriate type before returning it if required.
	 */
    public Object convertType(Object data, int dataType) {
        return typeManager.convert(data, dataType);
    }

    /**
	 * Binds the value to the statement according to the data type.
	 */
    private void bindSimpleData(int index, int dataType, Object data, PreparedStatement pstmt) throws SQLException {
        try {
            switch(dataType) {
                case java.sql.Types.BOOLEAN:
                    Boolean bo = (Boolean) data;
                    pstmt.setBoolean(index, bo.booleanValue());
                    break;
                case java.sql.Types.VARCHAR:
                    pstmt.setString(index, (String) data);
                    break;
                case java.sql.Types.CHAR:
                    pstmt.setString(index, data.toString());
                    break;
                case java.sql.Types.TINYINT:
                    pstmt.setByte(index, ((Byte) data).byteValue());
                    break;
                case java.sql.Types.SMALLINT:
                    pstmt.setShort(index, ((Short) data).shortValue());
                    break;
                case java.sql.Types.INTEGER:
                    pstmt.setInt(index, ((Integer) data).intValue());
                    break;
                case java.sql.Types.BIGINT:
                    pstmt.setLong(index, ((Long) data).longValue());
                    break;
                case java.sql.Types.REAL:
                    pstmt.setFloat(index, ((Float) data).floatValue());
                    break;
                case java.sql.Types.FLOAT:
                    pstmt.setDouble(index, ((Double) data).doubleValue());
                    break;
                case java.sql.Types.DOUBLE:
                    pstmt.setDouble(index, ((Double) data).doubleValue());
                    break;
                case java.sql.Types.NUMERIC:
                    pstmt.setBigDecimal(index, (BigDecimal) data);
                    break;
                case java.sql.Types.DECIMAL:
                    pstmt.setBigDecimal(index, (BigDecimal) data);
                    break;
                case java.sql.Types.TIME:
                    pstmt.setTime(index, (java.sql.Time) data);
                    break;
                case java.sql.Types.DATE:
                    pstmt.setDate(index, (java.sql.Date) data);
                    break;
                case java.sql.Types.TIMESTAMP:
                    pstmt.setTimestamp(index, (java.sql.Timestamp) data);
                    break;
                case java.sql.Types.BINARY:
                    pstmt.setBytes(index, (byte[]) data);
                    break;
                case java.sql.Types.VARBINARY:
                    pstmt.setBytes(index, (byte[]) data);
                    break;
                case java.sql.Types.OTHER:
                    pstmt.setObject(index, data);
                    break;
                case java.sql.Types.JAVA_OBJECT:
                    pstmt.setObject(index, data);
                    break;
                default:
                    String msg = Message.msg("persist.bind.datatype", "" + dataType, "" + index);
                    throw new SQLException(msg);
            }
        } catch (Exception e) {
            String dataClass = "Data is null?";
            if (data != null) {
                dataClass = data.getClass().getName();
            }
            String m = "Error with property[" + index + "] dt[" + dataType + "]";
            m += "data[" + data + "][" + dataClass + "]";
            throw new PersistenceException(m, e);
        }
    }

    /**
	 * Bind String data to a LONGVARCHAR column.
	 */
    private void bindLongVarChar(int index, Object data, PreparedStatement pstmt) throws SQLException {
        String sd = (String) data;
        Reader stringReader = new StringReader(sd);
        pstmt.setCharacterStream(index, stringReader, sd.length());
        return;
    }

    /**
	 * Bind byte[] data to a LONGVARBINARY column.
	 */
    private void bindLongVarBinary(int index, Object data, PreparedStatement pstmt) throws SQLException {
        byte[] bytes = (byte[]) data;
        pstmt.setBinaryStream(index, new ByteArrayInputStream(bytes), bytes.length);
        return;
    }

    /**
	 * Bind String data to a CLOB column.
	 */
    private void bindClob(int index, Object data, PreparedStatement pstmt) throws SQLException {
        String sd = (String) data;
        Reader reader = new StringReader(sd);
        pstmt.setCharacterStream(index, reader, sd.length());
    }

    /**
	 * Bind byte[] data to a BLOB column.
	 */
    private void bindBlob(int index, Object data, PreparedStatement pstmt) throws SQLException {
        byte[] bytes = (byte[]) data;
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        pstmt.setBinaryStream(index, is, bytes.length);
    }

    private boolean isLob(int dbType) {
        switch(dbType) {
            case Types.CLOB:
                return true;
            case Types.LONGVARCHAR:
                return true;
            case Types.BLOB:
                return true;
            case Types.LONGVARBINARY:
                return true;
            default:
                return false;
        }
    }
}
