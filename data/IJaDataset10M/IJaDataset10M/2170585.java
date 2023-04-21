package org.jdmp.sigmen.server;

import org.jdmp.sigmen.messages.Constantes.Chat;

public class SendMessage extends Thread {

    private Client client;

    private byte[] data;

    private int type = Chat.NORMAL;

    public SendMessage(Client client, byte[] data) {
        super();
        this.client = client;
        this.data = data;
    }

    public SendMessage(Client client, byte[] data, int type) {
        super();
        this.client = client;
        this.data = data;
        this.type = type;
    }

    @Override
    public void run() {
        client.sendChat(data, type);
    }
}
