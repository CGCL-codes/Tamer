package net.sf.ifw2rep.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import net.infordata.ifw2.web.util.SecurityContext;
import org.apache.log4j.Logger;
import org.hibernate.connection.DatasourceConnectionProvider;

/**
 * Use it to propagate to the db layer the {@link SecurityContext#getRemoteUser()}.<br>
 * Supports MySQL jdbc drivers only.<br>
 * Should be configured in the hibernate.cfg.xml as follow:<br>
 * <pre>
 *   <property name="hibernate.connection.provider_class">
 *     com.pfizer.pgm.coas.util.MySQLDataSourceConnectionProvider
 *   </property>
 * </pre>
 * @author valentino.proietti
 */
public class MySQLDataSourceConnectionProvider extends DatasourceConnectionProvider {

    private static final Logger LOGGER = Logger.getLogger(MySQLDataSourceConnectionProvider.class);

    private volatile boolean ivMysql = true;

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = super.getConnection();
        SecurityContext sc = SecurityContext.get();
        String userName = sc != null ? sc.getRemoteUser() : null;
        if (ivMysql) {
            try {
                PreparedStatement st = conn.prepareStatement("set @CLIENT_USER=?");
                st.setString(1, userName);
                st.execute();
                st.close();
            } catch (Exception ex) {
                ivMysql = false;
                LOGGER.error("Disabling MySQL \"set @CLIENT_USER\", " + "Cannot propagate user name to the db layer", ex);
            }
        }
        return conn;
    }

    @Override
    public void closeConnection(Connection conn) throws SQLException {
        if (ivMysql) {
            try {
                PreparedStatement st = conn.prepareStatement("set @CLIENT_USER=?");
                st.setString(1, null);
                st.execute();
                st.close();
            } catch (Exception ex) {
                ivMysql = false;
                LOGGER.error("Disabling MySQL \"set @CLIENT_USER\"", ex);
            }
        }
        super.closeConnection(conn);
    }
}
