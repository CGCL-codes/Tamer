package br.ufmg.dcc.vod.remoteworkers.processor.ui;

import br.edu.ufcg.lsd.commune.container.control.ApplicationServerManager;
import br.edu.ufcg.lsd.commune.container.servicemanager.client.InitializationContext;
import br.edu.ufcg.lsd.commune.container.servicemanager.client.sync.SyncManagerClient;
import br.ufmg.dcc.vod.remoteworkers.processor.server.ProcessorApplicationServer;

public class ProcessorInitializationContext implements InitializationContext<ApplicationServerManager, SyncManagerClient<ApplicationServerManager>> {

    @Override
    public SyncManagerClient<ApplicationServerManager> createManagerClient() {
        return new SyncManagerClient<ApplicationServerManager>();
    }

    @Override
    public Class<ApplicationServerManager> getManagerObjectType() {
        return ApplicationServerManager.class;
    }

    @Override
    public String getServerContainerName() {
        return ProcessorApplicationServer.SERVER_NAME;
    }
}
