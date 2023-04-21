package com.ibm.csdl.ecm.ta.critsitEX.db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author Liven
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DatabaseConn {

    public synchronized Connection getConnection() throws Exception {
        Class.forName("com.ibm.db2.jcc.DB2Driver");
        Connection conn = DriverManager.getConnection("jdbc:db2://9.181.141.124:50000/TADB", "csdluser", "passw0rd");
        return conn;
    }
}
