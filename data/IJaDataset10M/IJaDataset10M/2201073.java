package org.mobicents.diameter.impl.ha.common.auth;

import org.jdiameter.api.app.AppSession;
import org.jdiameter.api.auth.ClientAuthSession;
import org.jdiameter.api.auth.ServerAuthSession;
import org.jdiameter.common.api.app.IAppSessionDataFactory;
import org.jdiameter.common.api.app.auth.IAuthSessionData;
import org.jdiameter.common.api.data.ISessionDatasource;
import org.mobicents.cluster.MobicentsCluster;
import org.mobicents.diameter.impl.ha.client.auth.ClientAuthSessionDataReplicatedImpl;
import org.mobicents.diameter.impl.ha.data.ReplicatedSessionDatasource;
import org.mobicents.diameter.impl.ha.server.auth.ServerAuthSessionDataReplicatedImpl;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class AuthReplicatedSessionDataFactory implements IAppSessionDataFactory<IAuthSessionData> {

    private ReplicatedSessionDatasource replicatedSessionDataSource;

    private MobicentsCluster mobicentsCluster;

    /**
   * @param replicatedSessionDataSource
   */
    public AuthReplicatedSessionDataFactory(ISessionDatasource replicatedSessionDataSource) {
        super();
        this.replicatedSessionDataSource = (ReplicatedSessionDatasource) replicatedSessionDataSource;
        this.mobicentsCluster = this.replicatedSessionDataSource.getMobicentsCluster();
    }

    @Override
    public IAuthSessionData getAppSessionData(Class<? extends AppSession> clazz, String sessionId) {
        if (clazz.equals(ClientAuthSession.class)) {
            ClientAuthSessionDataReplicatedImpl data = new ClientAuthSessionDataReplicatedImpl(sessionId, this.mobicentsCluster);
            return data;
        } else if (clazz.equals(ServerAuthSession.class)) {
            ServerAuthSessionDataReplicatedImpl data = new ServerAuthSessionDataReplicatedImpl(sessionId, this.mobicentsCluster);
            return data;
        }
        throw new IllegalArgumentException();
    }
}
