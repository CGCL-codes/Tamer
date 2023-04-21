package org.vrspace.neurogrid;

import java.sql.*;

public class OracleStore extends SQLStore {

    public OracleStore(String url, String user, String password) throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        conn = DriverManager.getConnection(url, user, password);
    }
}
