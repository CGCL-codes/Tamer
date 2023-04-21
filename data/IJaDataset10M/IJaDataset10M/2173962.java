package isg3.pruebasDAO;

import isg3.data.ConnectionManager;
import java.sql.*;

public class PruebaConnection {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Connection c = ConnectionManager.getInstance().checkOut();
        try {
            PreparedStatement stmt = c.prepareStatement("SELECT * FROM Article");
            ResultSet s1 = stmt.executeQuery();
            while (s1.next()) {
                System.out.println("OID del elemento : " + s1.getString("oid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
