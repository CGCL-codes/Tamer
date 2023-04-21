package org.mili.test;

import java.sql.*;

/**
 * This test factory provides methods to instantiate object for test only.
 * 
 * @author Michael Lieshoff
 */
public class TableFactory {

    /**
     * Creates a new test table.
     *
     * @param connection the connection
     */
    static void create(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("drop table if exists A;");
        statement.executeUpdate("create table A(i integer primary key, s varchar(255));");
        statement.executeUpdate("insert into A(i, s) values(1, 's1');");
        statement.executeUpdate("insert into A(i, s) values(2, 's2');");
        statement.executeUpdate("insert into A(i, s) values(3, 's3');");
        statement.close();
    }
}
