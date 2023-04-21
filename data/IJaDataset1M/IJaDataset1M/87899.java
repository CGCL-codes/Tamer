package org.dreamspeak.lib.protocol.packets.inbound.reliablecontent;

import java.nio.ByteBuffer;
import org.dreamspeak.lib.data.Message;
import org.dreamspeak.lib.protocol.ProtocolException;
import org.dreamspeak.lib.protocol.packets.ResponsibleForPacketTypes;

/**
 * TODO: Proper documentation
 * 
 * @author avithan
 */
@ResponsibleForPacketTypes(0x0082)
public class MessageRecieved extends ReliableContent {

    public MessageRecieved(short packetType, ByteBuffer data) throws ProtocolException {
        super(packetType, data);
        byte[] raw = data.array();
        byte color_r = data.get(0x00);
        byte color_g = data.get(0x01);
        byte color_b = data.get(0x02);
        byte rawTarget = data.get(0x04);
        byte senderLength = data.get(0x05);
        int senderOffset = 0x06;
        StringBuilder rawSender = new StringBuilder();
        for (int i = 0; i < senderLength; i++) {
            rawSender.append((char) raw[i + senderOffset]);
        }
        String sender = rawSender.toString();
        StringBuilder rawMessage = new StringBuilder();
        int seqPos = 0x23;
        while (raw[seqPos] != 0x00 && seqPos < raw.length) {
            rawMessage.append((char) raw[seqPos]);
            seqPos++;
        }
        String message = rawMessage.toString();
        Message.Target target = Message.Target.fromByte(rawTarget);
        if (target == Message.Target.Invalid) {
            throw new ProtocolException("Message with invalid Target [" + rawTarget + "] recieved.");
        }
        this.message = new Message(sender, message, target, color_r, color_g, color_b);
    }

    final Message message;

    public Message getMessage() {
        return message;
    }
}
