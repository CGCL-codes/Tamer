package schemacrawler.schemacrawler;

import java.util.HashMap;
import java.util.Map;
import sf.util.TemplatingUtility;
import sf.util.Utility;

public final class DatabaseConfigConnectionOptions extends BaseDatabaseConnectionOptions {

    private static final long serialVersionUID = -8141436553988174836L;

    private static final String DRIVER = "driver";

    private static final String URL = "url";

    private static final String HOST = "host";

    private static final String PORT = "port";

    private static final String DATABASE = "database";

    private static final String USER = "user";

    private static final String PASSWORD = "password";

    private final Map<String, String> properties;

    public DatabaseConfigConnectionOptions(final Map<String, String> properties) throws SchemaCrawlerException {
        if (properties == null) {
            throw new SchemaCrawlerException("No connection properties provided");
        }
        this.properties = new HashMap<String, String>(properties);
        loadJdbcDriver(properties.get(DRIVER));
        setUser(properties.get(USER));
        setPassword(properties.get(PASSWORD));
    }

    @Override
    public String getConnectionUrl() {
        final Map<String, String> properties = new HashMap<String, String>(this.properties);
        TemplatingUtility.substituteVariables(properties);
        final String connectionUrl = properties.get(URL);
        if (!TemplatingUtility.extractTemplateVariables(connectionUrl).isEmpty()) {
            throw new IllegalArgumentException("Insufficient parameters for database connection URL");
        }
        return connectionUrl;
    }

    public String getDatabase() {
        return properties.get(DATABASE);
    }

    public String getHost() {
        return properties.get(HOST);
    }

    public int getPort() {
        try {
            return Integer.parseInt(properties.get(PORT));
        } catch (final NumberFormatException e) {
            return 0;
        }
    }

    public void setDatabase(final String database) {
        if (database != null) {
            properties.put(DATABASE, database);
        }
    }

    public void setHost(final String host) {
        if (!Utility.isBlank(host)) {
            properties.put(HOST, host);
        }
    }

    public void setPort(final int port) {
        if (port > 0) {
            properties.put(PORT, String.valueOf(port));
        }
    }
}
