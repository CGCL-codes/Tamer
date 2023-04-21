package net.sourceforge.acelogger.configuration;

import java.io.InputStream;
import java.util.Collection;

/**
 * Loads the logging system configuration from an external source.
 * 
 * @author Zardi (https://sourceforge.net/users/daniel_zardi)
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ConfigurationLoader {

    /**
	 * Configures the logging system from the data contained in the source stream.
	 * 
	 * @param source
	 *            The source for the configuration data. Depending on the implementation this might
	 *            be a file, url, etc.
	 * @since 1.0.0
	 */
    public void configureFrom(InputStream source);

    /**
	 * Configures the logging system from multiple sources. If two sources define configurations for
	 * the same identifier, the later will be used (ie. the configuration will be overwritten). This
	 * behavior makes possible configuration inheritance and refinement.
	 * 
	 * @param sources
	 *            An array containing the various sources to be used during configuration.
	 * @see #configureFrom(InputStream)
	 */
    public void configureFromMultipleSources(InputStream... sources);

    /**
	 * Configures the logging system from multiple sources. If two sources define configurations for
	 * the same identifier, the later will be used (ie. the configuration will be overwritten) based
	 * on the order returned by the collection's iterator. This
	 * behavior makes possible configuration inheritance and refinement.
	 * 
	 * @param sources
	 *            A collection containing the various sources to be used during configuration.
	 * @see #configureFrom(InputStream)
	 * @see Collection#iterator()
	 */
    public void configureFromMultipleSources(Collection<InputStream> sources);
}
