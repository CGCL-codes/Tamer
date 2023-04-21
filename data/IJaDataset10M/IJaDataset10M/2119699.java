package de.fzi.harmonia.commons.basematchers.linguisticbasematcher;

import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.model.OWLEntity;
import de.fzi.harmonia.commons.ConfigurationException;
import de.fzi.harmonia.commons.InfeasibleEvaluatorException;
import de.fzi.harmonia.commons.basematchers.AbstractBaseMatcher;
import de.fzi.harmonia.commons.basematchers.helper.WordNetConnector;
import de.fzi.kadmos.api.Alignment;
import de.fzi.kadmos.api.Correspondence;
import fr.inrialpes.exmo.ontosim.string.JWNLDistances;

/**
 * Parent class for all linguistic base matchers, which deal with WordNet
 * in order to compute distances.
 * 
 * @author Juergen Bock (bock@fzi.de)
 *
 */
public abstract class LinguisticDistanceMatcher extends AbstractBaseMatcher {

    private Log logger = LogFactory.getLog(LinguisticDistanceMatcher.class);

    private static final String WN_DIR_PARAM_SUFFIX = "wndir";

    private static final String WN_VERSION_PARAM_SUFFIX = "wnvers";

    private static final String WN_DIR_ENVIRONMENT_VARIABLE = "WNSEARCHDIR";

    private static final String WN_VERSION_ENVIRONMENT_VARIABLE = "WNVERS";

    private JWNLDistances wnsim;

    private boolean wnInitialised;

    protected String wndict = "dict/";

    protected String wnvers = "3.0";

    /**
	 * Parent constructor for all linguistic base matchers. It obtains a
	 * reference to the global WordNet distances object.
	 * @throws ConfigurationException 
	 */
    public LinguisticDistanceMatcher(Properties params, String id, Alignment align) throws ConfigurationException {
        super(params, id, align);
        wnInitialised = false;
        readParameters();
    }

    @Override
    protected void checkForCorrectEntityType(Correspondence<? extends OWLEntity> corr) throws InfeasibleEvaluatorException {
    }

    private void readParameters() throws ConfigurationException {
        try {
            wndict = getParameterValue(WN_DIR_PARAM_SUFFIX);
        } catch (ConfigurationException e) {
            if (System.getenv(WN_DIR_ENVIRONMENT_VARIABLE) != null) wndict = System.getenv(WN_DIR_ENVIRONMENT_VARIABLE); else {
                final String errMsg = e.getMessage() + " Environment variable " + WN_DIR_ENVIRONMENT_VARIABLE + " not set either.";
                logger.error(errMsg);
                throw new ConfigurationException(errMsg);
            }
        }
        if (logger.isDebugEnabled()) logger.debug("Parameter wndict is set to " + wndict);
        try {
            wnvers = getParameterValue(WN_VERSION_PARAM_SUFFIX);
        } catch (ConfigurationException e) {
            if (System.getenv(WN_VERSION_ENVIRONMENT_VARIABLE) != null) wnvers = System.getenv(WN_VERSION_ENVIRONMENT_VARIABLE); else {
                final String errMsg = e.getMessage() + " Environment variable " + WN_VERSION_ENVIRONMENT_VARIABLE + " not set either.";
                logger.error(errMsg);
                throw new ConfigurationException(errMsg);
            }
        }
        if (logger.isDebugEnabled()) logger.debug("Parameter wnvers is set to " + wnvers);
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
