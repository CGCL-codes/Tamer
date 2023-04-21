package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author steinbel
 * Hack to remove IOA.OrderNumber from the existing St.Louis db in order to avoid
 * reimporting the whole damn thing.  This class could potentially be used in
 * similiar future database-change situations, but if we think that's unlikely
 * we can just drop it.  (I was having permissions trouble with SQLServerManager,
 * which is why I did it this way.  It was faster and easier for me.)
 */
public class StLouishack {

    public StLouishack() {
        super();
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Database db = (Database) Database.getDatabase("SpASMSDB");
        db.openConnection();
        Connection con = db.getCon();
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate("DROP INDEX InternalAtomOrder.Order_Index");
            stmt.executeUpdate("ALTER TABLE InternalAtomOrder DROP COLUMN OrderNumber");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        db.closeConnection();
    }
}
