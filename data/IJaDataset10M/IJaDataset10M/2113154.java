package org.mobicents.diameter.impl.ha.server.cxdx;

import org.jboss.cache.Fqn;
import org.jdiameter.api.cxdx.ServerCxDxSession;
import org.jdiameter.client.api.IContainer;
import org.jdiameter.common.api.app.cxdx.CxDxSessionState;
import org.jdiameter.server.impl.app.cxdx.IServerCxDxSessionData;
import org.mobicents.cluster.MobicentsCluster;
import org.mobicents.diameter.impl.ha.common.cxdx.CxDxSessionDataReplicatedImpl;
import org.mobicents.diameter.impl.ha.data.ReplicatedSessionDatasource;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class ServerCxDxSessionDataReplicatedImpl extends CxDxSessionDataReplicatedImpl implements IServerCxDxSessionData {

    /**
   * @param nodeFqn
   * @param mobicentsCluster
   * @param iface
   */
    public ServerCxDxSessionDataReplicatedImpl(Fqn<?> nodeFqn, MobicentsCluster mobicentsCluster, IContainer container) {
        super(nodeFqn, mobicentsCluster, container);
        if (super.create()) {
            setAppSessionIface(this, ServerCxDxSession.class);
            setCxDxSessionState(CxDxSessionState.IDLE);
        }
    }

    /**
   * @param sessionId
   * @param mobicentsCluster
   * @param iface
   */
    public ServerCxDxSessionDataReplicatedImpl(String sessionId, MobicentsCluster mobicentsCluster, IContainer container) {
        this(Fqn.fromRelativeElements(ReplicatedSessionDatasource.SESSIONS_FQN, sessionId), mobicentsCluster, container);
    }
}
