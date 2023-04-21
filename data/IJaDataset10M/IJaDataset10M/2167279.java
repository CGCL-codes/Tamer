package ch.jester.db.hsqldb;

import java.sql.SQLException;
import org.hsqldb.Server;
import ch.jester.commonservices.api.persistency.IDatabaseManager;
import ch.jester.commonservices.api.persistency.IORMConfiguration;
import ch.jester.orm.ORMPlugin;

/**
 * Implementation von {@link IDatabaseManager} für HSQL
 *
 */
public class HSQLDatabaseManager implements IDatabaseManager {

    private Server server;

    private String mDbOptions = ";hsqldb.default_table_type=cached;hsqldb.tx=mvcc";

    private IORMConfiguration mConfig;

    public HSQLDatabaseManager() {
        server = new Server();
    }

    @Override
    public void start() {
        String ip = mConfig.getDefaultPath();
        String connection = ip + "/" + mConfig.getDbname() + mConfig.getConnectionOptions();
        connection = "jdbc:hsqldb:hsql://localhost:9001/" + mConfig.getDbname();
        mConfig.setConnectionurl(connection);
        server.setDatabaseName(0, mConfig.getDbname());
        server.setDatabasePath(0, ip + "/" + mConfig.getDbname() + mDbOptions);
        server.setSilent(false);
        server.start();
    }

    @Override
    public void stop() {
        try {
            ORMPlugin.getConfiguration().getConnection().createStatement().execute("shutdown");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        server.stop();
        while ((server.getState()) != 16) {
        }
        ;
    }

    @Override
    public void shutdown() {
        server.shutdown();
    }

    @Override
    public void editORMConfiguration(IORMConfiguration pConfig) {
        mConfig = pConfig;
    }
}
