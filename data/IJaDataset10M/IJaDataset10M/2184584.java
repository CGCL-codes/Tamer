package org.openXpertya.util;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import javax.sql.RowSet;
import javax.swing.JOptionPane;
import org.openXpertya.db.BaseDatosOXP;
import org.openXpertya.db.CConnection;
import org.openXpertya.interfaces.Server;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class CPreparedStatement extends CStatement implements PreparedStatement {

    /**
	 * Switch general para la conversión de sentencias SQL.
	 * En caso de setearla a true, evita la invocación a conversiones SQL
	 */
    public static boolean noConvertSQL = false;

    public static boolean isNoConvertSQL() {
        return noConvertSQL;
    }

    public static void setNoConvertSQL(boolean noConvertSQL) {
        CPreparedStatement.noConvertSQL = noConvertSQL;
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param resultSetType
     * @param resultSetConcurrency
     * @param sql0
     * @param trxName
     */
    public CPreparedStatement(int resultSetType, int resultSetConcurrency, String sql0, String trxName) {
        this(resultSetType, resultSetConcurrency, sql0, trxName, false);
    }

    public CPreparedStatement(int resultSetType, int resultSetConcurrency, String sql0, String trxName, boolean noConvert) {
        if ((sql0 == null) || (sql0.length() == 0)) {
            throw new IllegalArgumentException("sql required");
        }
        if (noConvertSQL) noConvert = noConvertSQL;
        p_vo = new CStatementVO(resultSetType, resultSetConcurrency, (noConvert ? sql0 : DB.getDatabase().convertStatement(sql0)));
        if (!DB.isRemoteObjects()) {
            try {
                Connection conn = null;
                Trx trx = (trxName == null) ? null : Trx.get(trxName, true);
                if (trx != null) {
                    log.fine("estamos en trx!=null");
                    conn = trx.getConnection();
                } else {
                    if (resultSetConcurrency == ResultSet.CONCUR_UPDATABLE) {
                        conn = DB.getConnectionRW();
                    } else {
                        conn = DB.getConnectionRO();
                    }
                }
                if (conn == null) {
                    throw new DBException("No Connection");
                }
                p_stmt = conn.prepareStatement(p_vo.getSql(), resultSetType, resultSetConcurrency);
                return;
            } catch (Exception e) {
                log.log(Level.SEVERE, p_vo.getSql(), e);
            }
        }
    }

    /**
     * Constructor de la clase ...
     *
     *
     * @param vo
     */
    public CPreparedStatement(CStatementVO vo) {
        super(vo);
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     *
     * @throws SQLException
     */
    public ResultSet executeQuery() throws SQLException {
        log.fine("estamos p_stmt= " + p_stmt);
        if (p_stmt != null) {
            try {
                return ((PreparedStatement) p_stmt).executeQuery();
            } catch (Exception e) {
                log.fine("falla al ejecutar la consulta = " + e);
            }
        }
        log.finest("server => " + p_vo + ", Remote=" + DB.isRemoteObjects());
        try {
            if (DB.isRemoteObjects() && CConnection.get().isAppsServerOK(false)) {
                Server server = CConnection.get().getServer();
                if (server != null) {
                    ResultSet rs = server.pstmt_getRowSet(p_vo);
                    p_vo.clearParameters();
                    if (rs == null) {
                        log.warning("executeQuery - ResultSet is null - " + p_vo);
                    }
                    return rs;
                }
                log.log(Level.SEVERE, "executeQuery - AppsServer not found");
            }
        } catch (Exception ex) {
            log.log(Level.SEVERE, "executeQuery - AppsServer error", ex);
        }
        PreparedStatement pstmt = local_getPreparedStatement(false, null);
        p_vo.clearParameters();
        return pstmt.executeQuery();
    }

    /**
     * Descripción de Método
     *
     *
     * @param sql0
     *
     * @return
     *
     * @throws SQLException
     */
    public ResultSet executeQuery(String sql0) throws SQLException {
        p_vo.setSql(DB.getDatabase().convertStatement(sql0));
        if (p_stmt != null) {
            return p_stmt.executeQuery(p_vo.getSql());
        }
        return executeQuery();
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     *
     * @throws SQLException
     */
    public int executeUpdate() throws SQLException {
        if (p_stmt != null) {
            return ((PreparedStatement) p_stmt).executeUpdate();
        }
        log.finest("server => " + p_vo + ", Remote=" + DB.isRemoteObjects());
        try {
            if (DB.isRemoteObjects() && CConnection.get().isAppsServerOK(false)) {
                Server server = CConnection.get().getServer();
                if (server != null) {
                    int result = server.stmt_executeUpdate(p_vo);
                    p_vo.clearParameters();
                    return result;
                }
                log.log(Level.SEVERE, "AppsServer not found");
            }
        } catch (RemoteException ex) {
            log.log(Level.SEVERE, "AppsServer error", ex);
        }
        log.warning("execute locally");
        PreparedStatement pstmt = local_getPreparedStatement(false, null);
        p_vo.clearParameters();
        return pstmt.executeUpdate();
    }

    /**
     * Descripción de Método
     *
     *
     * @param sql0
     *
     * @return
     *
     * @throws SQLException
     */
    public int executeUpdate(String sql0) throws SQLException {
        p_vo.setSql(DB.getDatabase().convertStatement(sql0));
        if (p_stmt != null) {
            return p_stmt.executeUpdate(p_vo.getSql());
        }
        return executeUpdate();
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     *
     * @throws SQLException
     */
    public boolean execute() throws SQLException {
        if (p_stmt != null) {
            return ((PreparedStatement) p_stmt).execute();
        }
        throw new java.lang.UnsupportedOperationException("Method execute() not yet implemented.");
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     *
     * @throws SQLException
     */
    public ResultSetMetaData getMetaData() throws SQLException {
        if (p_stmt != null) {
            return ((PreparedStatement) p_stmt).getMetaData();
        } else {
            throw new java.lang.UnsupportedOperationException("Method getMetaData() not yet implemented.");
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     *
     * @throws SQLException
     */
    public ParameterMetaData getParameterMetaData() throws SQLException {
        if (p_stmt != null) {
            return ((PreparedStatement) p_stmt).getParameterMetaData();
        }
        throw new java.lang.UnsupportedOperationException("Method getParameterMetaData() not yet implemented.");
    }

    /**
     * Descripción de Método
     *
     *
     * @throws SQLException
     */
    public void addBatch() throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).addBatch();
        } else {
            throw new java.lang.UnsupportedOperationException("Method addBatch() not yet implemented.");
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param sqlType
     *
     * @throws SQLException
     */
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setNull(parameterIndex, sqlType);
        } else {
            p_vo.setParameter(parameterIndex, new NullParameter(sqlType));
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param sqlType
     * @param typeName
     *
     * @throws SQLException
     */
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setNull(parameterIndex, sqlType);
        } else {
            p_vo.setParameter(parameterIndex, new NullParameter(sqlType));
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setBoolean(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, new Boolean(x));
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setByte(int parameterIndex, byte x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setByte(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, new Byte(x));
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setShort(int parameterIndex, short x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setShort(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, new Short(x));
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setInt(int parameterIndex, int x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setInt(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, new Integer(x));
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setLong(int parameterIndex, long x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setLong(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, new Long(x));
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setFloat(int parameterIndex, float x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setFloat(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, new Float(x));
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setDouble(int parameterIndex, double x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setDouble(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, new Double(x));
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setBigDecimal(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, x);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setString(int parameterIndex, String x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setString(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, x);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setBytes(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, x);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setDate(int parameterIndex, java.sql.Date x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setDate(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, x);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setTime(int parameterIndex, Time x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setTime(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, x);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setTimestamp(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, x);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     * @param length
     *
     * @throws SQLException
     */
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setAsciiStream(parameterIndex, x, length);
        } else {
            throw new java.lang.UnsupportedOperationException("Method setAsciiStream() not yet implemented.");
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     * @param length
     *
     * @throws SQLException
     */
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        throw new UnsupportedOperationException("Method setUnicodeStream() not yet implemented.");
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     * @param length
     *
     * @throws SQLException
     */
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setBinaryStream(parameterIndex, x, length);
        } else {
            throw new java.lang.UnsupportedOperationException("Method setBinaryStream() not yet implemented.");
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @throws SQLException
     */
    public void clearParameters() throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).clearParameters();
        } else {
            p_vo.clearParameters();
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     * @param targetSqlType
     * @param scale
     *
     * @throws SQLException
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setObject(parameterIndex, x, targetSqlType, scale);
        } else {
            throw new java.lang.UnsupportedOperationException("Method setObject() not yet implemented.");
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     * @param targetSqlType
     *
     * @throws SQLException
     */
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setObject(parameterIndex, x);
        } else {
            throw new java.lang.UnsupportedOperationException("Method setObject() not yet implemented.");
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setObject(int parameterIndex, Object x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setObject(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, x);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param reader
     * @param length
     *
     * @throws SQLException
     */
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setCharacterStream(parameterIndex, reader, length);
        } else {
            throw new java.lang.UnsupportedOperationException("Method setCharacterStream() not yet implemented.");
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setRef(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, x);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setObject(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, x);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setObject(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, x);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setArray(int parameterIndex, Array x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setObject(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, x);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     * @param cal
     *
     * @throws SQLException
     */
    public void setDate(int parameterIndex, java.sql.Date x, Calendar cal) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setDate(parameterIndex, x, cal);
        } else {
            throw new java.lang.UnsupportedOperationException("Method setDate() not yet implemented.");
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     * @param cal
     *
     * @throws SQLException
     */
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setTime(parameterIndex, x, cal);
        } else {
            throw new java.lang.UnsupportedOperationException("Method setTime() not yet implemented.");
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     * @param cal
     *
     * @throws SQLException
     */
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setTimestamp(parameterIndex, x, cal);
        } else {
            throw new java.lang.UnsupportedOperationException("Method setTimestamp() not yet implemented.");
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @param parameterIndex
     * @param x
     *
     * @throws SQLException
     */
    public void setURL(int parameterIndex, URL x) throws SQLException {
        if (p_stmt != null) {
            ((PreparedStatement) p_stmt).setObject(parameterIndex, x);
        } else {
            p_vo.setParameter(parameterIndex, x);
        }
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public String toString() {
        if (p_stmt != null) {
            return "CPreparedStatement[Local=" + p_stmt + "]";
        }
        return "CPreparedStatement[" + p_vo + "]";
    }

    /**
     * Descripción de Método
     *
     *
     * @param dedicatedConnection
     * @param trxName
     *
     * @return
     */
    private PreparedStatement local_getPreparedStatement(boolean dedicatedConnection, String trxName) {
        log.finest(p_vo.getSql());
        Connection conn = null;
        Trx trx = (trxName == null) ? null : Trx.get(trxName, true);
        if (trx != null) {
            conn = trx.getConnection();
        } else {
            if (dedicatedConnection) {
                conn = DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED);
            } else {
                conn = local_getConnection(trxName);
            }
        }
        if (conn == null) {
            throw new IllegalStateException("Local - No Connection");
        }
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(p_vo.getSql(), p_vo.getResultSetType(), p_vo.getResultSetConcurrency());
            ArrayList parameters = p_vo.getParameters();
            for (int i = 0; i < parameters.size(); i++) {
                Object o = parameters.get(i);
                if (o == null) {
                    throw new IllegalArgumentException("Local - Null Parameter #" + i);
                } else if (o instanceof NullParameter) {
                    int type = ((NullParameter) o).getType();
                    pstmt.setNull(i + 1, type);
                    log.finest("#" + (i + 1) + " - Null");
                } else if (o instanceof Integer) {
                    pstmt.setInt(i + 1, ((Integer) o).intValue());
                    log.finest("#" + (i + 1) + " - int=" + o);
                } else if (o instanceof String) {
                    pstmt.setString(i + 1, (String) o);
                    log.finest("#" + (i + 1) + " - String=" + o);
                } else if (o instanceof Timestamp) {
                    pstmt.setTimestamp(i + 1, (Timestamp) o);
                    log.finest("#" + (i + 1) + " - Timestamp=" + o);
                } else if (o instanceof BigDecimal) {
                    pstmt.setBigDecimal(i + 1, (BigDecimal) o);
                    log.finest("#" + (i + 1) + " - BigDecimal=" + o);
                } else {
                    throw new java.lang.UnsupportedOperationException("Unknown Parameter Class=" + o.getClass());
                }
            }
        } catch (SQLException ex) {
            log.log(Level.SEVERE, "local", ex);
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                pstmt = null;
            } catch (SQLException ex1) {
            }
        }
        return pstmt;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public RowSet local_getRowSet() {
        log.finest("local");
        Connection conn = DB.createConnection(false, Connection.TRANSACTION_READ_COMMITTED);
        PreparedStatement pstmt = null;
        RowSet rowSet = null;
        try {
            pstmt = conn.prepareStatement(p_vo.getSql(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ArrayList parameters = p_vo.getParameters();
            for (int i = 0; i < parameters.size(); i++) {
                Object o = parameters.get(i);
                if (o == null) {
                    throw new IllegalArgumentException("Null Parameter #" + i);
                } else if (o instanceof NullParameter) {
                    int type = ((NullParameter) o).getType();
                    pstmt.setNull(i + 1, type);
                    log.finest("#" + (i + 1) + " - Null");
                } else if (o instanceof Integer) {
                    pstmt.setInt(i + 1, ((Integer) o).intValue());
                    log.finest("#" + (i + 1) + " - int=" + o);
                } else if (o instanceof String) {
                    pstmt.setString(i + 1, (String) o);
                    log.finest("#" + (i + 1) + " - String=" + o);
                } else if (o instanceof Timestamp) {
                    pstmt.setTimestamp(i + 1, (Timestamp) o);
                    log.finest("#" + (i + 1) + " - Timestamp=" + o);
                } else if (o instanceof BigDecimal) {
                    pstmt.setBigDecimal(i + 1, (BigDecimal) o);
                    log.finest("#" + (i + 1) + " - BigDecimal=" + o);
                } else {
                    throw new java.lang.UnsupportedOperationException("Unknown Parameter Class=" + o.getClass());
                }
            }
            ResultSet rs = pstmt.executeQuery();
            rowSet = CCachedRowSet.getRowSet(rs);
            pstmt.close();
            pstmt = null;
            conn.close();
            conn = null;
        } catch (Exception ex) {
            log.log(Level.SEVERE, "..>" + p_vo.toString(), ex);
            throw new RuntimeException(ex);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
            if (conn != null) {
                conn.close();
            }
            conn = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "close", e);
        }
        return rowSet;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public RowSet remote_getRowSet() {
        log.finest("remote");
        Connection conn = local_getConnection(null);
        PreparedStatement pstmt = null;
        RowSet rowSet = null;
        try {
            pstmt = conn.prepareStatement(p_vo.getSql(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ArrayList parameters = p_vo.getParameters();
            for (int i = 0; i < parameters.size(); i++) {
                Object o = parameters.get(i);
                if (o == null) {
                    throw new IllegalArgumentException("Null Parameter #" + i);
                } else if (o instanceof NullParameter) {
                    int type = ((NullParameter) o).getType();
                    pstmt.setNull(i + 1, type);
                    log.finest("#" + (i + 1) + " - Null");
                } else if (o instanceof Integer) {
                    pstmt.setInt(i + 1, ((Integer) o).intValue());
                    log.finest("#" + (i + 1) + " - int=" + o);
                } else if (o instanceof String) {
                    pstmt.setString(i + 1, (String) o);
                    log.finest("#" + (i + 1) + " - String=" + o);
                } else if (o instanceof Timestamp) {
                    pstmt.setTimestamp(i + 1, (Timestamp) o);
                    log.finest("#" + (i + 1) + " - Timestamp=" + o);
                } else if (o instanceof BigDecimal) {
                    pstmt.setBigDecimal(i + 1, (BigDecimal) o);
                    log.finest("#" + (i + 1) + " - BigDecimal=" + o);
                } else {
                    throw new java.lang.UnsupportedOperationException("Unknown Parameter Class=" + o.getClass());
                }
            }
            ResultSet rs = pstmt.executeQuery();
            rowSet = CCachedRowSet.getRowSet(rs);
            pstmt.close();
            pstmt = null;
        } catch (Exception ex) {
            log.log(Level.SEVERE, p_vo.toString(), ex);
            throw new RuntimeException(ex);
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
            pstmt = null;
        } catch (Exception e) {
            log.log(Level.SEVERE, "close pstmt", e);
        }
        return rowSet;
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     */
    public int remote_executeUpdate() {
        log.finest("Update");
        try {
            BaseDatosOXP db = CConnection.get().getDatabase();
            if (db == null) {
                throw new NullPointerException("Remote - No Database");
            }
            PreparedStatement pstmt = local_getPreparedStatement(false, null);
            int result = pstmt.executeUpdate();
            pstmt.close();
            return result;
        } catch (Exception ex) {
            log.log(Level.SEVERE, p_vo.toString(), ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void setAsciiStream(int arg0, InputStream arg1) throws SQLException {
    }

    @Override
    public void setAsciiStream(int arg0, InputStream arg1, long arg2) throws SQLException {
    }

    @Override
    public void setBinaryStream(int arg0, InputStream arg1) throws SQLException {
    }

    @Override
    public void setBinaryStream(int arg0, InputStream arg1, long arg2) throws SQLException {
    }

    @Override
    public void setBlob(int arg0, InputStream arg1) throws SQLException {
    }

    @Override
    public void setBlob(int arg0, InputStream arg1, long arg2) throws SQLException {
    }

    @Override
    public void setCharacterStream(int arg0, Reader arg1) throws SQLException {
    }

    @Override
    public void setCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
    }

    @Override
    public void setClob(int arg0, Reader arg1) throws SQLException {
    }

    @Override
    public void setClob(int arg0, Reader arg1, long arg2) throws SQLException {
    }

    @Override
    public void setNCharacterStream(int arg0, Reader arg1) throws SQLException {
    }

    @Override
    public void setNCharacterStream(int arg0, Reader arg1, long arg2) throws SQLException {
    }

    @Override
    public void setNClob(int arg0, NClob arg1) throws SQLException {
    }

    @Override
    public void setNClob(int arg0, Reader arg1) throws SQLException {
    }

    @Override
    public void setNClob(int arg0, Reader arg1, long arg2) throws SQLException {
    }

    @Override
    public void setNString(int arg0, String arg1) throws SQLException {
    }

    @Override
    public void setRowId(int arg0, RowId arg1) throws SQLException {
    }

    @Override
    public void setSQLXML(int arg0, SQLXML arg1) throws SQLException {
    }

    @Override
    public boolean isClosed() throws SQLException {
        return false;
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return false;
    }

    @Override
    public void setPoolable(boolean arg0) throws SQLException {
    }

    @Override
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        return null;
    }
}
