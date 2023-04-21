package org.dbe.composer.wfengine.bpel.server.deploy.validate;

import org.activebpel.rt.bpel.server.AeMessages;
import org.activebpel.rt.bpel.server.logging.IAeDeploymentLogger;
import org.dbe.composer.wfengine.bpel.server.deploy.ISdlDeploymentContainer;

/**
 * This class handles some very basic file contents validation for
 * WSR and BPR deployments.
 */
public class SdlDeploymentFileValidator {

    private static final String INVALID_BPR = AeMessages.getString("SdlDeploymentFileValidator.0");

    private static final String INVALID_WSR = AeMessages.getString("SdlDeploymentFileValidator.1");

    /**
     * Preliminary validation of the deployment file.  Ensure that any BPR deployments DO NOT
     * contain any .wsdd files and that WSR deployments contain a .wsdd file.
     * @param aContainer
     * @param aBprFlag
     * @param aLogger
     */
    public static void validateFileType(ISdlDeploymentContainer aContainer, boolean aBprFlag, IAeDeploymentLogger aLogger) {
        if (aBprFlag) {
            if (aContainer.isWsddDeployment()) {
                aLogger.addError(INVALID_BPR, null, null);
            }
        } else if (!aContainer.isWsddDeployment()) {
            aLogger.addError(INVALID_WSR, null, null);
        }
    }
}
