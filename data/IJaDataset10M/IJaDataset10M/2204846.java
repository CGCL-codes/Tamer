package net.laubenberger.bogatyr.model.updater;

import java.net.URL;
import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import net.laubenberger.bogatyr.model.crypto.HashCodeAlgo;
import net.laubenberger.bogatyr.model.misc.Document;
import net.laubenberger.bogatyr.model.misc.Platform;
import net.laubenberger.bogatyr.model.updater.ModelUpdaterImpl.XmlAdapterModelUpdater;

/**
 * The interface for the updater model.
 *
 * @author Stefan Laubenberger
 * @version 0.9.6 (20110527)
 * @since 0.9.0
 */
@XmlJavaTypeAdapter(XmlAdapterModelUpdater.class)
public interface ModelUpdater extends Document {

    String MEMBER_LOCATIONS = "locations";

    String MEMBER_HASHS = "hashs";

    /**
	 * Returns all locations.
	 *
	 * @return {@link Map} containing all update locations
	 * @see URL
	 * @see Platform
	 * @since 0.9.0
	 */
    Map<Platform, URL> getLocations();

    /**
	 * Sets all update {@link URL} locations.
	 *
	 * @param locations {@link Map} containing all update {@link URL} locations
	 * @see URL
	 * @since 0.9.0
	 */
    void setLocations(Map<Platform, URL> locations);

    /**
	 * Returns the {@link URL} location for a given {@link Platform}.
	 *
	 * @param platform for the location
	 * @return location
	 * @see URL
	 * @see Platform
	 * @since 0.9.0
	 */
    URL getLocation(Platform platform);

    /**
	 * Returns the default update {@link URL} location (ANY platform).
	 *
	 * @return default update location
	 * @see URL
	 * @since 0.9.0
	 */
    URL getLocation();

    /**
	 * Returns all hashs.
	 *
	 * @return {@link Map} containing all hashs
	 * @see HashCodeAlgo
	 * @since 0.9.0
	 */
    Map<HashCodeAlgo, String> getHashs();

    /**
	 * Sets all hashs.
	 *
	 * @param hashs {@link Map} containing all hashs
	 * @see HashCodeAlgo
	 * @since 0.9.0
	 */
    void setHashs(Map<HashCodeAlgo, String> hashs);

    /**
	 * Returns the hash for a given {@link HashCodeAlgo}.
	 *
	 * @param hashCodeAlgo for the hash
	 * @return hash
	 * @see HashCodeAlgo
	 * @since 0.9.0
	 */
    String getHash(HashCodeAlgo hashCodeAlgo);

    /**
	 * Returns the default hash (generated with SHA256).
	 *
	 * @return default hash
	 * @since 0.9.0
	 */
    String getHash();
}
