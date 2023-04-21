package org.apache.harmony.nio.tests.java.nio.channels;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.spi.SelectorProvider;

class MockDatagramChannel extends DatagramChannel {

    public MockDatagramChannel(SelectorProvider arg0) {
        super(arg0);
    }

    public DatagramSocket socket() {
        return null;
    }

    public boolean isConnected() {
        return false;
    }

    public DatagramChannel connect(SocketAddress arg0) throws IOException {
        return null;
    }

    public DatagramChannel disconnect() throws IOException {
        return null;
    }

    public SocketAddress receive(ByteBuffer arg0) throws IOException {
        return null;
    }

    public int send(ByteBuffer arg0, SocketAddress arg1) throws IOException {
        return 0;
    }

    public int read(ByteBuffer arg0) throws IOException {
        return 0;
    }

    public long read(ByteBuffer[] arg0, int arg1, int arg2) throws IOException {
        return 0;
    }

    public int write(ByteBuffer arg0) throws IOException {
        return 0;
    }

    public long write(ByteBuffer[] arg0, int arg1, int arg2) throws IOException {
        return 0;
    }

    protected void implCloseSelectableChannel() throws IOException {
    }

    protected void implConfigureBlocking(boolean arg0) throws IOException {
    }
}
