package dbsync4j.core.concrete;

import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import dbsync4j.core.behavior.DBSyncManager;
import dbsync4j.core.behavior.Schema;
import dbsync4j.core.behavior.Server;
import dbsync4j.core.exception.DataSourceException;
import dbsync4j.core.exception.DataSourceMissingParameters;
import dbsync4j.utils.DBSync4JUtils;

/**
 * Gerencia a conex�o com os servidores e efetua o carregamento dos metadados.
 * 
 * @author Rafael
 *
 */
public class ConcreteDBSyncManager implements DBSyncManager {

    public static Locale LOCALE = null;

    private Map<String, Server> registeredServers = new HashMap<String, Server>();

    private long executionTime;

    public ConcreteDBSyncManager() {
    }

    public void connectServer(Server server) {
        try {
            if (server != null) {
                if (server.isConnected()) {
                    if (registeredServers.get(server.getName()) == null) {
                        registeredServers.put(server.getName(), server);
                    }
                } else {
                    server.connect();
                    registeredServers.put(server.getName(), server);
                }
            }
        } catch (DataSourceException e) {
            e.printStackTrace();
        } catch (DataSourceMissingParameters e) {
            e.printStackTrace();
        }
    }

    public void disconnectServer(Server server) {
        if (server != null) {
            try {
                server.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (!server.isConnected()) {
                registeredServers.remove(server.getName());
            }
        }
    }

    public Schema loadSchema(Server server) {
        long init = System.currentTimeMillis();
        Server s = registeredServers.get(server.getName());
        Schema loadedSchema = null;
        if (s != null) {
            loadedSchema = s.getSchema();
        }
        long end = System.currentTimeMillis();
        this.executionTime = end - init;
        return loadedSchema;
    }

    public void refresh() {
    }

    @Override
    public Schema loadFromFile(File path) {
        return null;
    }

    @Override
    public Schema loadFromFile(String path) {
        return null;
    }

    @Override
    public void saveToFile(Server server) {
    }

    @Override
    public long loadTime() {
        return this.executionTime / 1000;
    }

    @Override
    public void setLocale(Locale locale) {
        LOCALE = locale;
        DBSync4JUtils.loadResourceBundle("i18n.ResourceBundle", locale);
    }
}
