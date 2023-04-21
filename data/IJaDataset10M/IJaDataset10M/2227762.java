package pt.ips.estsetubal.mig.academicCloud.server.resources.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import pt.ips.estsetubal.mig.academicCloud.server.helper.ServerApplicationHelper;

/**
 * Class responsible to get a connection to the database.
 * 
 * @author António Casqueiro
 */
public class DatabaseConnection {

    private static String sDriver = ServerApplicationHelper.getInstance().getConfiguration().getJdbcDriver();

    private static String sConnectString = ServerApplicationHelper.getInstance().getConfiguration().getJdbcUrl();

    private static String sUserName = ServerApplicationHelper.getInstance().getConfiguration().getJdbcUserName();

    private static String sPassword = ServerApplicationHelper.getInstance().getConfiguration().getJdbcPassword();

    static {
        try {
            Class.forName(sDriver);
        } catch (Exception e) {
            ServerApplicationHelper.getInstance().getLog().debug("Unable to load driver", e);
            System.exit(0);
        }
    }

    /**
	 * Get the connection
	 * 
	 * @return connection
	 * @throws SQLException
	 */
    public static Connection getConnection() throws SQLException {
        Connection con;
        con = DriverManager.getConnection(sConnectString, sUserName, sPassword);
        return con;
    }
}
