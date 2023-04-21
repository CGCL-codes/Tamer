package dk.highflier.airlog.dataaccess.dbinstantdb;

import java.sql.*;
import java.util.Vector;
import dk.highflier.airlog.utility.*;

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
        try {
            Statement stmt = conn.getStatement();
            ResultSet rs = stmt.executeQuery("select count(id) from flytype");
            if (rs.next()) count = rs.getInt(1);
            if (count == 0) {
                log.info("No planes !");
                stmt.close();
                return result;
            }
            rs = stmt.executeQuery("select navn, id from flytype order by navn");
            result = new Vector(count);
            while (rs.next()) {
                Object[] ar = new Object[2];
                ar[0] = rs.getString(1);
                ar[1] = new Integer(rs.getInt(2));
                result.addElement(ar);
            }
            stmt.close();
        } catch (SQLException sqle) {
            log.error(sqle);
        }
        return result;
    }
}
