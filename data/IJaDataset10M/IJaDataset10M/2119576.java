package com.core.util;

import com.be.vo.ConnectionPropertiesVO;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {

    protected static String dbDriver = "org.postgresql.Driver";

    static {
        try {
            DriverManager.registerDriver((Driver) Class.forName(dbDriver).newInstance());
        } catch (Exception e) {
            System.err.println("com.core.util.ConnectionPool: Could not load Driver " + dbDriver + " for Database. " + e);
        }
    }

    private Connection[] connectionPool;

    private int connectionsused = 0;

    private boolean[] free;

    private int position = 0;

    private final int poolsize = 2;

    public int connectionCount = 0;

    ConnectionPropertiesVO cp;

    public ConnectionPool(ConnectionPropertiesVO cp) throws SQLException {
        this.cp = cp;
        if (connectionPool == null) {
            connectionPool = new Connection[poolsize];
            free = new boolean[poolsize];
            for (int i = 0; i < poolsize; i++) {
                connectionPool[i] = DriverManager.getConnection(cp.getConnectionString(), cp.getProperties());
                free[i] = true;
                connectionCount++;
            }
            System.out.println("com.core.util.ConnectionPool.Constructor(ConnectionPropertiesVO): " + connectionPool.length + " connections created for " + cp.getConnectionString());
        }
    }

    public synchronized Connection acquire() {
        if (connectionsused == poolsize) {
            log("com.core.util.ConnectionPool.acquire(): No more free connections in pool.");
            return null;
        }
        position++;
        position %= poolsize;
        for (int i = 0; i < poolsize; i++) {
            int pos = (i + position) % poolsize;
            if (free[pos]) {
                free[pos] = false;
                try {
                    connectionPool[pos].setAutoCommit(true);
                } catch (SQLException se) {
                    try {
                        connectionPool[pos] = DriverManager.getConnection(cp.getConnectionString(), cp.getProperties());
                    } catch (SQLException se2) {
                        log("com.core.util.ConnectionPool.acquire(): Could not replace connection in pool. " + se2);
                    }
                    log("com.core.util.ConnectionPool.acquire(): Connection in pool replaced.");
                }
                return connectionPool[pos];
            }
        }
        return null;
    }

    public void release(Connection conn) {
        for (int i = 0; i < poolsize; i++) {
            if (connectionPool[i] == conn) {
                free[i] = true;
                break;
            }
        }
    }

    private void log(String msg) {
        System.out.println(msg);
    }

    public Connection[] getConnectionPool() {
        return connectionPool;
    }

    public int getConnectionsused() {
        return connectionsused;
    }

    public boolean[] getFree() {
        return free;
    }

    public int getPoolsize() {
        return poolsize;
    }

    public int getPosition() {
        return position;
    }

    public String toString() {
        return cp.toString() + "[" + connectionPool.length + "]";
    }

    public void closePool() {
        for (int i = 0; i < poolsize; i++) {
            try {
                if (!connectionPool[i].isClosed()) {
                    connectionPool[i].close();
                }
            } catch (SQLException se) {
                System.out.println("com.core.util.ConnectionPool.closePool(): " + se);
            }
        }
    }
}
