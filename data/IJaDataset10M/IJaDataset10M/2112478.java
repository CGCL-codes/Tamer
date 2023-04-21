package org.geogurus.tools.raster;

import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Test extends Component {

    public Test() {
    }

    /**
   *
   * Simulate the getConnection() from QUANTIX servlet
   *
   */
    public Connection getConnection() {
        java.sql.Connection con = null;
        try {
            String dbURL = "jdbc:postgresql://terence:5432/test_postgis";
            Class driver = Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(dbURL, "postgres", "postgres");
        } catch (ClassNotFoundException cnfe) {
            return null;
        } catch (SQLException sqle) {
            return null;
        }
        return con;
    }

    /**
   *
   * Return a result from a single query
   *
   */
    public String getSingleQuery(Connection con, String queryString) {
        String res = null;
        try {
            Statement stmt = con.createStatement();
            StringBuffer query = new StringBuffer(queryString);
            ResultSet rs = stmt.executeQuery(query.toString());
            while (rs.next()) {
                res = rs.getString(1);
            }
            System.out.println(query.toString());
            System.out.println("Resultat: " + res);
            stmt.close();
        } catch (SQLException sqle) {
            System.out.println("getGeometries: SQL State: " + sqle.getSQLState() + " : " + sqle.getMessage());
            return null;
        }
        return res;
    }

    public static void main(String[] args) {
        Test test = new Test();
        System.out.println("Fini");
        System.exit(0);
    }
}
