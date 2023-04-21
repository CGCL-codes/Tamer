package org.objectstyle.cayenne.conf;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.objectstyle.cayenne.access.DataNode;

/**
 * A DataSource wrapper around Cayenne DataNode. Helpful for lazy initialization, i.e.
 * when node's DataSource is needed to initialize some other object BEFORE it is
 * initialized itself.
 * 
 * @since 1.2
 * @author Andrus Adamchik
 */
class NodeDataSource implements DataSource {

    DataNode node;

    NodeDataSource(DataNode node) {
        this.node = node;
    }

    public Connection getConnection() throws SQLException {
        return node.getDataSource().getConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException {
        return node.getDataSource().getConnection(username, password);
    }

    public PrintWriter getLogWriter() throws SQLException {
        return node.getDataSource().getLogWriter();
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        node.getDataSource().setLogWriter(out);
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        node.getDataSource().setLoginTimeout(seconds);
    }

    public int getLoginTimeout() throws SQLException {
        return node.getDataSource().getLoginTimeout();
    }
}
