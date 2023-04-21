package se.sics.tac.aw;

import java.io.IOException;

public abstract class TACConnection {

    protected TACAgent agent;

    final void init(TACAgent agent) {
        this.agent = agent;
        init();
    }

    protected abstract void init();

    public abstract boolean isConnected();

    public abstract void disconnect();

    public abstract void sendMessage(TACMessage msg) throws IOException;

    public void sendMessage(TACMessage msg, TACMessageReceiver rcv) throws IOException {
        msg.setMessageReceiver(rcv);
        sendMessage(msg);
    }
}
