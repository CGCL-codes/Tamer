package com.sun.cldc.io.j2me.datagram;

import java.io.*;
import javax.microedition.io.*;
import com.sun.cldc.io.*;

/**
 * This class is required because the J2SE Datagram class is final.
 */
public class DatagramObject extends GeneralBase implements Datagram {

    private static final int MAX_HOST_LENGTH = 256;

    Protocol parent;

    byte[] buf;

    int off = 0;

    int len;

    int ipNumber;

    String host;

    int port;

    public DatagramObject(Protocol parent, byte[] buf, int len) {
        this.parent = parent;
        setData(buf, 0, len);
    }

    public String getAddress() {
        if (host == null) {
            if (ipNumber == 0) return (null);
            try {
                byte[] tmp = new byte[MAX_HOST_LENGTH];
                parent.getHostByAddr(ipNumber, tmp);
                host = (new String(tmp)).trim();
            } catch (java.io.IOException ex) {
            }
        }
        return ("datagram://" + host + ":" + port);
    }

    public byte[] getData() {
        return buf;
    }

    public int getOffset() {
        return off;
    }

    public int getLength() {
        return len;
    }

    public synchronized void setAddress(String addr) throws IOException {
        ipNumber = 0;
        if (!addr.startsWith("datagram://")) {
            throw new IllegalArgumentException("Invalid datagram address" + addr);
        }
        String address = addr.substring(11);
        try {
            host = Protocol.getAddress(address);
            port = Protocol.getPort(address);
        } catch (NumberFormatException x) {
            throw new IllegalArgumentException("Invalid datagram address" + addr);
        }
    }

    public synchronized void setAddress(Datagram reference) {
        DatagramObject ref = (DatagramObject) reference;
        host = ref.host;
        port = ref.port;
        ipNumber = 0;
    }

    public synchronized void setData(byte[] buf, int offset, int length) {
        if (length < 0 || offset < 0 || ((length + offset) > buf.length)) {
            throw new IllegalArgumentException("Illegal length or offset");
        }
        this.buf = buf;
        this.off = offset;
        this.len = length;
    }

    public void setLength(int length) {
        setData(buf, off, length);
    }

    int pointer;

    public void reset() {
        setData(buf, 0, 0);
        pointer = 0;
    }

    public long skip(long n) {
        int min = Math.min((int) n, len - pointer);
        pointer += min;
        return (min);
    }

    public int read() throws IOException {
        if (pointer >= len) {
            return -1;
        }
        return buf[off + (pointer++)] & 0xFF;
    }

    public void write(int ch) throws IOException {
        if (pointer >= buf.length) {
            throw new IndexOutOfBoundsException();
        }
        buf[pointer++] = (byte) ch;
        off = 0;
        len = pointer;
    }
}
