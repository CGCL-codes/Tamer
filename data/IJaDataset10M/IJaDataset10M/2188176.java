package biz.wavelet.thickclient.data.db;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.Connection;
import java.util.logging.Logger;

public class BaseDAO {

    public String driver = "org.apache.derby.jdbc.EmbeddedDriver";

    public String protocol = "jdbc:derby:thickClient;create=true";

    public DriverManager dm;

    protected Logger log;

    public BaseDAO() {
        log = Logger.getLogger(BaseDAO.class.getName());
        log.info("Instantiating DriverManager");
        try {
            Class.forName(driver).newInstance();
        } catch (Exception ex) {
            log.severe(ex.getLocalizedMessage());
        }
    }

    public Connection getConnnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(protocol);
            conn.setAutoCommit(false);
        } catch (Exception e) {
            log.severe(e.getLocalizedMessage());
        }
        log.info("Returning new connection");
        return conn;
    }

    public void closeConnection(Connection conn) {
        try {
            log.info("Committing and closing connection");
            conn.commit();
            conn.close();
        } catch (Exception ex) {
            log.severe(ex.getClass().toString());
        }
    }
}
