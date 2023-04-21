package org.sf.bluprints;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This class is the main engine. Given a URI to a Bluprints XML file
 * in the classpath, this will parse the file as well as determine
 * when to reload the file based on the configuration in the XML
 * file.
 * </p>
 *
 * <p>
 * The default URI to the classpath entry for the Bluprints XML file
 * is always:
 * </p>
 *
 * <pre>bluprints.xml</pre>
 *
 * @author  Brian Pontarelli
 */
public class Engine {

    private ConfigurationParser parser;

    private Map<String, BluprintsNamespace> bluprints = new HashMap<String, BluprintsNamespace>();

    private String[] uris;

    private long lastLoad;

    private long reloadSeconds;

    /**
     * Constructs a new engine. The default URI is always '/bluprints.xml'.
     */
    public Engine(final long reloadSeconds, final String... uris) {
        this.uris = uris;
        this.reloadSeconds = reloadSeconds;
        this.parser = new ConfigurationParser();
        this.bluprints = parser.build(uris);
        this.lastLoad = System.currentTimeMillis();
    }

    /**
     * Determines how often the configuration file is reloaded. If this is set to -1, it is never
     * reloaded. This value changes after the configuration is loaded if the configuration file contains
     * a value for the <b>reloadSeconds</b> attribute on the root element.
     *
     * <p>Defaults to 1 second</p>
     */
    public long getReloadSeconds() {
        return reloadSeconds;
    }

    /**
     * Determines how often the configuration file is reloaded. If this is set to -1, it is never
     * reloaded. This value changes after the configuration is loaded if the configuration file contains
     * a value for the <b>reloadSeconds</b> attribute on the root element.
     *
     * <p>Defaults to 1 second</p>
     */
    public void setReloadSeconds(long reloadSeconds) {
        this.reloadSeconds = reloadSeconds;
    }

    /**
     * Returns the Bluprint for the given name, or null. This loads and possibly reloads the Bluprint
     * configuration file from the classpath.
     */
    public Bluprint getPage(String name) {
        if (System.currentTimeMillis() > lastLoad + (reloadSeconds * 1000)) {
            bluprints = parser.build(uris);
        }
        String[] parts = Bluprints.split(name);
        BluprintsNamespace bn = this.bluprints.get(parts[0]);
        if (bn == null) {
            throw new IllegalArgumentException("Invalid bluprint namespace [" + parts[0] + "]. " + "Remember that undefined bluprints have the namespace [" + Bluprints.DEFAULT_NAMESPACE + "].");
        }
        return bn.getPage(parts[1]);
    }
}
