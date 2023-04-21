package nu.boomboom.p2p.gnutella.message;

import java.io.*;
import nu.boomboom.p2p.util.*;

public class MessageBody {

    byte payload[];

    public MessageBody(byte payload[]) throws P2PException {
        if (payload == null) throw new P2PException("Invalid payload");
        this.payload = payload;
    }

    public byte[] getPayload() {
        return payload;
    }

    public static MessageBody getMessageBody(InputStream in, MessageHeader header) throws P2PException {
        try {
            int payloadLen = header.payloadLen;
            if (payloadLen <= 0) return new MessageBody(new byte[0]);
            byte payload[] = new byte[payloadLen];
            if (in.read(payload) < payloadLen) throw new P2PException("connection closed");
            return new MessageBody(payload);
        } catch (IOException ex) {
            throw new P2PException(ex);
        }
    }

    public void send(OutputStream out) throws P2PException {
        try {
            for (int i = 0; i < payload.length; i++) out.write(payload[i]);
        } catch (IOException ex) {
            throw new P2PException(ex);
        }
    }
}
