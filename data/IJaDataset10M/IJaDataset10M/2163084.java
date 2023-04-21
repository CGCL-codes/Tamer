package com.jcraft.jsch;

public class RequestSftp extends Request {

    RequestSftp() {
        setReply(true);
    }

    public void request(Session session, Channel channel) throws Exception {
        super.request(session, channel);
        Buffer buf = new Buffer();
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte((byte) Session.SSH_MSG_CHANNEL_REQUEST);
        buf.putInt(channel.getRecipient());
        buf.putString("subsystem".getBytes());
        buf.putByte((byte) (waitForReply() ? 1 : 0));
        buf.putString("sftp".getBytes());
        write(packet);
    }
}
