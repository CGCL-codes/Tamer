package br.com.qotsa.mobilepcremotecontrolprotocol.handler;

import br.com.qotsa.mobilepcremotecontrolprotocol.listener.SyncServerListener;

/**
 *
 * @author Francisco Guimar�es
 */
public class SyncServerHandler extends SyncHandler {

    public SyncServerHandler(SyncServerListener listener) {
        super(listener);
    }

    void processMessage(int code, String messageString, byte[] messageByte) {
        SyncServerListener listener = (SyncServerListener) getListener();
        switch(code) {
            case HELLO_SERVER:
                listener.helloServerHandle();
                break;
            case REQUEST_SYNC:
                listener.requestSyncHandle();
                break;
            case SEND_CLIENT_NAME:
                listener.clientNameHandle(messageString);
                break;
            case DISCONNECT:
                listener.disconnect();
                break;
        }
    }

    public int hashCode() {
        return 2;
    }
}
