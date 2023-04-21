package org.groovyflow.db;

import java.sql.Connection;

/**
 * @author Chuck Tassoni
 */
public class ConnectionThreadLocal {

    private static ThreadLocal<Connection> holder = new ThreadLocal<Connection>();

    public static Connection getConnection() {
        return holder.get();
    }

    public static void setConnection(Connection conn) {
        holder.set(conn);
    }
}
