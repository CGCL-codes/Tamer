package br.com.qotsa.btsoftcontrolprotocol.handler;

import br.com.qotsa.btsoftcontrolprotocol.listener.EndOfCommunicationListener;
import br.com.qotsa.btsoftcontrolprotocol.listener.ErrorListener;
import br.com.qotsa.btsoftcontrolprotocol.listener.SyncServerListener;
import br.com.qotsa.btsoftcontrolprotocol.protocol.Connection;

/**
 *
 * @author Francisco Guimar�es
 */
public class BTSoftControlProtocolServerSynchronization extends BTSoftControlProtocolSynchronization {

    private static BTSoftControlProtocolServerSynchronization instance;

    private BTSoftControlProtocolServerSynchronization(Connection connection, ErrorListener errorListener, EndOfCommunicationListener endOfCommunicationListener, SyncServerListener syncServerListener) {
        super(connection, errorListener, endOfCommunicationListener, new SyncServerHandler(syncServerListener));
    }

    public static void waitSync(Connection connection, ErrorListener errorListener, EndOfCommunicationListener endOfCommunicationListener, SyncServerListener syncServerListener) throws Exception {
        connection.newConnection(4);
        instance = new BTSoftControlProtocolServerSynchronization(connection, errorListener, endOfCommunicationListener, syncServerListener);
        instance.getProtocol().receiveBuffer(false);
    }

    public static BTSoftControlProtocolServerSynchronization getInstance() {
        return instance;
    }
}
