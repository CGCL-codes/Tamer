package de.fzi.mappso.basematcher;

import java.util.Properties;
import org.apache.log4j.Logger;
import de.fzi.mappso.basematcher.helper.WordNetConnector;
import fr.inrialpes.exmo.ontosim.string.JWNLDistances;

/**
 * Parent class for all linguistic base matchers, which deal with WordNet
 * in order to compute distances.
 * 
 * @author bock
 *
 */
public abstract class LinguisticDistanceMatcher extends AbstractBaseMatcher {

    private JWNLDistances wnsim;

    private Logger logger;

    private boolean wnInitialised;

    protected String wndict = "dict/";

    protected String wnvers = "3.0";

    /**
	 * Parent constructor for all linguistic base matchers. It obtains a
	 * reference to the global WordNet distances object.
	 */
    LinguisticDistanceMatcher() {
        logger = Logger.getLogger(LinguisticDistanceMatcher.class.getName());
        wnInitialised = false;
    }

    @Override
    public void setParameters(Properties params) {
        super.setParameters(params);
        if (params.getProperty("wndict") != null) {
            wndict = params.getProperty("wndict");
        } else if (System.getenv("WNSEARCHDIR") != null) {
            wndict = System.getenv("WNSEARCHDIR");
        } else {
            logger.warn("Parameter wndict not given and environment variable WNSEARCHDIR not set. Using default " + wndict + ".");
        }
        logger.debug("Parameter wndict is set to " + wndict);
        if (params.getProperty("wnvers") != null) {
            wnvers = params.getProperty("wnvers");
            logger.debug("Parameter wnvers is set to " + wnvers);
        } else if (System.getenv("WNVERS") != null) {
            wnvers = System.getenv("WNVERS");
        } else {
            logger.warn("Parameter wnvers is not given. Using default " + wnvers + ".");
        }
        logger.debug("Parameter wnvers is set to " + wnvers);
        initWordNet();
    }

    /**
	 * Computes the WordNet distance between two words by calling the global
	 * WordNet distances object. If WordNet is not yet initialised at this stage,
	 * which only occurs if <code>computeDistance</code> is invoked before
	 * <code>setParameters</code>, the initialisation is triggered (using defaults). 
	 * 
	 * @param s1 First word.
	 * @param s2 Second word.
	 * @return WordNet distance between the first and second word. 
	 */
    protected double computeWNDistance(String s1, String s2) {
        if (!wnInitialised) initWordNet();
        if (wnsim != null) return 1. - wnsim.computeSimilarity(s1, s2); else return 1.;
    }

    /**
	 * Initialises WordNet if not initialised already.
	 */
    private void initWordNet() {
        if (!wnInitialised) {
            WordNetConnector.initialize(wndict, wnvers);
            wnsim = WordNetConnector.getWNReference();
            wnInitialised = true;
        }
    }
}
