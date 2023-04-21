package de.sonivis.tool.mediawikiconnector.textmining;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.sonivis.tool.core.CoreTooling;
import de.sonivis.tool.core.eventhandling.INetworkFilter;

/**
 * This class initializes the keywords by creating a term document matrix in R.
 * 
 * @author Janette
 * @version $Revision
 */
public final class KeywordMatrixLoading {

    /**
	 * Logger at {@value}.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(KeywordMatrixLoading.class.getName());

    /**
	 * Instance of this class.
	 */
    private static final KeywordMatrixLoading INSTANCE = new KeywordMatrixLoading();

    /**
	 * Default Constructor.
	 */
    public KeywordMatrixLoading() {
    }

    /**
	 * Returns an instance of this class.
	 * 
	 * @return KeywordMatrixLoading instance
	 */
    public static synchronized KeywordMatrixLoading getInstance() {
        return INSTANCE;
    }

    /**
	 * Initialize the keywords by connection to the database and creating and
	 * term document matrix.
	 * 
	 * @param filterNetwork
	 *            defines which revisions should be used
	 * @return true, if data initializing was successful; false otherwise
	 */
    public synchronized boolean loadKeywordMatrix(final INetworkFilter filterNetwork) {
        final boolean dataLoaded = false;
        final long startTime = System.nanoTime();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Keywords matrix loaded in " + String.valueOf((System.nanoTime() - startTime) * CoreTooling.NANO_TIME_FACTOR) + CoreTooling.NANO_TIME_IDENTIFIER);
        }
        return dataLoaded;
    }
}
