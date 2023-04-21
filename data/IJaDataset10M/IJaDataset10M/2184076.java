package be.vds.jtbdive.client.core.config;

import be.vds.jtbdive.client.integration.businessdelegate.mysql.jdbc.MysqlJdbcConfiguratorBusinessDelegate;
import be.vds.jtbdive.client.integration.businessdelegate.mysql.jdbc.MysqlJdbcDiveLocationManagerBusinessDelegate;
import be.vds.jtbdive.client.integration.businessdelegate.mysql.jdbc.MysqlJdbcDiverBusinessDelegate;
import be.vds.jtbdive.client.integration.businessdelegate.mysql.jdbc.MysqlJdbcLogBookBusinessDelegate;

public class MysqlJdbcConfiguration extends JdbcConfiguration {

    @Override
    protected void initializeDiveLocationBD() {
        diveLocationBusinessDelegate = new MysqlJdbcDiveLocationManagerBusinessDelegate(connectionManager);
    }

    @Override
    protected void initializeDiverBD() {
        diverBusinessDelegate = new MysqlJdbcDiverBusinessDelegate(connectionManager);
    }

    @Override
    protected void initializeLogBookBD() {
        logbookBusinessDelegate = new MysqlJdbcLogBookBusinessDelegate(connectionManager);
    }

    @Override
    protected void initializeDBConfiguratorDB() {
        dbConfiguratorBusinessDelegate = new MysqlJdbcConfiguratorBusinessDelegate(connectionManager);
    }

    public String getConfigType() {
        return CONFIG_MYSQL_JDBC;
    }
}
