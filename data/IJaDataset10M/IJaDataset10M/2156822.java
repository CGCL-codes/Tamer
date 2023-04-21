package net.sourceforge.ws_jdbc.jdbc;

public class CallableStatement extends PreparedStatement implements java.sql.CallableStatement {

    java.util.HashMap parameters;

    public CallableStatement(long anID, String sql) {
        super(anID, sql);
    }

    /*****************************************************
 **********                                 **********
 ********** Beginning of API implementation **********
 **********                                 **********
 *****************************************************/
    public void registerOutParameter(int parameterIndex, int sqlType) throws java.sql.SQLException {
        try {
            theStub.c_registerOutParameter(id, parameterIndex, sqlType);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void registerOutParameter(int parameterIndex, int sqlType, int scale) throws java.sql.SQLException {
        try {
            theStub.c_registerOutParameter(id, parameterIndex, sqlType, scale);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public boolean wasNull() throws java.sql.SQLException {
        try {
            return theStub.c_wasNull(id);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public String getString(int parameterIndex) throws java.sql.SQLException {
        try {
            return theStub.c_getString(id, parameterIndex);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public boolean getBoolean(int parameterIndex) throws java.sql.SQLException {
        try {
            return theStub.c_getBoolean(id, parameterIndex);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public byte getByte(int parameterIndex) throws java.sql.SQLException {
        try {
            return theStub.c_getByte(id, parameterIndex);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public short getShort(int parameterIndex) throws java.sql.SQLException {
        try {
            return theStub.c_getShort(id, parameterIndex);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public int getInt(int parameterIndex) throws java.sql.SQLException {
        try {
            return theStub.c_getInt(id, parameterIndex);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public long getLong(int parameterIndex) throws java.sql.SQLException {
        try {
            return theStub.c_getLong(id, parameterIndex);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public float getFloat(int parameterIndex) throws java.sql.SQLException {
        try {
            return theStub.c_getFloat(id, parameterIndex);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public double getDouble(int parameterIndex) throws java.sql.SQLException {
        try {
            return theStub.c_getDouble(id, parameterIndex);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public java.math.BigDecimal getBigDecimal(int parameterIndex, int scale) throws java.sql.SQLException {
        try {
            return theStub.c_getBigDecimal(id, parameterIndex, scale);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public byte[] getBytes(int parameterIndex) throws java.sql.SQLException {
        try {
            return theStub.c_getBytes(id, parameterIndex);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public java.sql.Date getDate(int parameterIndex) throws java.sql.SQLException {
        java.sql.Date res = null;
        net.sourceforge.ws_jdbc.client_stubs.DateTime_T tempD;
        try {
            tempD = theStub.c_getTime(id, parameterIndex);
            res = TypeConverter.convertTypeD(tempD);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
        return res;
    }

    public java.sql.Time getTime(int parameterIndex) throws java.sql.SQLException {
        java.sql.Time res = null;
        net.sourceforge.ws_jdbc.client_stubs.DateTime_T tempT;
        try {
            tempT = theStub.c_getTime(id, parameterIndex);
            res = TypeConverter.convertTypeT(tempT);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
        return res;
    }

    public java.sql.Timestamp getTimestamp(int parameterIndex) throws java.sql.SQLException {
        java.sql.Timestamp res = null;
        net.sourceforge.ws_jdbc.client_stubs.Timestamp_T tempTS;
        try {
            tempTS = theStub.c_getTimestamp(id, parameterIndex);
            res = TypeConverter.convertType(tempTS);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
        return res;
    }

    public Object getObject(int parameterIndex) throws java.sql.SQLException {
        try {
            return theStub.c_getObject(id, parameterIndex);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public java.math.BigDecimal getBigDecimal(int parameterIndex) throws java.sql.SQLException {
        try {
            return theStub.c_getBigDecimal(id, parameterIndex);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public Object getObject(int i, java.util.Map map) throws java.sql.SQLException {
        try {
            return theStub.c_getObject(id, i, (java.util.HashMap) map);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public java.sql.Ref getRef(int i) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public java.sql.Blob getBlob(int i) throws java.sql.SQLException {
        try {
            return new net.sourceforge.ws_jdbc.jdbc.Blob(theStub.c_getBlob(id, i));
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public java.sql.Clob getClob(int i) throws java.sql.SQLException {
        try {
            return new net.sourceforge.ws_jdbc.jdbc.Clob(theStub.c_getClob(id, i));
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public java.sql.Array getArray(int i) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public java.sql.Date getDate(int parameterIndex, java.util.Calendar cal) throws java.sql.SQLException {
        java.sql.Date res = null;
        net.sourceforge.ws_jdbc.client_stubs.DateTime_T tempD;
        try {
            tempD = theStub.c_getTime(id, parameterIndex, TypeConverter.convertType(cal));
            res = TypeConverter.convertTypeD(tempD);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
        return res;
    }

    public java.sql.Time getTime(int parameterIndex, java.util.Calendar cal) throws java.sql.SQLException {
        java.sql.Time res = null;
        net.sourceforge.ws_jdbc.client_stubs.DateTime_T tempT;
        try {
            tempT = theStub.c_getTime(id, parameterIndex, TypeConverter.convertType(cal));
            res = TypeConverter.convertTypeT(tempT);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
        return res;
    }

    public java.sql.Timestamp getTimestamp(int parameterIndex, java.util.Calendar cal) throws java.sql.SQLException {
        java.sql.Timestamp res = null;
        net.sourceforge.ws_jdbc.client_stubs.Timestamp_T tempTS;
        try {
            tempTS = theStub.c_getTimestamp(id, parameterIndex, TypeConverter.convertType(cal));
            res = TypeConverter.convertType(tempTS);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
        return res;
    }

    public void registerOutParameter(int paramIndex, int sqlType, String typeName) throws java.sql.SQLException {
        try {
            theStub.c_registerOutParameter(id, paramIndex, sqlType, typeName);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void registerOutParameter(String parameterName, int sqlType) throws java.sql.SQLException {
        try {
            theStub.c_registerOutParameter(id, parameterName, sqlType);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void registerOutParameter(String parameterName, int sqlType, int scale) throws java.sql.SQLException {
        try {
            theStub.c_registerOutParameter(id, parameterName, sqlType, scale);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void registerOutParameter(String parameterName, int sqlType, String typeName) throws java.sql.SQLException {
        try {
            theStub.c_registerOutParameter(id, parameterName, sqlType, typeName);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public java.net.URL getURL(int parameterIndex) throws java.sql.SQLException {
        java.net.URL res = null;
        net.sourceforge.ws_jdbc.client_stubs.URL_T tempURL;
        try {
            tempURL = theStub.c_getURL(id, parameterIndex);
            res = TypeConverter.convertType(tempURL);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
        return res;
    }

    public void setURL(String parameterName, java.net.URL val) throws java.sql.SQLException {
        try {
            theStub.c_setURL(id, parameterName, TypeConverter.convertType(val));
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setNull(String parameterName, int sqlType) throws java.sql.SQLException {
        try {
            theStub.c_setNull(id, parameterName, sqlType);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setBoolean(String parameterName, boolean x) throws java.sql.SQLException {
        try {
            theStub.c_setBoolean(id, parameterName, x);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setByte(String parameterName, byte x) throws java.sql.SQLException {
        try {
            theStub.c_setByte(id, parameterName, x);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setShort(String parameterName, short x) throws java.sql.SQLException {
        try {
            theStub.c_setShort(id, parameterName, x);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setInt(String parameterName, int x) throws java.sql.SQLException {
        try {
            theStub.c_setInt(id, parameterName, x);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setLong(String parameterName, long x) throws java.sql.SQLException {
        try {
            theStub.c_setLong(id, parameterName, x);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setFloat(String parameterName, float x) throws java.sql.SQLException {
        try {
            theStub.c_setFloat(id, parameterName, x);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setDouble(String parameterName, double x) throws java.sql.SQLException {
        try {
            theStub.c_setDouble(id, parameterName, x);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setBigDecimal(String parameterName, java.math.BigDecimal x) throws java.sql.SQLException {
        try {
            theStub.c_setBigDecimal(id, parameterName, x);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setString(String parameterName, String x) throws java.sql.SQLException {
        try {
            theStub.c_setString(id, parameterName, x);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setBytes(String parameterName, byte x[]) throws java.sql.SQLException {
        try {
            theStub.c_setBytes(id, parameterName, x);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setDate(String parameterName, java.sql.Date x) throws java.sql.SQLException {
        try {
            theStub.c_setDate(id, parameterName, TypeConverter.convertType(x));
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setTime(String parameterName, java.sql.Time x) throws java.sql.SQLException {
        try {
            theStub.c_setTime(id, parameterName, TypeConverter.convertType(x));
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setTimestamp(String parameterName, java.sql.Timestamp x) throws java.sql.SQLException {
        try {
            theStub.c_setTimestamp(id, parameterName, TypeConverter.convertType(x));
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setAsciiStream(String parameterName, java.io.InputStream x, int length) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void setBinaryStream(String parameterName, java.io.InputStream x, int length) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void setObject(String parameterName, Object x, int targetSqlType, int scale) throws java.sql.SQLException {
        try {
            theStub.c_setObject(id, parameterName, x, targetSqlType, scale);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setObject(String parameterName, Object x, int targetSqlType) throws java.sql.SQLException {
        try {
            theStub.c_setObject(id, parameterName, x, targetSqlType);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setObject(String parameterName, Object x) throws java.sql.SQLException {
        try {
            theStub.c_setObject(id, parameterName, x);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setCharacterStream(String parameterName, java.io.Reader reader, int length) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public void setDate(String parameterName, java.sql.Date x, java.util.Calendar cal) throws java.sql.SQLException {
        try {
            theStub.c_setDate(id, parameterName, TypeConverter.convertType(x), TypeConverter.convertType(cal));
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setTime(String parameterName, java.sql.Time x, java.util.Calendar cal) throws java.sql.SQLException {
        try {
            theStub.c_setTime(id, parameterName, TypeConverter.convertType(x), TypeConverter.convertType(cal));
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setTimestamp(String parameterName, java.sql.Timestamp x, java.util.Calendar cal) throws java.sql.SQLException {
        try {
            theStub.c_setTimestamp(id, parameterName, TypeConverter.convertType(x), TypeConverter.convertType(cal));
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public void setNull(String parameterName, int sqlType, String typeName) throws java.sql.SQLException {
        try {
            theStub.c_setNull(id, parameterName, sqlType, typeName);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public String getString(String parameterName) throws java.sql.SQLException {
        try {
            return theStub.c_getString(id, parameterName);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public boolean getBoolean(String parameterName) throws java.sql.SQLException {
        try {
            return theStub.c_getBoolean(id, parameterName);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public byte getByte(String parameterName) throws java.sql.SQLException {
        try {
            return theStub.c_getByte(id, parameterName);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public short getShort(String parameterName) throws java.sql.SQLException {
        try {
            return theStub.c_getShort(id, parameterName);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public int getInt(String parameterName) throws java.sql.SQLException {
        try {
            return theStub.c_getInt(id, parameterName);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public long getLong(String parameterName) throws java.sql.SQLException {
        try {
            return theStub.c_getLong(id, parameterName);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public float getFloat(String parameterName) throws java.sql.SQLException {
        try {
            return theStub.c_getFloat(id, parameterName);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public double getDouble(String parameterName) throws java.sql.SQLException {
        try {
            return theStub.c_getDouble(id, parameterName);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public byte[] getBytes(String parameterName) throws java.sql.SQLException {
        try {
            return theStub.c_getBytes(id, parameterName);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public java.sql.Date getDate(String parameterName) throws java.sql.SQLException {
        java.sql.Date res = null;
        net.sourceforge.ws_jdbc.client_stubs.DateTime_T tempD;
        try {
            tempD = theStub.c_getTime(id, parameterName);
            res = TypeConverter.convertTypeD(tempD);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
        return res;
    }

    public java.sql.Time getTime(String parameterName) throws java.sql.SQLException {
        java.sql.Time res = null;
        net.sourceforge.ws_jdbc.client_stubs.DateTime_T tempT;
        try {
            tempT = theStub.c_getTime(id, parameterName);
            res = TypeConverter.convertTypeT(tempT);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
        return res;
    }

    public java.sql.Timestamp getTimestamp(String parameterName) throws java.sql.SQLException {
        java.sql.Timestamp res = null;
        net.sourceforge.ws_jdbc.client_stubs.Timestamp_T tempTS;
        try {
            tempTS = theStub.c_getTimestamp(id, parameterName);
            res = TypeConverter.convertType(tempTS);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
        return res;
    }

    public Object getObject(String parameterName) throws java.sql.SQLException {
        try {
            return theStub.c_getObject(id, parameterName);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public java.math.BigDecimal getBigDecimal(String parameterName) throws java.sql.SQLException {
        try {
            return theStub.c_getBigDecimal(id, parameterName);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public Object getObject(String parameterName, java.util.Map map) throws java.sql.SQLException {
        try {
            return theStub.c_getObject(id, parameterName, (java.util.HashMap) map);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public java.sql.Ref getRef(String parameterName) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public java.sql.Blob getBlob(String parameterName) throws java.sql.SQLException {
        try {
            return new net.sourceforge.ws_jdbc.jdbc.Blob(theStub.c_getBlob(id, parameterName));
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public java.sql.Clob getClob(String parameterName) throws java.sql.SQLException {
        try {
            return new net.sourceforge.ws_jdbc.jdbc.Clob(theStub.c_getClob(id, parameterName));
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
    }

    public java.sql.Array getArray(String parameterName) throws java.sql.SQLException {
        throw new java.sql.SQLException("Not Implemented");
    }

    public java.sql.Date getDate(String parameterName, java.util.Calendar cal) throws java.sql.SQLException {
        java.sql.Date res = null;
        net.sourceforge.ws_jdbc.client_stubs.DateTime_T tempD;
        try {
            tempD = theStub.c_getTime(id, parameterName, TypeConverter.convertType(cal));
            res = TypeConverter.convertTypeD(tempD);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
        return res;
    }

    public java.sql.Time getTime(String parameterName, java.util.Calendar cal) throws java.sql.SQLException {
        java.sql.Time res = null;
        net.sourceforge.ws_jdbc.client_stubs.DateTime_T tempT;
        try {
            tempT = theStub.c_getTime(id, parameterName, TypeConverter.convertType(cal));
            res = TypeConverter.convertTypeT(tempT);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
        return res;
    }

    public java.sql.Timestamp getTimestamp(String parameterName, java.util.Calendar cal) throws java.sql.SQLException {
        java.sql.Timestamp res = null;
        net.sourceforge.ws_jdbc.client_stubs.Timestamp_T tempTS;
        try {
            tempTS = theStub.c_getTimestamp(id, parameterName, TypeConverter.convertType(cal));
            res = TypeConverter.convertType(tempTS);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
        return res;
    }

    public java.net.URL getURL(String parameterName) throws java.sql.SQLException {
        java.net.URL res = null;
        net.sourceforge.ws_jdbc.client_stubs.URL_T tempURL;
        try {
            tempURL = theStub.c_getURL(id, parameterName);
            res = TypeConverter.convertType(tempURL);
        } catch (Exception e) {
            throw new java.sql.SQLException(getExceptionStr(e));
        }
        return res;
    }

    /*****************************************************
	 **********                                 **********
	 **********    End of API implementation    **********
	 **********                                 **********
	 *****************************************************/
    private String getExceptionStr(Exception e) {
        String res = e.toString(), findStr = "java.sql.", findStr2 = "SQLException: ";
        if (res.startsWith(findStr + findStr2)) res = res.substring(findStr.length() + findStr2.length());
        if (res.startsWith(findStr2)) res = res.substring(findStr2.length());
        return res;
    }
}
