package org.ourgrid.discoveryservice.ui.sync;

import org.ourgrid.common.interfaces.management.DiscoveryServiceManager;
import org.ourgrid.discoveryservice.status.DiscoveryServiceCompleteStatus;
import br.edu.ufcg.lsd.commune.container.ContainerContext;
import br.edu.ufcg.lsd.commune.container.servicemanager.client.InitializationContext;
import br.edu.ufcg.lsd.commune.container.servicemanager.client.sync.SyncApplicationClient;
import br.edu.ufcg.lsd.commune.container.servicemanager.client.sync.SyncContainerUtil;
import br.edu.ufcg.lsd.commune.network.xmpp.CommuneNetworkException;
import br.edu.ufcg.lsd.commune.processor.ProcessorStartException;

/**
 *
 */
public class DiscoveryServiceSyncComponentClient extends SyncApplicationClient<DiscoveryServiceManager, DiscoveryServiceSyncManagerClient> {

    public DiscoveryServiceSyncComponentClient(ContainerContext context) throws CommuneNetworkException, ProcessorStartException {
        super("DS_SYNC_UI", context);
    }

    public DiscoveryServiceCompleteStatus getDiscoveryServiceCompleteStatus() {
        getManager().getCompleteStatus(getManagerClient());
        return SyncContainerUtil.waitForResponseObject(queue, DiscoveryServiceCompleteStatus.class);
    }

    @Override
    protected InitializationContext<DiscoveryServiceManager, DiscoveryServiceSyncManagerClient> createInitializationContext() {
        return new DiscoveryServiceSyncInitializationContext();
    }
}
