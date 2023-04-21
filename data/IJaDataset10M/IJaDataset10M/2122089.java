package persistence;

import java.sql.*;
import isworld.*;

class test {

    public static void main(String argv[]) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:postgresql:impspace", "impspace", "impspace");
            SQLPersistence xmp = new SQLPersistence("../data/database/ObjectMapping.xml", conn, new SQL92());
            Star as = (Star) xmp.loadObject("isworld.Star", "0");
            System.out.println("Star, Name : " + as.getName());
            xmp.saveObject(as);
            xmp.insertObject(as);
        } catch (ClassNotFoundException e) {
            System.err.println("Look like the jdbc driver is missing.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Look like jdbc driver refuse the connection.");
            e.printStackTrace();
        }
    }
}
