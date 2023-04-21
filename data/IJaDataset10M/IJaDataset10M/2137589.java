package org.itsnat.impl.core.listener;

import org.itsnat.impl.core.clientdoc.ClientAJAXDocumentImpl;
import org.itsnat.impl.core.clientdoc.ClientDocumentInvitedRemoteCtrlCometImpl;
import org.itsnat.impl.core.js.listener.EventListenerJSRenderImpl;
import org.itsnat.impl.core.js.listener.ItsNatRemoteCtrlCometEventListenerJSRenderImpl;

/**
 *
 * @author jmarranz
 */
public class RemoteControlCometTaskRegistry extends CometTaskRegistry {

    /**
     * Creates a new instance of RemoteControlCometTaskRegistry
     */
    public RemoteControlCometTaskRegistry(ClientDocumentInvitedRemoteCtrlCometImpl clientDoc) {
        super(clientDoc);
    }

    public ClientDocumentInvitedRemoteCtrlCometImpl getClientDocumentInvitedRemoteCtrlComet() {
        return (ClientDocumentInvitedRemoteCtrlCometImpl) clientDoc;
    }

    public CometTaskEventListenerWrapper createCometTaskEventListenerWrapper(CometTask taskContainer) {
        ClientDocumentInvitedRemoteCtrlCometImpl clientDoc = getClientDocumentInvitedRemoteCtrlComet();
        return new ItsNatRemoteCtrlCometEventListenerWrapperImpl(taskContainer, clientDoc);
    }

    public EventListenerJSRenderImpl getEventListenerJSRender() {
        return ItsNatRemoteCtrlCometEventListenerJSRenderImpl.SINGLETON;
    }
}
