package fr.upemlv.transfile.reply.impl;

import java.nio.ByteBuffer;
import fr.upemlv.transfile.reply.ReplyHandler;

public class AckReply extends AbstractReply {

    public AckReply() {
        super(RPL_ACK);
    }

    @Override
    public ByteBuffer getPacket() {
        ByteBuffer packet = ByteBuffer.allocate(2);
        packet.put(RPL_ACK);
        packet.put(BYTE_EOT);
        packet.flip();
        return packet;
    }

    @Override
    public ReplyHandler createReplyHandler() {
        return null;
    }
}
