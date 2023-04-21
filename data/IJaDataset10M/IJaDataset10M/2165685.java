package simpleclient.server;

import com.sun.sgs.app.*;
import java.io.Serializable;

public class EchoClientListener implements ClientSessionListener, Serializable {

    protected ClientSession client;

    public EchoClientListener(ClientSession client) {
        this.client = client;
    }

    public void receivedMessage(byte[] message) {
        System.out.println(new String(message));
        client.send(message);
    }

    public void disconnected(boolean graceful) {
    }
}
