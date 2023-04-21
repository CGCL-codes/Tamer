package nz.ac.waikato.mcennis.rat.graph.property.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel McEnnis
 */
public class StringDB extends AbstractPropertyDB<String> {

    PreparedStatement get = null;

    PreparedStatement put = null;

    PreparedStatement putGet = null;

    PreparedStatement delete = null;

    public StringDB newCopy() {
        return new StringDB();
    }

    public void initializeDatabase(Connection conn) {
        if (conn != null) {
            try {
                Statement stat = conn.createStatement();
                stat.executeUpdate("CREATE TABLE String (" + "id integer not null generated ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," + "string varchar(1024) not null, " + "primary key(id))");
            } catch (SQLException ex) {
                Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setConnection(Connection con) {
        try {
            conn = con;
            get = conn.prepareStatement("SELECT string FROM String WHERE id=?");
            put = conn.prepareStatement("INSERT INTO String (string) VALUES (?)");
            putGet = conn.prepareStatement("SELECT MAX(id) FROM String WHERE id=?");
            delete = conn.prepareStatement("DELETE FROM String WHERE id=?");
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearDatabase(Connection conn) {
        try {
            conn.createStatement().executeUpdate("DELETE FROM String");
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String get(int i) {
        String ret = "";
        ResultSet rs = null;
        try {
            get.clearParameters();
            get.setInt(1, i);
            rs = get.executeQuery();
            if (rs.next()) {
                ret = rs.getString("string");
            }
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
                rs = null;
            }
        }
        return ret;
    }

    public int put(String object) {
        int ret = -1;
        ResultSet rs = null;
        try {
            put.clearParameters();
            put.setString(1, object);
            putGet.clearParameters();
            put.executeUpdate();
            rs = putGet.executeQuery();
            if (rs.next()) {
                ret = rs.getInt("id");
            } else {
                Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, "Insert of String '" + object + "' silently failed");
            }
        } catch (SQLException ex) {
            Logger.getLogger(URLDB.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                }
                rs = null;
            }
        }
        return ret;
    }

    public void remove(int id) {
        try {
            delete.clearParameters();
            delete.setInt(1, id);
            delete.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(StringDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Class getValueClass() {
        return String.class;
    }
}
