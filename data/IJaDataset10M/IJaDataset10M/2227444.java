package fr.soleil.hdbtdbArchivingApi.ArchivingApi.DataBaseManagement.PoolConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import fr.esrf.Tango.ErrSeverity;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.GlobalConst;
import fr.soleil.hdbtdbArchivingApi.ArchivingTools.Tools.ArchivingException;

public class HDBMySqlPoolConnection implements IPoolConnection {

    private static DataSource dataSource;

    public HDBMySqlPoolConnection() {
    }

    public DataSource getPoolInstance(String url, String user, String pwd) throws ArchivingException {
        if (dataSource == null) {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(url, user, pwd);
                dataSource = new MysqlDataSource();
                ((MysqlDataSource) dataSource).setURL(url);
                ((MysqlDataSource) dataSource).setUser(user);
                ((MysqlDataSource) dataSource).setPassword(pwd);
                ((MysqlDataSource) dataSource).setMetadataCacheSize(50);
                if (conn != null) conn.close();
            } catch (SQLException e) {
                if (conn != null) try {
                    conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                String mysqlMessage = GlobalConst.ARCHIVING_ERROR_PREFIX + " : " + "CONNECTION FAILED !";
                String mysqlReason = "Failed while executing ConnectionFactory.connect_auto() method...";
                String mysqlDesc = "Failed while connecting to the MySQL archiving database";
                throw new ArchivingException(mysqlMessage, mysqlReason, ErrSeverity.PANIC, mysqlDesc, "");
            }
        }
        return dataSource;
    }
}
