package dk.highflier.airlog.dataaccess.dboracle;

import java.sql.*;
import java.util.Vector;
import dk.highflier.airlog.utility.*;
import dk.highflier.airlog.*;

public class PlaneDAImpl implements dk.highflier.airlog.dataaccess.PlaneDA {

    Vector result = null;

    int count = 0, x = 0;

    private org.log4j.Category log = org.log4j.Category.getInstance("Log.PlanesDB");

    private JdbcConnection conn;

    public PlaneDAImpl(JdbcConnection conn) {
        this.conn = conn;
    }

    /**
     * Method will generate an Object[] consisting of Object[]<br>
     * [0] = Name of plane
     * [1] = ID of plane
     *
     * @return Object[]
     */
    public Vector get() {
        Vector result = null;
        try {
            ResultSet rs;
            Statement stmt = conn.getStatement();
            rs = stmt.executeQuery("select navn, id from flytype order by navn");
            if (!rs.next()) {
                log.info("No rows in table flytype");
                result = null;
                return result;
            }
            result = new Vector(200);
            do {
                Object[] row = new Object[2];
                row[0] = rs.getString(1);
                row[1] = new Integer(rs.getInt(2));
                result.addElement(row);
            } while (rs.next());
        } catch (SQLException sqle) {
            log.debug(sqle);
        }
        return result;
    }
}
