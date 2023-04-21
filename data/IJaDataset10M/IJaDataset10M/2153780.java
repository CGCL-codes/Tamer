package org.linkset;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/*******************************************************************************
 * A dummy datasource returning the same connection every time.
 * 
 * @author Łukasz Bownik
 ******************************************************************************/
final class DummyDataSource implements DataSource {

    /***************************************************************************
	 * A constructor
	 * 
	 * @param target
	 *            an objects that defines command execution methods.
	 **************************************************************************/
    public DummyDataSource(final Connection connection) {
        if (connection == null) {
            throw new NullPointerException("Null connection!");
        }
        this.connection = connection;
    }

    /***************************************************************************
	 * Not implemented
	 **************************************************************************/
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    /***************************************************************************
	 * Not implemented
	 **************************************************************************/
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    /***************************************************************************
	 * Not implemented
	 **************************************************************************/
    public void setLogWriter(PrintWriter arg0) throws SQLException {
    }

    /***************************************************************************
	 * Not implemented
	 **************************************************************************/
    public void setLoginTimeout(int arg0) throws SQLException {
    }

    /***************************************************************************
	 * Not implemented
	 **************************************************************************/
    public boolean isWrapperFor(Class<?> arg0) throws SQLException {
        return false;
    }

    /***************************************************************************
	 * Not implemented
	 **************************************************************************/
    public <T> T unwrap(Class<T> arg0) throws SQLException {
        return null;
    }

    /***************************************************************************
	 * @see DataSource#getConnection()
	 **************************************************************************/
    public Connection getConnection() throws SQLException {
        return this.connection;
    }

    /***************************************************************************
	 * Not implemented
	 **************************************************************************/
    public Connection getConnection(String arg0, String arg1) throws SQLException {
        return null;
    }

    /***************************************************************************
	 * 
	 **************************************************************************/
    private Connection connection;
}
