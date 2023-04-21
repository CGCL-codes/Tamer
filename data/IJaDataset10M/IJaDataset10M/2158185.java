package org.settings4j.connector;

/**
 * The SystemProperties implementation of an {@link org.settings4j.Connector}.
 * <p>
 * 
 * see also {@link System#getProperty(String, String)}.
 * 
 * @author Harald.Brabenetz
 *
 */
public class SystemPropertyConnector extends AbstractPropertyConnector {

    /** General Logger for this Class. */
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(SystemPropertyConnector.class);

    /**
     * Very similar to <code>System.getProperty</code> except that the {@link SecurityException} is hidden.
     * 
     * @param key The key to search for.
     * @param def The default value to return.
     * @return the string value of the system property, or the default value if there is no property with that key.
     */
    protected String getProperty(final String key, final String def) {
        try {
            return System.getProperty(key, def);
        } catch (final Throwable e) {
            LOG.debug("Was not allowed to read system property \"" + key + "\".");
            return def;
        }
    }
}
