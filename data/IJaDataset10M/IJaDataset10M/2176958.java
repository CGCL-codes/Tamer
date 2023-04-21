package com.cube42.util.database;

import java.text.MessageFormat;
import com.cube42.util.exception.Cube42Exception;
import com.cube42.util.exception.Cube42NullParameterException;

/**
 * Library of utility methods for working with the database
 *
 * @author  Matt Paulin
 * @version $Id: JDBCUtils.java,v 1.3 2003/03/12 00:28:13 zer0wing Exp $
 */
public class JDBCUtils {

    /**
     * Static string used in creating the URL string
     *
     * <ul>
     * <li> Param 0 = Host name </li>
     * <li> Param 1 = port number </li>
     * <li> Param 2 = database name </li>
     * <li> Param 3 = user </li>
     * <li> Param 4 = password </li>
     * </ul>
     */
    private static final String URL_FORMAT = "jdbc:mysql://{0}:{1}/{2}?user={3}&password={4}";

    /**
     * The methods of this class are static, and the class may not be
     * instantiated.
     */
    private JDBCUtils() {
    }

    /**
     * Loads the requested database drivers
     *
     * @param   The full classname of the driver, including the package
     *          Example: org.gjt.mm.mysql.Driver
     * @throws  Cube42Exception if the connection cannot be loaded
     * @throws  Cube42NullParameterException    if driverName is null
     */
    public static void loadDBDriver(String driverName) throws Cube42Exception {
        Cube42NullParameterException.checkNull(driverName, "driverName", "loadDBDriver", new JDBCUtils());
        try {
            Class.forName(driverName).newInstance();
        } catch (java.lang.InstantiationException e) {
            throw new Cube42Exception(DBSystemCodes.COULD_NOT_INSTANTIATE_DRIVER_EXCEPTION, new Object[] { driverName, e.getMessage() });
        } catch (java.lang.ClassNotFoundException e) {
            throw new Cube42Exception(DBSystemCodes.DRIVER_NOT_FOUND_EXCEPTION, new Object[] { driverName, e.getMessage() });
        } catch (java.lang.IllegalAccessException e) {
            throw new Cube42Exception(DBSystemCodes.COULD_NOT_ACCESS_DRIVER_EXCEPTION, new Object[] { driverName, e.getMessage() });
        }
    }

    /**
     * Forms the URL to uses when getting the connection to the database
     *
     * @param   dbHost      The host running the database
     * @param   dbPort      The port that the database is running on
     * @param   dbName      The name of the database
     * @param   dbUser      The user to connect to the database
     * @param   dbPassword  The password that corrisponds to the user
     * @throws  Cube42NullParameterException if any of the parameters are null
     */
    public static String formURL(String dbHost, int dbPort, String dbName, String dbUser, String dbPassword) {
        Cube42NullParameterException.checkNull(dbHost, "dbHost", "formURL", new JDBCUtils());
        Cube42NullParameterException.checkNull(dbName, "dbName", "formURL", new JDBCUtils());
        Cube42NullParameterException.checkNull(dbUser, "dbUser", "formURL", new JDBCUtils());
        Cube42NullParameterException.checkNull(dbPassword, "dbPassword", "formURL", new JDBCUtils());
        return MessageFormat.format(URL_FORMAT, new Object[] { dbHost, Integer.toString(dbPort), dbName, dbUser, dbPassword });
    }
}
