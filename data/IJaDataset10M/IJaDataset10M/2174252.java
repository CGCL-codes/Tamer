package orkomet;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DerbyDAL {

    {
        String className = "org.apache.derby.jdbc.EmbeddedDriver";
        try {
            Class.forName(className).newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static IDBConnection conn = null;

    public static IDBConnection getConnection() {
        if (conn == null) {
            conn = new DBConnection("jdbc:derby:Orkomet;create=true", "ork", "ork", "Derby");
        }
        return conn;
    }

    public static void shutDownDerby() throws SQLException {
        DriverManager.getConnection("jdbc:derby:;shutdown=true");
    }
}
