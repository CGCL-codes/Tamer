package com.apelon.apps.dts.usermanager.connect;

import com.apelon.beans.apelapp.ApelJDBCConnMgr;
import com.apelon.beans.apelconfig.ApelConfig;
import com.apelon.common.sql.*;
import java.sql.Connection;
import java.util.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Apelon, Inc.</p>
 * @author Apelon Inc.
 * @version DTS 3.2.0
 */
public class UsermgrJDBCConnMgr extends ApelJDBCConnMgr {

    private Connection fConnection;

    public UsermgrJDBCConnMgr(Class queryserverclass, ApelConfig ac, boolean fShowSupportedDBTypes) {
        super(queryserverclass, ac, fShowSupportedDBTypes);
    }

    public void performConnection(Map map, boolean autoConnect, boolean useAsDefaults) throws Exception {
        super.performConnection(map, autoConnect, useAsDefaults);
        Connection conn = SQL.getConnection(getConnectionParams());
        setConnection(conn);
    }

    public void setConnection(Connection conn) {
        fConnection = conn;
    }

    public Connection getConnection() {
        return fConnection;
    }
}
