package org.itsnat.impl.core.listener.trans;

import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.event.fromclient.ClientNormalEventImpl;
import org.itsnat.impl.core.request.RequestNormalEventImpl;

/**
 *
 * @author jmarranz
 */
public class NodeCompleteTransportUtil extends ParamTransportUtil {

    public static final NodeCompleteTransportUtil SINGLETON = new NodeCompleteTransportUtil();

    /**
     * Creates a new instance of NodeCompleteTransportUtil
     */
    public NodeCompleteTransportUtil() {
    }

    public String getCodeToSend(ParamTransport param) {
        return "    this.getUtil().transpNodeComplete(this,\"" + NodeInnerTransportUtil.getName() + "\");\n";
    }

    public void syncWithServer(ParamTransport param, RequestNormalEventImpl request, ClientNormalEventImpl event) {
        NodeAllAttribTransportUtil.SINGLETON.syncWithServer(request, event);
        NodeInnerTransportUtil.SINGLETON.syncWithServer(request, event);
    }
}
