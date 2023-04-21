package org.jinion.database.connectionPool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zhlmmc
 *
 */
public class MyConnection {

    private static int connectionNumber = 0;

    private int id;

    private Connection conn;

    private boolean inUse;

    private long lastAccess;

    private int useCount;

    public MyConnection(Connection conn) {
        this.conn = conn;
        this.inUse = false;
        this.lastAccess = 0;
        this.useCount = 0;
        this.id = this.makeConnectionId();
    }

    private synchronized int makeConnectionId() {
        return connectionNumber++;
    }

    protected void finalize() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public int getId() {
        return this.id;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    public long getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(long lastAccess) {
        this.lastAccess = lastAccess;
    }

    public int getUseCount() {
        return useCount;
    }

    public void increaseUseCount() {
        this.useCount++;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public Connection getConn() {
        return conn;
    }
}
