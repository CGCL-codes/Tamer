package com.emcaster.topics;

import java.net.InetAddress;

public class MessageImpl implements Message {

    private final InetAddress _address;

    private final int _port;

    private final byte[] _msg;

    private final String _topic;

    public MessageImpl(Message msg) {
        _address = msg.getAddress();
        _port = msg.getPort();
        byte[] orig = msg.getMessage();
        _msg = new byte[orig.length];
        System.arraycopy(orig, 0, _msg, 0, orig.length);
        _topic = msg.getTopic();
    }

    public Message copy() {
        return new MessageImpl(this);
    }

    public InetAddress getAddress() {
        return _address;
    }

    public byte[] getMessage() {
        return _msg;
    }

    public int getPort() {
        return _port;
    }

    public String getTopic() {
        return _topic;
    }
}
