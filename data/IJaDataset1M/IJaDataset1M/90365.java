package br.com.qotsa.btsoftcontrolprotocol.listener;

import br.com.qotsa.btsoftcontrolprotocol.handler.BTSoftControlProtocolServerSynchronization;
import br.com.qotsa.btsoftcontrolprotocol.handler.SyncHandler;

/**
 *
 * @author Francisco Guimar�es
 */
public class SyncServerListenerImpl implements SyncServerListener {

    public void helloServerHandle() {
        BTSoftControlProtocolServerSynchronization.getInstance().sendBuffer(SyncHandler.HELLO_CLIENT);
    }

    public void requestSyncHandle() {
        BTSoftControlProtocolServerSynchronization.getInstance().sendBuffer(SyncHandler.SYNC_STARTED);
    }

    public void clientNameHandle(String clientName) {
        BTSoftControlProtocolServerSynchronization.getInstance().getProtocol().sendBuffer(SyncHandler.SYNC_OK, true, false);
    }

    public void disconnect() {
        try {
            BTSoftControlProtocolServerSynchronization.getInstance().getProtocol().receiveBuffer(false);
        } catch (Exception ex) {
        }
    }
}
