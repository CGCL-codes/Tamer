package com.google.code.jahath.common.connection;

import java.io.IOException;
import java.io.InputStream;
import com.google.code.jahath.Connection;
import com.google.code.jahath.ConnectionClosedLocallyException;

class SocketInputStream extends InputStream {

    private final SocketConnection connection;

    private final InputStream parent;

    public SocketInputStream(SocketConnection connection, InputStream parent) {
        this.connection = connection;
        this.parent = parent;
    }

    private boolean isClosedLocally() {
        return connection.getState() != Connection.State.OPEN;
    }

    @Override
    public int available() throws IOException {
        try {
            return parent.available();
        } catch (IOException ex) {
            throw isClosedLocally() ? new ConnectionClosedLocallyException() : ex;
        }
    }

    @Override
    public void close() throws IOException {
        parent.close();
    }

    @Override
    public int read() throws IOException {
        try {
            return parent.read();
        } catch (IOException ex) {
            throw isClosedLocally() ? new ConnectionClosedLocallyException() : ex;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        try {
            return parent.read(b, off, len);
        } catch (IOException ex) {
            throw isClosedLocally() ? new ConnectionClosedLocallyException() : ex;
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        try {
            return parent.read(b);
        } catch (IOException ex) {
            throw isClosedLocally() ? new ConnectionClosedLocallyException() : ex;
        }
    }

    @Override
    public long skip(long n) throws IOException {
        try {
            return parent.skip(n);
        } catch (IOException ex) {
            throw isClosedLocally() ? new ConnectionClosedLocallyException() : ex;
        }
    }
}
