package org.hardtokenmgmt.server.uds.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.ejbca.core.model.log.Admin;
import org.ejbca.core.model.ra.userdatasource.MultipleMatchException;
import org.ejbca.core.model.ra.userdatasource.UserDataSourceConnectionException;
import org.ejbca.core.model.ra.userdatasource.UserDataSourceException;
import org.ejbca.core.model.ra.userdatasource.UserDataSourceVO;
import org.hardtokenmgmt.server.uds.BaseCustomUserDataSource;

/**
 * A General SQL User Data Source for use with htmf.
 * 
 * 
 * @author Philip Vendil 16 dec 2008
 *
 * @version $Id$
 */
public class SQLUserDataSource extends BaseCustomUserDataSource {

    private static Logger log = Logger.getLogger(SQLUserDataSource.class);

    /**
	 * Class path to JDBC driver to use.
	 */
    public static final String DBDRIVER = "db.driver";

    /**
	 * DB connection URL
	 */
    public static final String DBURL = "db.url";

    /**
	 * Username used to connect to database.
	 */
    public static final String DBUSERNAME = "user";

    /**
	 * Password used to connect to database.
	 */
    public static final String DBPASSWORD = "password";

    /**
	 * complete test query string used for making sure the
	 * database is up and running example value is
	 * "select 1" but the actual query depends on database type.
	 */
    public static final String TESTQUERY = "testquery";

    /**
	 * Complete SQL query used to fetch user data from database.
	 * it should return only few columns in the following order
	 * in one of following order, depending on column type.
	 * <ul>
	 * <li>uid,  e-mail, full name 
	 * <li>uid,  e-mail, first name, surname
	 * <li>uid,  e-mail, first name, middle name, surname
	 */
    public static final String FETCHQUERY = "fetchquery";

    /**
	 * Setting defining how the full name should be fetched.
	 * can be one of fullname, firstandsurname and firstmiddleandsurname values
	 * and indicates how column 3 and up should be fetched from database.
	 * 
	 */
    public static final String NAMECOLUMNS = "namecolumns";

    public static final String NAMECOLUMNS_FULLNAME = "fullname";

    public static final String NAMECOLUMNS_FIRSTANDSURNAME = "firstandsurname";

    public static final String NAMECOLUMNS_FIRSTMIDDLEANDSURNAME = "firstmiddleandsurname";

    /**
	 * Boolean string indicating if remove user data should be supported.
	 * Default is "FALSE"
	 */
    public static final String SUPPORTREMOVE = "supportremove";

    public static final String DEFAULT_SUPPORTREMOVE = "FALSE";

    /**
	 * Complete SQL used to get the size in "remove" queries, the call
	 * is done if by remove user call have multiple match is false.
	 * The query should be a count(*) query returning an integer value.
	 * 
	 */
    public static final String REMOVESIZEQUERY = "removesizequery";

    /**
	 * Complete SQL used in "remove" queries, the call
	 * is called after a fetch query to make sure not more
	 * than one entry matches the search string. 
	 */
    public static final String REMOVEQUERY = "removequery";

    private Connection con = null;

    public Collection<?> fetch(Admin admin, String searchString) throws UserDataSourceException {
        Collection<UserDataSourceVO> retval = new ArrayList<UserDataSourceVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = getJDBCConnection().prepareStatement(getSetting(FETCHQUERY));
            ps.setString(1, searchString);
            rs = ps.executeQuery();
            while (rs.next()) {
                String uid = rs.getString(1);
                String eMail = rs.getString(2);
                String fullName = getFullname(rs);
                retval.add(getUserDataSourceVO(searchString, uid, fullName, eMail, null, null));
            }
        } catch (SQLException e) {
            throw new UserDataSourceException("Error when performing fetch query : " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new UserDataSourceException("Error when performing fetch query : " + e.getMessage());
            }
        }
        return retval;
    }

    /**
	 * Returns the full name from the result set depending 
	 * on the namecolumns setting
	 * @param rs
	 * @return the full name constructed from the result set.
	 * @throws UserDataSourceException 
	 * @throws SQLException 
	 */
    private String getFullname(ResultSet rs) throws UserDataSourceException, SQLException {
        if (getSetting(NAMECOLUMNS).equals(NAMECOLUMNS_FULLNAME)) {
            return removeNullFromResult(rs, 3);
        }
        if (getSetting(NAMECOLUMNS).equals(NAMECOLUMNS_FIRSTANDSURNAME)) {
            return removeNullFromResult(rs, 3) + " " + removeNullFromResult(rs, 4);
        }
        if (getSetting(NAMECOLUMNS).equals(NAMECOLUMNS_FIRSTMIDDLEANDSURNAME)) {
            return removeNullFromResult(rs, 3) + " " + removeNullFromResult(rs, 4) + " " + removeNullFromResult(rs, 5);
        }
        return null;
    }

    private String removeNullFromResult(ResultSet rs, int columnIndex) throws SQLException {
        String result = rs.getString(columnIndex);
        if (result == null) {
            return "";
        }
        return result;
    }

    public boolean removeUserData(Admin admin, String searchString, boolean removeMultipleMatch) throws MultipleMatchException, UserDataSourceException {
        boolean retval = true;
        if (props.getProperty(SUPPORTREMOVE, DEFAULT_SUPPORTREMOVE).trim().equalsIgnoreCase("TRUE")) {
            retval = false;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                if (!removeMultipleMatch) {
                    ps = getJDBCConnection().prepareStatement(getSetting(REMOVESIZEQUERY));
                    ps.setString(1, searchString);
                    rs = ps.executeQuery();
                    int size = rs.getInt(1);
                    if (size > 1) {
                        throw new MultipleMatchException("Error the search string : " + searchString + " matches more than one user (" + size + ") in the database");
                    }
                }
                ps = getJDBCConnection().prepareStatement(getSetting(REMOVEQUERY));
                ps.setString(1, searchString);
                rs = ps.executeQuery();
                retval = rs.rowDeleted();
            } catch (SQLException e) {
                throw new UserDataSourceException("Error when performin remove query : " + e.getMessage());
            } finally {
                try {
                    if (ps != null) {
                        ps.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    throw new UserDataSourceException("Error when performin remove query : " + e.getMessage());
                }
            }
            return retval;
        }
        return retval;
    }

    public void testConnection(Admin admin) throws UserDataSourceConnectionException {
        if (props.getProperty(TESTQUERY) != null) {
            Statement s = null;
            try {
                s = getJDBCConnection().createStatement();
                s.executeQuery(props.getProperty(TESTQUERY));
            } catch (SQLException e) {
                throw new UserDataSourceConnectionException("Error when performin test query : " + e.getMessage());
            } catch (UserDataSourceException e) {
                throw new UserDataSourceConnectionException(e.getMessage());
            } finally {
                try {
                    if (s != null) {
                        s.close();
                    }
                } catch (SQLException e) {
                    throw new UserDataSourceConnectionException("Error when performin test query : " + e.getMessage());
                }
            }
        }
    }

    private Connection getJDBCConnection() throws UserDataSourceException, SQLException {
        if (con == null || con.isClosed()) {
            try {
                Class.forName(getSetting(DBDRIVER));
            } catch (java.lang.ClassNotFoundException e) {
                throw new UserDataSourceException("Error driver class " + getSetting(DBDRIVER) + " doesn't exist in classpath.");
            }
            try {
                con = DriverManager.getConnection(getSetting(DBURL), props);
            } catch (SQLException ex) {
                log.error("SQLException when creating database connection : " + ex.getMessage(), ex);
                throw new UserDataSourceException("SQLException when creating database connection : " + ex.getMessage());
            }
        }
        return con;
    }
}
