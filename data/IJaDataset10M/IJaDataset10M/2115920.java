package org.itsnat.impl.core.listener.trans;

import org.itsnat.core.event.NodeAllAttribTransport;
import org.itsnat.core.event.SingleParamTransport;
import org.itsnat.core.event.NodeCompleteTransport;
import org.itsnat.core.event.NodeMutationTransport;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.event.fromclient.ClientNormalEventImpl;
import org.itsnat.impl.core.request.RequestNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public abstract class ParamTransportUtil {

    /**
     * Creates a new instance of ParamTransportUtil
     */
    public ParamTransportUtil() {
    }

    public static ParamTransportUtil getSingleton(ParamTransport param) {
        if (param instanceof SingleParamTransport) return SingleParamTransportUtil.getSingleParamTransportUtilSingleton((SingleParamTransport) param); else if (param instanceof NodeAllAttribTransport) return NodeAllAttribTransportUtil.SINGLETON; else if (param instanceof NodeCompleteTransport) return NodeCompleteTransportUtil.SINGLETON; else if (param instanceof NodeMutationTransport) return NodeMutationTransportUtil.SINGLETON;
        return null;
    }

    public abstract String getCodeToSend(ParamTransport param);

    public abstract void syncWithServer(ParamTransport param, RequestNormalEventImpl request, ClientNormalEventImpl event);
}
