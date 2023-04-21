package org.dbe.composer.wfengine.bpel.webserver.web;

import java.io.File;
import org.apache.log4j.Logger;
import org.dbe.composer.wfengine.SdlException;
import org.dbe.composer.wfengine.bpel.server.engine.SdlEngineAdministration;
import org.dbe.composer.wfengine.bpel.server.engine.SdlEngineFactory;
import org.dbe.composer.wfengine.bpel.server.logging.ISdlDeploymentLogger;
import org.dbe.composer.wfengine.bpel.server.logging.SdlTeeDeploymentLogger;

/**
 * This is an implementation of the BPEL engine administration interface.  It extends
 * the base implementation in order to override the <code>deployNewBpr</code> method.  This
 * class provides an implementation of that method in order to allow web service deployments
 * to work in BPEL.
 */
public class WebEngineAdministration extends SdlEngineAdministration {

    /** for deployment logging purposes */
    protected static final Logger logger = Logger.getLogger(WebEngineAdministration.class.getName());

    /**
     * Overrides the base engine admin impl in order to provide an implementation of the
     * <code>deployNewBpr</code> method.
     */
    public void deployNewBpr(File aBprFile, String aBprFilename, ISdlDeploymentLogger aLogger) throws SdlException {
        logger.info("deployNewBpr() bpr=" + aBprFilename);
        ISdlDeploymentLogger logger = SdlEngineFactory.getDeploymentLoggerFactory().createLogger();
        ISdlDeploymentLogger teeLogger = new SdlTeeDeploymentLogger(logger, aLogger);
        SdlProcessEngineServlet.getDeploymentHandler().handleDeployment(aBprFile, aBprFilename, teeLogger);
    }
}
