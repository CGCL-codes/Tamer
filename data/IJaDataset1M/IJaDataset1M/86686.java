package org.extjsf.jdbc;

import java.io.PrintWriter;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractDataSource implements DataSource {

    private static final Log logger = LogFactory.getLog(AbstractDataSource.class);

    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    public void setLoginTimeout(int timeout) throws SQLException {
        throw new UnsupportedOperationException("setLoginTimeout");
    }

    public PrintWriter getLogWriter() {
        throw new UnsupportedOperationException("getLogWriter");
    }

    public void setLogWriter(PrintWriter pw) throws SQLException {
        throw new UnsupportedOperationException("setLogWriter");
    }

    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (!DataSource.class.equals(iface)) {
            throw new SQLException("DataSource of type [" + getClass().getName() + "] can only be unwrapped as [javax.sql.DataSource], not as [" + iface.getName());
        }
        return (T) this;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return DataSource.class.equals(iface);
    }
}
