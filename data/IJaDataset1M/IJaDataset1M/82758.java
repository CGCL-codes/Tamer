package fr.cantor.commore.comm;

import fr.cantor.commore.comm.address.ProtocolData;

public final class ListenerFactory {

    private ListenerFactory() {
    }

    static Listener getListener(ProtocolData data) {
        return data.getProtocol().listenerFromProtocolData(data);
    }
}
