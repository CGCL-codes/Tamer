package uk.ac.ed.ph.qtitools.xmlutils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link ResourceLocator} that loads HTTP
 * resources directly over the network.
 * 
 * @author David McKain
 * @version $Revision: 2801 $
 */
public final class NetworkHttpResourceLocator implements ResourceLocator {

    private static final long serialVersionUID = 6595159080004817912L;

    private static final Logger logger = LoggerFactory.getLogger(NetworkHttpResourceLocator.class);

    public InputStream findResource(final URI systemId) {
        final String scheme = systemId.getScheme();
        if ("http".equals(scheme)) {
            return loadHttpResource(systemId);
        }
        return null;
    }

    private InputStream loadHttpResource(final URI systemId) {
        InputStream resourceStream;
        try {
            resourceStream = systemId.toURL().openConnection().getInputStream();
            logger.debug("Successful connection to HTTP resource with URI {}", systemId);
        } catch (IOException e) {
            resourceStream = null;
            logger.warn("Failed to open connection to HTTP resource with URI {}", systemId);
        }
        return resourceStream;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + hashCode();
    }
}
