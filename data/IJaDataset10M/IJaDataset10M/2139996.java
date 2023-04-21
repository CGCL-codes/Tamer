package org.snipsnap.config;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;
import java.util.Properties;

/**
 * Interface template for global configuration options
 * @author Matthias L. Jugel
 * @version $Id: Globals.java,v 1.1 2007-07-10 16:35:58 angelo Exp $
 */
public interface Globals {

    public String getGlobal(String name);

    public void setGlobal(String name, String value);

    public void loadGlobals(InputStream stream) throws IOException;

    public void storeGlobals(OutputStream stream) throws IOException;

    public Properties getGlobals();

    public String getGlobalDefault(String name);

    public void setWebInfDir(File file);

    public File getWebInfDir();

    public String getVersion();

    boolean isInstalled();

    public static final String APP_CACHE = "app.cache";

    public String getCache();

    public String setCache(String value);

    public static final String APP_DATABASE = "app.database";

    public String getDatabase();

    public String setDatabase(String value);

    public static final String APP_ENCODING = "app.encoding";

    public String getEncoding();

    public String setEncoding(String value);

    public static final String APP_FILE_STORE = "app.file.store";

    public String getFileStore();

    public String setFileStore(String value);

    public static final String APP_HOST = "app.host";

    public String getHost();

    public String setHost(String value);

    public static final String APP_INSTALL_KEY = "app.install.key";

    public String getInstallKey();

    public String setInstallKey(String value);

    public static final String APP_INSTALLED = "app.installed";

    public String getInstalled();

    public String setInstalled(String value);

    public static final String APP_JDBC_DRIVER = "app.jdbc.driver";

    public String getJdbcDriver();

    public String setJdbcDriver(String value);

    public static final String APP_JDBC_PASSWORD = "app.jdbc.password";

    public String getJdbcPassword();

    public String setJdbcPassword(String value);

    public static final String APP_JDBC_URL = "app.jdbc.url";

    public String getJdbcUrl();

    public String setJdbcUrl(String value);

    public static final String APP_JDBC_USER = "app.jdbc.user";

    public String getJdbcUser();

    public String setJdbcUser(String value);

    public static final String APP_LOGGER = "app.logger";

    public String getLogger();

    public String setLogger(String value);

    public static final String APP_PATH = "app.path";

    public String getPath();

    public String setPath(String value);

    public static final String APP_PORT = "app.port";

    public String getPort();

    public String setPort(String value);

    public static final String APP_PROTOCOL = "app.protocol";

    public String getProtocol();

    public String setProtocol(String value);

    public static final String SNIPSNAP_SERVER_VERSION = "snipsnap.server.version";

    public String getSnipsnapServerVersion();

    public String setSnipsnapServerVersion(String value);
}
