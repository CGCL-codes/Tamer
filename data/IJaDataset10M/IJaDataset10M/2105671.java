package com.clanwts.w3gs.server;

import java.net.InetSocketAddress;
import edu.cmu.ece.agora.codecs.Message;
import edu.cmu.ece.agora.core.EventObject;

public class ClientMessageEvent extends EventObject {

    /**
   * 
   */
    private static final long serialVersionUID = -3079739139385284802L;

    private InetSocketAddress address;

    private Message message;

    public ClientMessageEvent(Gateway source, Object context, InetSocketAddress address, Message message) {
        super(source, context);
        this.address = address;
        this.message = message;
    }

    @Override
    public Gateway getSource() {
        return (Gateway) super.getSource();
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public Message getMessage() {
        return message;
    }
}
