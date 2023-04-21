package org.exolab.jms.tools.migration.proxy;

import org.exolab.jms.persistence.PersistenceException;
import org.exolab.jms.tools.migration.proxy.PropertyStore;

/**
 * Migration version information.
 *
 * @author <a href="mailto:tma@netspace.net.au">Tim Anderson</a>
 * @version $Revision: 1.1 $ $Date: 2005/10/20 14:07:03 $
 */
public class VersionInfo {

    /**
     * The property store.
     */
    private final PropertyStore _properties;

    /**
     * OpenJMS version property.
     */
    private final String OPENJMS_VERSION = "openjmsVersion";

    /**
     * Proxy database schema version property.
     */
    private final String PROXY_SCHEMA_VERSION = "proxySchemaVersion";

    /**
     * Proxy database creation timestamp.
     */
    private final String CREATION_TIMESTAMP = "creationTimestamp";

    /**
     * Construct a new <code>VersionInfo</code>.
     *
     * @param properties the property store to access
     */
    public VersionInfo(PropertyStore properties) {
        _properties = properties;
    }

    /**
     * Sets the OpenJMS version that the proxy database was created with.
     *
     * @param version the version
     * @throws PersistenceException for any persistence error
     */
    public void setOpenJMSVersion(String version) throws PersistenceException {
        _properties.add(OPENJMS_VERSION, version);
    }

    /**
     * Returns the OpenJMS version that the proxy database was created with.
     *
     * @return the OpenJMS version
     * @throws PersistenceException for any persistence error
     */
    public String getOpenJMSVersion() throws PersistenceException {
        return _properties.get(OPENJMS_VERSION);
    }

    /**
     * Sets the proxy schema version.
     *
     * @param version the version
     * @throws PersistenceException for any persistence error
     */
    public void setProxySchemaVersion(String version) throws PersistenceException {
        _properties.add(PROXY_SCHEMA_VERSION, version);
    }

    /**
     * Returns the proxy schema version.
     *
     * @return the proxy schema version.
     * @throws PersistenceException for any persistence error
     */
    public String getProxySchemaVersion() throws PersistenceException {
        return _properties.get(PROXY_SCHEMA_VERSION);
    }

    /**
     * Sets the timestamp when the proxy database was created.
     *
     * @param timestamp the timestamp, in milliseconds
     * @throws PersistenceException for any persistence error
     */
    public void setCreationTimestamp(long timestamp) throws PersistenceException {
        _properties.add(CREATION_TIMESTAMP, Long.toString(timestamp));
    }

    /**
     * Returns the timestamp when the proxy database was created.
     *
     * @return the proxy database creation timestamp
     * @throws PersistenceException for any persistence error
     */
    public long getCreationTimestamp() throws PersistenceException {
        String value = _properties.get(CREATION_TIMESTAMP);
        return Long.valueOf(value).longValue();
    }
}
