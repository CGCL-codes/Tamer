package pl.edu.agh.iosr.ftpserverremote.message;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.sql.DataSource;
import org.apache.ftpserver.FtpServerConfigurationException;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.interfaces.MessageResource;
import org.apache.ftpserver.util.IoUtils;
import org.apache.log4j.Logger;
import pl.edu.agh.iosr.ftpserverremote.connection.JDBCConnection;
import pl.edu.agh.iosr.ftpserverremote.server.ServerManagerImpl;

/**
 * This is a <code>MessageResource</code> implementation that gets messages from a database.
 * 
 * @author Tomasz Sadura
 *
 */
public class DBMessageResourceImpl implements MessageResource {

    private static final Logger logger = Logger.getLogger(DBMessageResourceImpl.class);

    private static final String ATTR_NAME = "Name";

    private static final String ATTR_MESSAGE = "Message";

    private static final String ATTR_SERVERID = "serverId";

    private static final String RESOURCE_PATH = "org/apache/ftpserver/message/";

    private String[] languages;

    private Map<String, PropertiesPair> messages;

    private DataSource dataSource;

    private final JDBCConnection connection = new JDBCConnection();

    private String selectAll;

    private static class PropertiesPair {

        public Properties defaultProperties = new Properties();

        public Properties customProperties = new Properties();
    }

    /**
   * Initializes and configures the message resource.
   * @throws FtpException
   */
    public void configure() throws FtpException {
        if (dataSource == null) {
            throw new FtpServerConfigurationException("Required data source not provided");
        }
        ServerManagerImpl.setDataSource(getDataSource());
        if (selectAll == null) {
            throw new FtpServerConfigurationException("Required select Message SQL statement not provided");
        }
        try {
            connection.createConnection();
            logger.info("Database connection opened.");
        } catch (final SQLException ex) {
            logger.error("DBMessageResourceImpl.configure()", ex);
            throw new FtpServerConfigurationException("DBMessageResourceImpl.configure()", ex);
        }
        messages = new HashMap<String, PropertiesPair>();
        if (this.languages != null) {
            for (int i = 0; i < this.languages.length; ++i) {
                final String lang = this.languages[i];
                final PropertiesPair pair = createPropertiesPair(lang);
                messages.put(lang, pair);
            }
        }
        final PropertiesPair pair = createPropertiesPair(null);
        messages.put(null, pair);
    }

    private PropertiesPair createPropertiesPair(final String lang) throws FtpException {
        final PropertiesPair pair = new PropertiesPair();
        String defaultResourceName;
        if (lang == null) {
            defaultResourceName = RESOURCE_PATH + "FtpStatus.properties";
        } else {
            defaultResourceName = RESOURCE_PATH + "FtpStatus_" + lang + ".properties";
        }
        InputStream in = null;
        try {
            in = getClass().getClassLoader().getResourceAsStream(defaultResourceName);
            if (in != null) {
                pair.defaultProperties.load(in);
            }
        } catch (final Exception ex) {
            logger.warn("DBMessageResourceImpl.createPropertiesPair()", ex);
            throw new FtpException("DBMessageResourceImpl.createPropertiesPair()", ex);
        } finally {
            IoUtils.close(in);
        }
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            final String selectQuery = selectAll.replaceFirst("\\{" + ATTR_SERVERID + "\\}", ServerManagerImpl.getServerManager().getServerId().toString());
            logger.info(selectQuery);
            rs = stmt.executeQuery(selectQuery);
            while (rs.next()) {
                final String name = rs.getString(ATTR_NAME);
                final String message = rs.getString(ATTR_MESSAGE);
                pair.customProperties.setProperty(name, message);
            }
        } catch (final SQLException ex) {
            logger.error("DBMessageResourceImpl.getPermission()", ex);
            throw new FtpException("DBMessageResourceImpl.getPermission()", ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (final Exception ex) {
                    logger.error("DBMessageResourceImpl.getPermission()", ex);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (final Exception ex) {
                    logger.error("DBMessageResourceImpl.getPermission()", ex);
                }
            }
        }
        return pair;
    }

    public String[] getAvailableLanguages() {
        if (languages == null) {
            return null;
        } else {
            return languages.clone();
        }
    }

    public String getMessage(final int code, final String subId, String language) {
        String key = String.valueOf(code);
        if (subId != null) {
            key = key + '.' + subId;
        }
        String value = null;
        PropertiesPair pair = null;
        if (language != null) {
            language = language.toLowerCase();
            pair = messages.get(language);
            if (pair != null) {
                value = pair.customProperties.getProperty(key);
                if (value == null) {
                    value = pair.defaultProperties.getProperty(key);
                }
            }
        }
        if (value == null) {
            pair = messages.get(null);
            if (pair != null) {
                value = pair.customProperties.getProperty(key);
                if (value == null) {
                    value = pair.defaultProperties.getProperty(key);
                }
            }
        }
        return value;
    }

    public Properties getMessages(String language) {
        final Properties messages = new Properties();
        PropertiesPair pair = this.messages.get(null);
        if (pair != null) {
            messages.putAll(pair.defaultProperties);
            messages.putAll(pair.customProperties);
        }
        if (language != null) {
            language = language.toLowerCase();
            pair = this.messages.get(language);
            if (pair != null) {
                messages.putAll(pair.defaultProperties);
                messages.putAll(pair.customProperties);
            }
        }
        return messages;
    }

    /**
   * Unsupported operation.
   */
    public void save(final Properties prop, final String language) throws FtpException {
        throw new FtpException("The save operation is not supported");
    }

    /**
   * Dispose component - frees allocated structures and closes the connection to the database.
   */
    public void dispose() {
        final Iterator<String> it = messages.keySet().iterator();
        while (it.hasNext()) {
            final String language = it.next();
            final PropertiesPair pair = messages.get(language);
            pair.customProperties.clear();
            pair.defaultProperties.clear();
        }
        messages.clear();
        connection.closeConnection();
    }

    /**
   * Return the data source used by this class.
   * @return the data source
   */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
   * Sets the data source used by this class.
   * @param dataSource the data source to set
   */
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
        connection.setDataSource(dataSource);
    }

    /**
   * Returns the SQL SELECT statement used to select all messages.
   * @return the SQL SELECT statement
   */
    public String getSqlSelectAll() {
        return selectAll;
    }

    /**
   * Sets the SQL SELECT statement used to select all messages.
   * @param selectAll the SQL SELECT statement to set
   */
    public void setSqlSelectAll(final String selectAll) {
        this.selectAll = selectAll;
    }

    /**
   * Sets the available languages.
   * @param langs the available languages to set, delimited by comma, semicolon, space or tab 
   */
    public void setLanguages(final String langs) {
        if (langs != null) {
            final StringTokenizer st = new StringTokenizer(langs, ",; \t");
            final int tokenCount = st.countTokens();
            this.languages = new String[tokenCount];
            for (int i = 0; i < tokenCount; ++i) {
                this.languages[i] = st.nextToken().toLowerCase();
            }
        }
    }
}
