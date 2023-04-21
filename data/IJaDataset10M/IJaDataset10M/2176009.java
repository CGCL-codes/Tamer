package org.ourgrid.cmmstatusprovider.controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.ourgrid.cmmstatusprovider.CommunityStatusProviderCallback;
import org.ourgrid.cmmstatusprovider.CommunityStatusProviderConstants;
import org.ourgrid.cmmstatusprovider.dao.CommunityStatusProviderDAO;
import org.ourgrid.common.interfaces.status.DiscoveryServiceStatusProvider;
import org.ourgrid.common.interfaces.status.DiscoveryServiceStatusProviderClient;
import org.ourgrid.common.interfaces.status.PeerStatusProvider;
import org.ourgrid.common.interfaces.status.PeerStatusProviderClient;
import org.ourgrid.discoveryservice.DiscoveryServiceConstants;
import org.ourgrid.discoveryservice.status.DiscoveryServiceCompleteStatus;
import org.ourgrid.peer.PeerConstants;
import org.ourgrid.peer.status.PeerCompleteStatus;
import br.edu.ufcg.lsd.commune.Application;
import br.edu.ufcg.lsd.commune.container.control.ApplicationControlClient;
import br.edu.ufcg.lsd.commune.container.control.ApplicationServerController;
import br.edu.ufcg.lsd.commune.container.control.ApplicationServerManager;
import br.edu.ufcg.lsd.commune.container.control.ControlOperationResult;
import br.edu.ufcg.lsd.commune.container.servicemanager.client.sync.SyncContainerUtil;
import br.edu.ufcg.lsd.commune.identification.ServiceID;

/**
 * @author Windows XP
 *
 */
public class CommunityStatusProviderAppController extends ApplicationServerController implements ApplicationServerManager, ApplicationControlClient {

    private static final int POLLING_TIMEOUT = 60;

    private final BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<Object>(1);

    @Override
    protected void createServices() {
        getServiceManager().deploy(CommunityStatusProviderConstants.ASYNC_CMM_CLIENT, new AsyncCommunityStatusProviderClientController());
        getServiceManager().deploy(CommunityStatusProviderConstants.SYNC_CMM_CLIENT, new SyncCommunityStatusProviderClientController(blockingQueue));
    }

    @Override
    protected void createDAOs() {
        getServiceManager().createDAO(CommunityStatusProviderDAO.class);
    }

    public void getPeerCompleteStatus(CommunityStatusProviderCallback callback, String peerAddress) {
        String[] splitAddress = peerAddress.split("@");
        ServiceID serviceID = new ServiceID(splitAddress[0], splitAddress[1], PeerConstants.MODULE_NAME, Application.CONTROL_OBJECT_NAME);
        getServiceManager().getDAO(CommunityStatusProviderDAO.class).addPeerCallback(serviceID, callback);
        getServiceManager().registerInterest(CommunityStatusProviderConstants.ASYNC_CMM_CLIENT, serviceID.toString(), PeerStatusProvider.class);
    }

    /**
	 * @param callback
	 * @param dsAddress
	 */
    public void getDSCompleteStatus(CommunityStatusProviderCallback callback, String dsAddress) {
        String[] splitAddress = dsAddress.split("@");
        ServiceID serviceID = new ServiceID(splitAddress[0], splitAddress[1], DiscoveryServiceConstants.MODULE_NAME, Application.CONTROL_OBJECT_NAME);
        getServiceManager().getDAO(CommunityStatusProviderDAO.class).addDsCallback(serviceID, callback);
        getServiceManager().registerInterest(CommunityStatusProviderConstants.ASYNC_CMM_CLIENT, serviceID.toString(), DiscoveryServiceStatusProvider.class);
    }

    /**
	 * @param peerAddress
	 * @return
	 */
    public PeerCompleteStatus getPeerCompleteStatus(String peerAddress) {
        String[] splitAddress = peerAddress.split("@");
        ServiceID serviceID = new ServiceID(splitAddress[0], splitAddress[1], PeerConstants.MODULE_NAME, Application.CONTROL_OBJECT_NAME);
        getServiceManager().registerInterest(CommunityStatusProviderConstants.SYNC_CMM_CLIENT, serviceID.toString(), PeerStatusProvider.class);
        PeerStatusProvider peerStatusProvider = SyncContainerUtil.waitForResponseObject(blockingQueue, PeerStatusProvider.class, POLLING_TIMEOUT);
        peerStatusProvider.getCompleteStatus((PeerStatusProviderClient) getServiceManager().getObjectDeployment(CommunityStatusProviderConstants.SYNC_CMM_CLIENT).getObject());
        return SyncContainerUtil.waitForResponseObject(blockingQueue, PeerCompleteStatus.class, POLLING_TIMEOUT);
    }

    /**
	 * @param dsAddress
	 * @return
	 */
    public DiscoveryServiceCompleteStatus getDSCompleteStatus(String dsAddress) {
        String[] splitAddress = dsAddress.split("@");
        ServiceID serviceID = new ServiceID(splitAddress[0], splitAddress[1], DiscoveryServiceConstants.MODULE_NAME, Application.CONTROL_OBJECT_NAME);
        getServiceManager().registerInterest(CommunityStatusProviderConstants.SYNC_CMM_CLIENT, serviceID.toString(), DiscoveryServiceStatusProvider.class);
        DiscoveryServiceStatusProvider dsStatusProvider = SyncContainerUtil.waitForResponseObject(blockingQueue, DiscoveryServiceStatusProvider.class, POLLING_TIMEOUT);
        dsStatusProvider.getCompleteStatus((DiscoveryServiceStatusProviderClient) getServiceManager().getObjectDeployment(CommunityStatusProviderConstants.SYNC_CMM_CLIENT).getObject());
        return SyncContainerUtil.waitForResponseObject(blockingQueue, DiscoveryServiceCompleteStatus.class, POLLING_TIMEOUT);
    }

    public void operationSucceed(ControlOperationResult controlOperationResult) {
    }
}
