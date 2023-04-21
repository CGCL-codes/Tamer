package com.zzsoft.framework.e2p.frame.app.db.pool;

import java.sql.Connection;
import framework.zze2p.db.dbconnect.DBConnect;

public class OpConnection implements OpConnectionI {

    public Connection getConnection(String fullDBName) {
        return DBConnect.getConnection_ByFullDBName(fullDBName);
    }

    public boolean close(Connection con) {
        try {
            if (con != null || !con.isClosed()) con.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
