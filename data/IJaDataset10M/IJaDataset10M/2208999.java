package org.ourgrid.peer.controller.messages;

import org.ourgrid.peer.PeerConfiguration;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;

public class VOMSMessages {

    /**
	 * @param workerProviderClientID
	 * @param requestId
	 * @param errorMessage
	 * @return
	 */
    public static String getErrorOnConnectingToVOMSMessage(DeploymentID workerProviderClientID, long requestId, String errorMessage) {
        return "Request " + requestId + ": request from [" + workerProviderClientID + "] ignored because " + "there was an error while connecting to VOMS. Error cause: " + errorMessage;
    }

    /**
	 * @param workerProviderID
	 * @param errorMessage
	 * @return
	 */
    public static String getErrorOnConnectingToVOMSMessage(DeploymentID workerProviderID, String errorMessage) {
        return "Disposing worker provider [" + workerProviderID + "] because there was an error while connecting to VOMS. " + "Error cause: " + errorMessage;
    }

    /**
	 * @param workerProviderClientID
	 * @param requestId
	 * @return
	 */
    public static String getNonAuthorisedConsumerMessage(DeploymentID workerProviderClientID, long requestId) {
        return "Request " + requestId + ": request ignored because [" + workerProviderClientID + "] " + " is not authorized at VOMS.";
    }

    /**
	 * @param deploymentID
	 * @return
	 */
    public static String getNonAuthorisedProviderMessage(DeploymentID deploymentID) {
        return "Disposing worker provider [" + deploymentID + "] because it " + "is not authorized at VOMS.";
    }

    /**
	 * @return
	 */
    public static String getNullVOMSUrlMessage() {
        return "Property " + PeerConfiguration.PROP_VOMS_URL + " should not be null.";
    }
}
