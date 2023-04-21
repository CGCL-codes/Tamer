package f8ql.jdbc;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.sql.SQLException;

class Clob implements java.sql.Clob {

    public InputStream getAsciiStream() throws SQLException {
        return null;
    }

    public Reader getCharacterStream() throws SQLException {
        return null;
    }

    public String getSubString(long l, int i) throws SQLException {
        return null;
    }

    public long length() throws SQLException {
        return 0;
    }

    public long position(String s, long l) throws SQLException {
        return 0;
    }

    public long position(java.sql.Clob clob, long l) throws SQLException {
        return 0;
    }

    public OutputStream setAsciiStream(long l) throws SQLException {
        return null;
    }

    public Writer setCharacterStream(long l) throws SQLException {
        return null;
    }

    public int setString(long l, String s) throws SQLException {
        return 0;
    }

    public int setString(long l, String s, int i, int j) throws SQLException {
        return 0;
    }

    public void truncate(long l) throws SQLException {
    }
}
