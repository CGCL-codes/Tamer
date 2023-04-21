package com.jstudio.config;

import com.jstudio.logging.Logger;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.Serializable;

public class ApplicationConfig implements Serializable {

    private static Logger logger = Logger.newLogger(ApplicationConfig.class);

    public static ApplicationConfig getConfig() {
        if (instance == null) {
            throw new RuntimeException("Application has not initialized");
        }
        return instance;
    }

    public static ApplicationConfig config(String resource) {
        Digester digester = new Digester();
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        digester.push(instance);
        digester.addObjectCreate("jdep-config/database", "com.jstudio.config.DatabaseConfig");
        digester.addCallMethod("jdep-config/database/connection-provider", "setConnectionProvider", 1);
        digester.addCallParam("jdep-config/database/connection-provider", 0, "className");
        digester.addCallMethod("jdep-config/database/connection-provider/driver", "setDataSource", 4);
        digester.addCallParam("jdep-config/database/connection-provider/driver", 0, "className");
        digester.addCallParam("jdep-config/database/connection-provider/driver", 1, "URL");
        digester.addCallParam("jdep-config/database/connection-provider/driver", 2, "username");
        digester.addCallParam("jdep-config/database/connection-provider/driver", 3, "password");
        digester.addCallMethod("jdep-config/database/connection-provider/connection-pool", "setPool", 3);
        digester.addCallParam("jdep-config/database/connection-provider/connection-pool", 0, "minConnections");
        digester.addCallParam("jdep-config/database/connection-provider/connection-pool", 1, "maxConnections");
        digester.addCallParam("jdep-config/database/connection-provider/connection-pool", 2, "connectionTimeout");
        digester.addCallMethod("jdep-config/database/property", "setProperty", 2);
        digester.addCallParam("jdep-config/database/property", 0, "name");
        digester.addCallParam("jdep-config/database/property", 1, "value");
        digester.addCallMethod("jdep-config/database/queries", "setDefaultPageSize", 1);
        digester.addCallParam("jdep-config/database/queries", 0, "defaultPageSize");
        digester.addObjectCreate("jdep-config/database/queries/query", "com.jstudio.database.QueryPair");
        digester.addSetProperties("jdep-config/database/queries/query");
        digester.addCallMethod("jdep-config/database/queries/query/SQL", "setSQL", 0);
        digester.addCallMethod("jdep-config/database/queries/query/count", "setCount", 0);
        digester.addCallMethod("jdep-config/database/queries/query/pageSize", "setPageSize", 0);
        digester.addSetNext("jdep-config/database/queries/query", "addQuery", "com.jstudio.database.QueryPair");
        digester.addSetNext("jdep-config/database", "setDatabaseConfig");
        digester.addObjectCreate("jdep-config/cache", "com.jstudio.config.CacheConfig");
        digester.addSetProperties("jdep-config/cache");
        digester.addCallMethod("jdep-config/cache/entity", "addEntity", 5);
        digester.addCallParam("jdep-config/cache/entity", 0, "name");
        digester.addCallParam("jdep-config/cache/entity", 1, "capacity");
        digester.addCallParam("jdep-config/cache/entity", 2, "initSize");
        digester.addCallParam("jdep-config/cache/entity", 3, "updateTime");
        digester.addCallParam("jdep-config/cache/entity", 4, "timeout");
        digester.addSetNext("jdep-config/cache", "setCacheConfig");
        try {
            digester.parse(resource);
        } catch (IOException e) {
            logger.error("Application Initial Error", e);
        } catch (SAXException e) {
            logger.error("Application Initial Error", e);
        }
        return instance;
    }

    public void setCacheConfig(CacheConfig cachecfg) {
        this.cachecfg = cachecfg;
    }

    public CacheConfig getCacheConfig() {
        return cachecfg;
    }

    public void setDatabaseConfig(DatabaseConfig dbcfg) {
        this.dbcfg = dbcfg;
    }

    public DatabaseConfig getDatabaseConfig() {
        return dbcfg;
    }

    public String toString() {
        return "ApplicationConfig = {[DatabaseConfig = " + dbcfg + "],[CacheConfig = " + cachecfg + "]}";
    }

    private static ApplicationConfig instance;

    private DatabaseConfig dbcfg;

    private CacheConfig cachecfg;

    private ApplicationConfig() {
    }
}
