package com.vgkk.hula.sql;

import java.sql.*;

/**
 * This is the Database object for the PostgrSQL
 */
public class PostgresDatabase extends Database {

    /**
	 * Creates database object for given connection string, user and password
	 *
	 * @param connectString connection string.
	 * @param user name of user who will use the database
	 * @param password
	 */
    public PostgresDatabase(String connectString, String user, String password) {
        super(connectString, user, password);
    }

    /**
	 * Returns the next available id in the named sequence from the named table.
	 * These is no need for a transacton since HSQL only has one thread
	 * accessing the database at a time.
	 * @param con Database Connection
	 * @param sequenceName name of the squence to query
	 * @return the next ID in the sequence.
	 */
    public synchronized int getNextID(Connection con, String sequenceName) throws SQLException {
        Statement st = con.createStatement();
        ResultSet rs = null;
        try {
            rs = st.executeQuery("SELECT nextval(\'" + sequenceName + "\')");
            rs.next();
            return rs.getInt(1);
        } finally {
            closeResultSet(rs);
            closeStatement(st);
        }
    }

    /**
     * Creates a sequence in the database
     * @param con connection to the database
     * @param seqenceName the sequence to be created
     * @param startingValue starting value of that seqence. This value will be
     *         returned the first time getNextID() is called on this sequence
     * @throws SQLException
     */
    public void createSequence(Connection con, String seqenceName, int startingValue) throws SQLException {
        String sqlDef = "CREATE SEQUENCE " + seqenceName + " START " + startingValue + " INCREMENT 1 MAXVALUE 2147483647 MINVALUE 1 CACHE 1";
        Statement st = null;
        try {
            st = con.createStatement();
            st.execute(sqlDef);
        } finally {
            Database.closeStatement(st);
        }
    }
}
