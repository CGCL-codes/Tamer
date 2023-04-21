package fr.upemlv.transfile.reply;

import java.nio.ByteBuffer;

public interface Reply {

    public abstract ByteBuffer getPacket();

    public abstract byte getType();

    public abstract ReplyHandler createReplyHandler();
}
