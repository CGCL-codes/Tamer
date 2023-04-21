package org.vrspace.neurogrid;

import java.sql.*;

public class MySQLStore extends SQLStore {

    public MySQLStore(String url, String user, String password) throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection(url, user, password);
    }
}
