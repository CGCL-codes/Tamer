package fr.upemlv.transfile.server;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import fr.upemlv.transfile.reply.Reply;

public class ReplySender {

    private final SocketChannel client;

    public ReplySender(SocketChannel client) {
        this.client = client;
    }

    public void send(Reply reply) {
        try {
            client.write(reply.getPacket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
