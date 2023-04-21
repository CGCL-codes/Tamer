package dk.nullesoft.Airlog.DBoracle;

import java.util.*;
import java.sql.*;
import dk.nullesoft.Airlog.*;

public class AirFieldsDB implements dk.nullesoft.Airlog.AirFieldsDB {

    private org.log4j.Category log = org.log4j.Category.getInstance("Log.AirFieldsDB");

    private JdbcConnection conn;

    public AirFieldsDB(JdbcConnection conn) {
        this.conn = conn;
    }

    /**
     * Method will generate a Vector of Object[]<br>
     * [0] = Name of airfield<br>
     * [1] = ID of airfield
     *
     * @return Object[]
     */
    public Vector get() {
        Vector result = null;
        try {
            ResultSet rs;
            Statement stmt = conn.getStatement();
            rs = stmt.executeQuery("select name, id from airfield order by name");
            if (!rs.next()) {
                log.error("No rows in table airfield");
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
