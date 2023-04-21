package pos.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import pos.domain.Product;

public class JDBCProductDAO implements IProductDAO {

    public List selectAllProducts() {
        Connection conn = ConnectionManager.getInstance().checkOut();
        PreparedStatement stmt = null;
        List searchResults = new LinkedList();
        ResultSet result = null;
        try {
            String sql = "SELECT * FROM Product";
            stmt = conn.prepareStatement(sql);
            stmt.executeQuery();
            result = stmt.executeQuery();
            while (result.next()) {
                Product temp = new Product();
                temp.setProductID(result.getString("productID"));
                temp.setDescription(result.getString("description"));
                temp.setPrice(result.getInt("price"));
                searchResults.add(temp);
            }
        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("ErrorCode: " + e.getErrorCode());
        } finally {
            ConnectionManager.getInstance().checkIn(conn);
            try {
                if (result != null) result.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
            }
        }
        return searchResults;
    }

    public Product select(Connection conn, String productOID) {
        PreparedStatement stmt = null;
        ResultSet result = null;
        Product p = null;
        String sql = "SELECT * FROM Product WHERE (OID = ?) ";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, productOID);
            result = stmt.executeQuery();
            result.next();
            p = new Product();
            p.setDescription(result.getString("description"));
            p.setPrice(result.getInt("price"));
            p.setProductID(result.getString("productid"));
        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("ErrorCode: " + e.getErrorCode());
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
            }
        }
        return p;
    }

    public String selectProductOID(Connection conn, String productid) {
        PreparedStatement stmt = null;
        ResultSet result = null;
        String oidp = null;
        String sql = "SELECT * FROM Product WHERE (productid = ?) ";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, productid);
            result = stmt.executeQuery();
            result.next();
            oidp = result.getString("OID");
        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("ErrorCode: " + e.getErrorCode());
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
            }
        }
        return oidp;
    }
}
