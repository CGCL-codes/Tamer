package org.dbe.composer.wfengine.bpel.server.engine.storage.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.SdlException;
import org.dbe.composer.wfengine.bpel.config.ISdlEngineConfiguration;
import org.dbe.composer.wfengine.bpel.server.engine.storage.SdlStorageException;

/**
 * Implements a JNDI version of an SdlDataSource.  This implementation uses JNDI
 * to look up the configured data source with the given JNDI name.
 */
public class SdlJNDIDataSource extends SdlDataSource {

    private static final Logger logger = Logger.getLogger(SdlJNDIDataSource.class.getName());

    /** The JNDI name for this data source. */
    private String mJNDIName;

    /** The data source username. */
    private String mUsername;

    /** The data source password; */
    private String mPassword;

    /**
     * Constructs a JNDI data source.  Uses information in the engine configuration map
     * to initialize its state (JNDI name, username, password).
     *
     * @param aConfig The engine configuration map for this data source.
     * @param aSQLConfig The SQL configuration.
     */
    public SdlJNDIDataSource(Map aConfig, SdlSQLConfig aSQLConfig) throws SdlException {
        super("JNDI", aSQLConfig);
        try {
            String jndiName = (String) aConfig.get(ISdlEngineConfiguration.DS_JNDI_NAME_ENTRY);
            String username = (String) aConfig.get(ISdlEngineConfiguration.DS_USERNAME_ENTRY);
            String password = (String) aConfig.get(ISdlEngineConfiguration.DS_PASSWORD_ENTRY);
            setJNDIName(jndiName);
            setUsername(username);
            setPassword(password);
        } catch (Exception e) {
            logger.error("Error creating JNDI data source from the engine configuration: " + e);
            throw new SdlException("Error creating JNDI data source from the engine configuration.", e);
        }
    }

    public DataSource createDelegate() throws SdlStorageException {
        DataSource ds = lookupDataSource(mJNDIName);
        if (ds == null) {
            logger.error("Error looking up JNDI DataSource (not found).");
            throw new SdlStorageException("Error looking up JNDI DataSource (not found).");
        }
        return ds;
    }

    /**
     * Looks up a DataSource using JNDI.
     *
     * @param aJNDIPath The JNDI path of the DataSource.
     * @return A DataSource or null if not found.
     */
    protected DataSource lookupDataSource(String aJNDIPath) {
        try {
            Context initialContext = new InitialContext();
            return (DataSource) initialContext.lookup(aJNDIPath);
        } catch (NamingException e) {
            return null;
        }
    }

    public Connection getConnection() throws SQLException {
        if (getUsername() != null) {
            return getConnection(getUsername(), getPassword());
        } else {
            return super.getConnection();
        }
    }

    /** Sets the JNDI name for this data source. */
    protected void setJNDIName(String jNDIName) {
        mJNDIName = jNDIName;
    }

    /** Returns the JNDI name for this data source. */
    protected String getJNDIName() {
        return mJNDIName;
    }

    /** Sets the username for this data source. */
    protected void setUsername(String username) {
        mUsername = username;
    }

    /** Returns the username for this data source. */
    protected String getUsername() {
        return mUsername;
    }

    /** Sets the password for this data source. */
    protected void setPassword(String password) {
        mPassword = password;
    }

    /** Returns the password for this data source. */
    protected String getPassword() {
        return mPassword;
    }
}
