package org.sulweb.infureports.event;

import java.sql.Connection;

/**
 *
 * @author lucio
 */
public final class DBConnectionEvent {

    private Connection conn;

    /** Creates a new instance of DBConnectionEvent */
    public DBConnectionEvent(Connection conn) {
        this.conn = conn;
    }

    public Connection getConnection() {
        return conn;
    }
}
