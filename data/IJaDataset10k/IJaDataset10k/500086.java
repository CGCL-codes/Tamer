package com.daffodilwoods.daffodildb.server.datasystem.persistentsystem;

import com.daffodilwoods.database.resource.*;
import java.io.*;
import java.sql.*;
import com.daffodilwoods.daffodildb.server.sql99.utils._Reference;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces.Datatype;

/**
 * it is used to insert data in clob type column,data is set in DClobUpdatable which has to insert and
 * data of clob column can be retrieved through DClobUpdatable
 */
public class DClobUpdatable extends DBlobUpdatable implements Clob {

    DClobUpdatable(DBlob blob) {
        super(blob);
    }

    public DClobUpdatable(byte[] bytes) {
        super(bytes);
    }

    /**
    * Constructs DClobUpdatable when data has to insert through stream
    *
    * @param stream0 from which data has to insert
    */
    public DClobUpdatable(InputStream stream0) {
        super(stream0);
    }

    public DClobUpdatable(boolean isNull) {
        super(isNull);
    }

    /**
    * Constructs DClobUpdatable when data has to insert through stream
    *
    * @param inputString which has to insert
    */
    public DClobUpdatable(String inputString) {
        this(inputString.getBytes());
    }

    public DClobUpdatable() {
    }

    public long length() throws SQLException {
        return super.length();
    }

    /**
    * To retrieve substring of given length and from specified position
    *
    * @param pos position from where string is required
    * @param length length of string which is required
    *
    * @return substring of given length and from specified position
    */
    public String getSubString(long pos, int length) throws SQLException {
        return new String(getBytes(pos, length));
    }

    public Reader getCharacterStream() throws SQLException {
        return new java.io.InputStreamReader(getAsciiStream());
    }

    public InputStream getAsciiStream() throws SQLException {
        return getBinaryStream();
    }

    public long position(String searchstr, long start) throws SQLException {
        initialize();
        String targetString = new String(getBytes(start, lengthOfBlob - (int) start));
        return targetString.indexOf(searchstr);
    }

    public long position(Clob searchstr, long start) throws SQLException {
        try {
            return position(((DClob) searchstr).getSubString(start, (int) (searchstr.length() - start)), 0);
        } catch (DException ex) {
            throw ex.getSqlException(null);
        }
    }

    /**
    * To update string from given position
    *
    * @param pos position form where string has to update
    * @param str new string which has to write
    *
    * @return length of string which is updated
    */
    public int setString(long pos, String str) throws SQLException {
        return setBytes(pos, str.getBytes());
    }

    public int setString(long pos, String str, int offset, int len) throws SQLException {
        return setString(pos, str.substring(offset, len));
    }

    public OutputStream setAsciiStream(long pos) throws SQLException {
        return setBinaryStream(pos);
    }

    public Writer setCharacterStream(long pos) throws SQLException {
        return new java.io.OutputStreamWriter(setAsciiStream(pos));
    }

    public void setStream(InputStream stream) {
        super.setStream(stream);
    }

    public String toString() {
        return "java.sql.Clob";
    }

    public int getDatatype() throws DException {
        return Datatype.CLOB;
    }

    public int getLength() {
        try {
            return (int) length();
        } catch (SQLException ex) {
            return -1;
        }
    }
}
