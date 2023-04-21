package org.ourgrid.peer.controller.actions;

import java.io.Serializable;
import org.ourgrid.peer.PeerConstants;
import org.ourgrid.peer.controller.LocalWorkerProviderController;
import org.ourgrid.peer.dao.RequestDAO;
import org.ourgrid.peer.to.Request;
import br.edu.ufcg.lsd.commune.container.servicemanager.ServiceManager;
import br.edu.ufcg.lsd.commune.container.servicemanager.actions.RepeatedAction;

/**
 * Action that request workers for a determined consumer on the <code>LocalWorkerProvider</code>
 * It is stored on the <code>Component</codes> on the peer initialization.
 *
 */
public class RequestWorkersAction implements RepeatedAction {

    public void run(Serializable requestID, ServiceManager serviceManager) {
        Request request = serviceManager.getDAO(RequestDAO.class).getRequest((Long) requestID);
        LocalWorkerProviderController lwpc = (LocalWorkerProviderController) serviceManager.getObjectDeployment(PeerConstants.LOCAL_ACCESS_OBJECT_NAME).getObject();
        lwpc.requestWorkers(request.getSpec(), serviceManager.getMyPublicKey());
    }
}
