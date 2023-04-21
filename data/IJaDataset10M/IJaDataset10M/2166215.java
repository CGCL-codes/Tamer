package org.hkupp.db.accessors;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * This class extends the generated dbuser table accessor.
 *
 * @author Lennart Martens
 * @version $Id$
 */
public class Dbuser extends DbuserTableAccessor {

    public Dbuser(ResultSet aResultSet) throws SQLException {
        super(aResultSet);
    }

    public static Dbuser[] getDbusers(Connection aConn) throws SQLException {
        ArrayList<Dbuser> dbUsers = new ArrayList<Dbuser>();
        Statement stat = aConn.createStatement();
        ResultSet rs = stat.executeQuery("select * from dbuser");
        while (rs.next()) {
            dbUsers.add(new Dbuser(rs));
        }
        rs.close();
        stat.close();
        Dbuser[] result = new Dbuser[dbUsers.size()];
        dbUsers.toArray(result);
        return result;
    }

    public String toString() {
        return getName();
    }
}
