package net.xmlmiddleware.server;

import java.sql.*;
import de.tudarmstadt.ito.xmldbms.db.*;
import java.util.Properties;
import net.xmlmiddleware.props.ServerProps;

public class SQLHandler {

    public java.sql.Connection conn;

    public static final String REVISION_ID = "$Id: SQLHandler.java,v 1.1.1.1 2002/08/20 15:49:40 adamf Exp $";

    public static final String REVISION_TAG = "$Name:  $";

    private DbConn dbConn;

    /**
	 * SQLHandler constructor comment.
	 */
    public SQLHandler() {
        super();
    }

    /**
	 * A method to allow for querying the DB such that you
	 * could put something in place to check that everything is OK
	 * It will also be used by the Admin Class to get (DB) System Info 
	 * Back.
	 * Creation date: (30/08/01 12:13:05)
	 * @param prop java.util.Properties
	 * @param Statement java.lang.String
	 * @exception java.sql.SQLException The exception description.
	 */
    public ResultSet handleSelect(java.util.Properties prop, String Statement) throws java.sql.SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, java.lang.Exception {
        setDatabaseProperties(prop);
        conn = dbConn.getConn();
        PreparedStatement ps = statementBuilder(Statement);
        ResultSet RS = null;
        RS = ps.executeQuery();
        return RS;
    }

    /**
	 * Handles the Statement
	 * Creation date: (30/08/01 12:13:05)
	 * @param prop java.util.Properties
	 * @param Statement java.lang.String
	 * @exception java.sql.SQLException The exception description.
	 */
    public void handleStatement(java.util.Properties prop, String Statement) throws java.sql.SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, java.lang.Exception {
        setDatabaseProperties(prop);
        conn = dbConn.getConn();
        PreparedStatement ps = statementBuilder(Statement);
        String action = prop.getProperty(ServerProps.UPDATESQLSTATEMENTACTION);
        if (action.equalsIgnoreCase("update")) {
            ps.executeUpdate();
        } else if (action.equalsIgnoreCase("select")) {
            ps.executeQuery();
        }
    }

    /**
	 * Insert the method's description here.
	 * Creation date: (30/08/01 12:13:05)
	 * @param prop java.util.Properties
	 * @param Statement java.lang.String
	 * @exception java.sql.SQLException The exception description.
	 */
    public int handleUpdate(java.util.Properties prop, String Statement) throws java.sql.SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException, java.lang.Exception {
        setDatabaseProperties(prop);
        conn = dbConn.getConn();
        PreparedStatement ps = statementBuilder(Statement);
        String action = prop.getProperty(ServerProps.UPDATESQLSTATEMENTACTION);
        int i = 0;
        i = ps.executeUpdate();
        return i;
    }

    private Object instantiateClass(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        if (className == null) return null;
        return Class.forName(className).newInstance();
    }

    /**
		* Set the properties used to connect to the database.
		*
		* <p>This method must be called before transferring data between
		* an XML document and a database. The following properties are
		* accepted:</p>
		*
		* <ul>
		* <li>Driver: Name of the JDBC driver class to use. Required.</li>
		* <li>URL: URL of the database containing the XMLDBMSKey table. Required.</li>
		* <li>User: Database user name. Optional.</li>
		* <li>Password: Database password. Optional.</li>
		* </ul>
		*
		* @param props A Properties object containing the above properties.
		* Changed by Adam Flinton 08/04/2001. Now the method evaluates the JDBC level to work out
		* which JDBC level to use (presently 1 or 2). Level 2 requires JNDI & Javax.sql so to compensate
		* for the possibility of level 1 useage instantiation is used.
		* If & when JDBC level 3 comes along shoving it in as well should be easy.
		*/
    public void setDatabaseProperties(Properties props) throws ClassNotFoundException, IllegalAccessException, InstantiationException, java.lang.Exception {
        int i = 0;
        String JDBC = props.getProperty(DBProps.JDBCLEVEL);
        if (JDBC == null) {
            i = 1;
        } else {
            try {
                i = Integer.parseInt(JDBC.trim());
            } catch (NumberFormatException nfe) {
                System.out.println("JDBC Level MUST be a number " + nfe.getMessage());
                i = 1;
            }
        }
        switch(i) {
            case 1:
                dbConn = (DbConn) instantiateClass("de.tudarmstadt.ito.xmldbms.db.DbConn1");
                break;
            case 2:
                dbConn = (DbConn) instantiateClass("de.tudarmstadt.ito.xmldbms.db.DbConn2");
                break;
        }
        dbConn.setDB(props);
    }

    PreparedStatement statementBuilder(String Statement) throws SQLException {
        if (conn == null) throw new IllegalStateException("Connection not set.");
        try {
            return conn.prepareStatement(Statement);
        } catch (SQLException se) {
            throw se;
        }
    }
}
