package org.ourgrid.acceptance.util.peer;

import org.easymock.classextension.EasyMock;
import org.ourgrid.acceptance.util.PeerAcceptanceUtil;
import org.ourgrid.common.interfaces.RemoteWorkerProvider;
import org.ourgrid.common.interfaces.management.RemoteWorkerManagement;
import org.ourgrid.peer.PeerComponent;
import br.edu.ufcg.lsd.commune.container.ContainerContext;
import br.edu.ufcg.lsd.commune.container.logging.CommuneLogger;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;
import br.edu.ufcg.lsd.commune.test.AcceptanceTestUtil;

public class Req_114_Util extends PeerAcceptanceUtil {

    public Req_114_Util(ContainerContext context) {
        super(context);
    }

    /**
	 * Notifies a remote worker failure and expects the peer to log it.
	 * @param component The peer component
	 * @param rwmOID The ObjectID of the <code>RemoteWorkerManagement</code> interface of this worker.
	 * @param rwp The RemoteWorkerProvider of this worker
	 */
    public void notifyRemoteWorkerFailure(PeerComponent component, DeploymentID rwmOID, RemoteWorkerProvider rwp, DeploymentID rwpID) {
        CommuneLogger oldLogger = component.getLogger();
        CommuneLogger newLogger = EasyMock.createMock(CommuneLogger.class);
        component.setLogger(newLogger);
        newLogger.debug("The remote Worker [" + rwmOID + "] has failed. Disposing this Worker.");
        EasyMock.replay(newLogger);
        EasyMock.reset(rwp);
        RemoteWorkerManagement remoteWorker = (RemoteWorkerManagement) AcceptanceTestUtil.getBoundObject(rwmOID);
        rwp.disposeWorker(rwmOID.getServiceID());
        EasyMock.replay(rwp);
        getRemoteWorkerMonitor().doNotifyFailure(remoteWorker, rwmOID);
        EasyMock.verify(rwp);
        EasyMock.verify(newLogger);
        component.setLogger(oldLogger);
    }
}
