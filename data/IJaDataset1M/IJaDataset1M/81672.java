package net.admin4j.jdbc.driver.sql;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import net.admin4j.util.annotate.PackageRestrictions;

/**
 * Wraps a JDBC V2.0 callable statement so specifics can be reported to administrators.
 * @author D. Ashmore
 * @since 1.0
 */
@PackageRestrictions({ "net.admin4j", "java", "javax" })
public abstract class CallableStatementWrapper30Base extends PreparedStatementWrapper30Base implements CallableStatement {

    public CallableStatementWrapper30Base(ConnectionWrapper30Base connection, PreparedStatement statement) {
        super(connection, statement);
    }

    protected CallableStatement getUnderlyingCallableStatement() {
        return (CallableStatement) this.getUnderlyingStatement();
    }

    public Array getArray(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getArray(parameterIndex);
    }

    public Array getArray(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getArray(parameterName);
    }

    public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getBigDecimal(parameterIndex);
    }

    public BigDecimal getBigDecimal(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getBigDecimal(parameterName);
    }

    @SuppressWarnings("deprecation")
    public BigDecimal getBigDecimal(int parameterIndex, int scale) throws SQLException {
        return this.getUnderlyingCallableStatement().getBigDecimal(parameterIndex, scale);
    }

    public Blob getBlob(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getBlob(parameterIndex);
    }

    public Blob getBlob(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getBlob(parameterName);
    }

    public boolean getBoolean(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getBoolean(parameterIndex);
    }

    public boolean getBoolean(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getBoolean(parameterName);
    }

    public byte getByte(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getByte(parameterIndex);
    }

    public byte getByte(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getByte(parameterName);
    }

    public byte[] getBytes(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getBytes(parameterIndex);
    }

    public byte[] getBytes(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getBytes(parameterName);
    }

    public Clob getClob(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getClob(parameterIndex);
    }

    public Clob getClob(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getClob(parameterName);
    }

    public Date getDate(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getDate(parameterIndex);
    }

    public Date getDate(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getDate(parameterName);
    }

    public Date getDate(int parameterIndex, Calendar cal) throws SQLException {
        return this.getUnderlyingCallableStatement().getDate(parameterIndex, cal);
    }

    public Date getDate(String parameterName, Calendar cal) throws SQLException {
        return this.getUnderlyingCallableStatement().getDate(parameterName, cal);
    }

    public double getDouble(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getDouble(parameterIndex);
    }

    public double getDouble(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getDouble(parameterName);
    }

    public float getFloat(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getFloat(parameterIndex);
    }

    public float getFloat(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getFloat(parameterName);
    }

    public int getInt(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getInt(parameterIndex);
    }

    public int getInt(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getInt(parameterName);
    }

    public long getLong(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getLong(parameterIndex);
    }

    public long getLong(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getLong(parameterName);
    }

    public Object getObject(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getObject(parameterIndex);
    }

    public Object getObject(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getObject(parameterName);
    }

    public Object getObject(int parameterIndex, Map<String, Class<?>> map) throws SQLException {
        return this.getUnderlyingCallableStatement().getObject(parameterIndex, map);
    }

    public Object getObject(String parameterName, Map<String, Class<?>> map) throws SQLException {
        return this.getUnderlyingCallableStatement().getObject(parameterName, map);
    }

    public Ref getRef(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getRef(parameterIndex);
    }

    public Ref getRef(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getRef(parameterName);
    }

    public short getShort(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getShort(parameterIndex);
    }

    public short getShort(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getShort(parameterName);
    }

    public String getString(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getString(parameterIndex);
    }

    public String getString(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getString(parameterName);
    }

    public Time getTime(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getTime(parameterIndex);
    }

    public Time getTime(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getTime(parameterName);
    }

    public Time getTime(int parameterIndex, Calendar cal) throws SQLException {
        return this.getUnderlyingCallableStatement().getTime(parameterIndex, cal);
    }

    public Time getTime(String parameterName, Calendar cal) throws SQLException {
        return this.getUnderlyingCallableStatement().getTime(parameterName, cal);
    }

    public Timestamp getTimestamp(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getTimestamp(parameterIndex);
    }

    public Timestamp getTimestamp(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getTimestamp(parameterName);
    }

    public Timestamp getTimestamp(int parameterIndex, Calendar cal) throws SQLException {
        return this.getUnderlyingCallableStatement().getTimestamp(parameterIndex, cal);
    }

    public Timestamp getTimestamp(String parameterName, Calendar cal) throws SQLException {
        return this.getUnderlyingCallableStatement().getTimestamp(parameterName, cal);
    }

    public URL getURL(int parameterIndex) throws SQLException {
        return this.getUnderlyingCallableStatement().getURL(parameterIndex);
    }

    public URL getURL(String parameterName) throws SQLException {
        return this.getUnderlyingCallableStatement().getURL(parameterName);
    }

    public void registerOutParameter(int parameterIndex, int sqlType) throws SQLException {
        this.getUnderlyingCallableStatement().registerOutParameter(parameterIndex, sqlType);
    }

    public void registerOutParameter(String parameterName, int sqlType) throws SQLException {
        this.getUnderlyingCallableStatement().registerOutParameter(parameterName, sqlType);
    }

    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws SQLException {
        this.getUnderlyingCallableStatement().registerOutParameter(parameterIndex, sqlType, scale);
    }

    public void registerOutParameter(int parameterIndex, int sqlType, String typeName) throws SQLException {
        this.getUnderlyingCallableStatement().registerOutParameter(parameterIndex, sqlType, typeName);
    }

    public void registerOutParameter(String parameterName, int sqlType, int scale) throws SQLException {
        this.getUnderlyingCallableStatement().registerOutParameter(parameterName, sqlType, scale);
    }

    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws SQLException {
        this.getUnderlyingCallableStatement().registerOutParameter(parameterName, sqlType, typeName);
    }

    public void setBigDecimal(String parameterName, BigDecimal x) throws SQLException {
        this.getUnderlyingCallableStatement().setBigDecimal(parameterName, x);
    }

    public void setBoolean(String parameterName, boolean x) throws SQLException {
        this.getUnderlyingCallableStatement().setBoolean(parameterName, x);
    }

    public void setByte(String parameterName, byte x) throws SQLException {
        this.getUnderlyingCallableStatement().setByte(parameterName, x);
    }

    public void setBytes(String parameterName, byte[] x) throws SQLException {
        this.getUnderlyingCallableStatement().setBytes(parameterName, x);
    }

    public void setDate(String parameterName, Date x) throws SQLException {
        this.getUnderlyingCallableStatement().setDate(parameterName, x);
    }

    public void setDate(String parameterName, Date x, Calendar cal) throws SQLException {
        this.getUnderlyingCallableStatement().setDate(parameterName, x, cal);
    }

    public void setDouble(String parameterName, double x) throws SQLException {
        this.getUnderlyingCallableStatement().setDouble(parameterName, x);
    }

    public void setFloat(String parameterName, float x) throws SQLException {
        this.getUnderlyingCallableStatement().setFloat(parameterName, x);
    }

    public void setInt(String parameterName, int x) throws SQLException {
        this.getUnderlyingCallableStatement().setInt(parameterName, x);
    }

    public void setLong(String parameterName, long x) throws SQLException {
        this.getUnderlyingCallableStatement().setLong(parameterName, x);
    }

    public void setNull(String parameterName, int sqlType) throws SQLException {
        this.getUnderlyingCallableStatement().setNull(parameterName, sqlType);
    }

    public void setNull(String parameterName, int sqlType, String typeName) throws SQLException {
        this.getUnderlyingCallableStatement().setNull(parameterName, sqlType, typeName);
    }

    public void setObject(String parameterName, Object x) throws SQLException {
        this.getUnderlyingCallableStatement().setObject(parameterName, x);
    }

    public void setObject(String parameterName, Object x, int targetSqlType) throws SQLException {
        this.getUnderlyingCallableStatement().setObject(parameterName, x, targetSqlType);
    }

    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws SQLException {
        this.getUnderlyingCallableStatement().setObject(parameterName, x, targetSqlType, scale);
    }

    public void setShort(String parameterName, short x) throws SQLException {
        this.getUnderlyingCallableStatement().setShort(parameterName, x);
    }

    public void setString(String parameterName, String x) throws SQLException {
        this.getUnderlyingCallableStatement().setString(parameterName, x);
    }

    public void setTime(String parameterName, Time x) throws SQLException {
        this.getUnderlyingCallableStatement().setTime(parameterName, x);
    }

    public void setTime(String parameterName, Time x, Calendar cal) throws SQLException {
        this.getUnderlyingCallableStatement().setTime(parameterName, x, cal);
    }

    public void setTimestamp(String parameterName, Timestamp x) throws SQLException {
        this.getUnderlyingCallableStatement().setTimestamp(parameterName, x);
    }

    public void setTimestamp(String parameterName, Timestamp x, Calendar cal) throws SQLException {
        this.getUnderlyingCallableStatement().setTimestamp(parameterName, x, cal);
    }

    public void setURL(String parameterName, URL val) throws SQLException {
        this.getUnderlyingCallableStatement().setURL(parameterName, val);
    }

    public boolean wasNull() throws SQLException {
        return this.getUnderlyingCallableStatement().wasNull();
    }

    public void setCharacterStream(String parameterName, Reader reader, int length) throws SQLException {
        this.getUnderlyingCallableStatement().setCharacterStream(parameterName, reader, length);
    }

    public void setBinaryStream(String parameterName, InputStream x, int length) throws SQLException {
        this.getUnderlyingCallableStatement().setBinaryStream(parameterName, x, length);
    }

    public void setAsciiStream(String parameterName, InputStream x, int length) throws SQLException {
        this.getUnderlyingCallableStatement().setAsciiStream(parameterName, x, length);
    }
}
