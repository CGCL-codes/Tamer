package br.com.qotsa.mobilepcremotecontrolprotocol.listener;

import br.com.qotsa.mobilepcremotecontrolprotocol.handler.MobilePCRemoteControlProtocolClientSynchronization;
import br.com.qotsa.mobilepcremotecontrolprotocol.handler.SyncHandler;

/**
 *
 * @author Francisco Guimar�es
 */
public class SyncClientListenerImpl implements SyncClientListener {

    public void helloClientHandle() {
        MobilePCRemoteControlProtocolClientSynchronization sync = MobilePCRemoteControlProtocolClientSynchronization.getInstance();
        sync.getProtocol().sendBuffer(SyncHandler.REQUEST_SYNC);
    }

    public void syncOKHandle() {
        MobilePCRemoteControlProtocolClientSynchronization sync = MobilePCRemoteControlProtocolClientSynchronization.getInstance();
        sync.getProtocol().sendBuffer(SyncHandler.SEND_CLIENT_NAME, sync.getClientName());
    }
}
