package de.tudarmstadt.ukp.wikipedia.util;

import java.sql.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DbUtilities {

    private Connection conn;

    private final Log logger = LogFactory.getLog(getClass());

    public DbUtilities(Connection conn) {
        this.conn = conn;
    }

    public boolean tableExists(String tableName) {
        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            String[] types = { "TABLE" };
            ResultSet resultSet = dbmd.getTables(null, null, "%", types);
            while (resultSet.next()) {
                if (resultSet.getString("TABLE_NAME").equals(tableName)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            logger.error("Table " + tableName + " does not exist.", new Throwable());
        }
        return false;
    }
}
