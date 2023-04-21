package org.qtitools.mathassess.tools.glue.extras.pooling;

import org.qtitools.mathassess.tools.qticasbridge.maxima.QTIMaximaSession;
import org.qtitools.mathassess.tools.utilities.ConstraintUtilities;
import uk.ac.ed.ph.jacomax.MaximaConfiguration;
import uk.ac.ed.ph.jacomax.MaximaInteractiveProcess;
import uk.ac.ed.ph.jacomax.MaximaProcessLauncher;
import uk.ac.ed.ph.snuggletex.utilities.StylesheetCache;
import org.apache.commons.pool.PoolableObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link PoolableObjectFactory} catering {@link QTIMaximaSession}s.
 *
 * @author  David McKain
 * @version $Revision: 2428 $
 */
class PoolableQTIMaximaSessionFactory implements PoolableObjectFactory {

    private static final Logger logger = LoggerFactory.getLogger(PoolableQTIMaximaSessionFactory.class);

    private StylesheetCache stylesheetCache;

    private MaximaConfiguration maximaConfiguration;

    private MaximaProcessLauncher maximaProcessLauncher;

    public StylesheetCache getStylesheetCache() {
        return stylesheetCache;
    }

    public void setStylesheetCache(StylesheetCache stylesheetCache) {
        this.stylesheetCache = stylesheetCache;
    }

    public MaximaConfiguration getMaximaConfiguration() {
        return maximaConfiguration;
    }

    public void setMaximaConfiguration(MaximaConfiguration maximaConfiguration) {
        this.maximaConfiguration = maximaConfiguration;
    }

    public void init() {
        ConstraintUtilities.ensureNotNull(maximaConfiguration, "maximaConfiguration");
        ConstraintUtilities.ensureNotNull(stylesheetCache, "stylesheetCache");
        maximaProcessLauncher = new MaximaProcessLauncher(maximaConfiguration);
    }

    public Object makeObject() {
        logger.debug("Creating new pooled Maxima process");
        MaximaInteractiveProcess maximaInteractiveProcess = maximaProcessLauncher.launchInteractiveProcess();
        QTIMaximaSession session = new QTIMaximaSession(maximaInteractiveProcess, stylesheetCache);
        session.init();
        return session;
    }

    public void activateObject(Object obj) {
    }

    public void passivateObject(Object obj) {
        logger.debug("Resetting Maxima session and passivating");
        QTIMaximaSession session = (QTIMaximaSession) obj;
        if (session.isTerminated()) {
            throw new IllegalStateException("Expected pool to verify Objects before passivation");
        }
        try {
            session.reset();
        } catch (Exception e) {
            logger.warn("Could not reset process - terminating so that it is no longer considered valid");
            session.terminate();
        }
    }

    public boolean validateObject(Object obj) {
        QTIMaximaSession session = (QTIMaximaSession) obj;
        return !session.isTerminated();
    }

    public void destroyObject(Object obj) {
        logger.debug("Terminating pooled Maxima session");
        QTIMaximaSession session = (QTIMaximaSession) obj;
        session.terminate();
    }
}
