package org.apache.torque.adapter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is used to connect to Hypersonic SQL databases. <a href="http://axion.tigris.org">http://axion.tigris.org</a>
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @version $Id: DBAxion.java,v 1.3 2005-06-25 17:39:47 psy666m Exp $
 */
public class DBAxion extends DB {

    /**
     * Constructor.
     */
    protected DBAxion() {
    }

    /**
     * This method is used to ignore case.
     * @param in The string to transform to upper case.
     * @return The upper case string.
     */
    public String toUpperCase(String in) {
        return in;
    }

    /**
     * This method is used to ignore case.
     * @param in The string whose case to ignore.
     * @return The string in a case that can be ignored.
     */
    public String ignoreCase(String in) {
        return in;
    }

    /**
     * @see org.apache.torque.adapter.DB#getIDMethodType()
     */
    public String getIDMethodType() {
        return NO_ID_METHOD;
    }

    /**
     * @see org.apache.torque.adapter.DB#getIDMethodSQL(Object obj)
     */
    public String getIDMethodSQL(Object obj) {
        return null;
    }

    /**
     * Locks the specified table.
     * @param con The JDBC connection to use.
     * @param table The name of the table to lock.
     * @exception SQLException No Statement could be created or executed.
     */
    public void lockTable(Connection con, String table) throws SQLException {
    }

    /**
     * Unlocks the specified table.
     * @param con The JDBC connection to use.
     * @param table The name of the table to unlock.
     * @exception SQLException No Statement could be created or executed.
     */
    public void unlockTable(Connection con, String table) throws SQLException {
    }
}
