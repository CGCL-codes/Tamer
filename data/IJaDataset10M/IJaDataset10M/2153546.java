package com.googlecode.projects.tests;

import java.sql.*;

/**
 * The purpose of this class 'JDBCCreateTable.java' is to test that the
 * applications code can successfully create a new table in a database.
 * 
 * @author Sean
 * 
 */
public class JDBCDropTable {

    /**
	 * Create a main method to run the program.
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        Connection connection = null;
        String database = "jdbc:derby:/DerbyDB/AssetDB";
        String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";
        String dropTable = "DROP TABLE TEST";
        try {
            Class.forName(databaseDriver).newInstance();
            connection = DriverManager.getConnection(database);
            try {
                Statement statement = connection.createStatement();
                statement.executeUpdate(dropTable);
                System.out.println("Table successfully removed!!!");
            } catch (SQLException s) {
                System.out.println("No table TEST to be removed!!!");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
