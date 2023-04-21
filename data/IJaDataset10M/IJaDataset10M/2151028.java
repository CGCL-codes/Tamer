package net.mkp.librtp;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketException;

public abstract class AbstractPacketizer extends Thread implements Runnable {

    protected SmallRtpSocket rsock = null;

    protected InputStream fis = null;

    protected boolean running = false;

    protected byte[] buffer = new byte[4096];

    protected final int rtphl = 12;

    public AbstractPacketizer(InputStream fis, InetAddress dest, int port) throws SocketException {
        this.fis = fis;
        this.rsock = new SmallRtpSocket(dest, port, buffer);
    }

    public void startStreaming() {
        running = true;
        start();
    }

    public void stopStreaming() {
        running = false;
    }

    public abstract void run();

    protected String printBuffer(int start, int end) {
        String str = "";
        for (int i = start; i < end; i++) str += "," + Integer.toHexString(buffer[i] & 0xFF);
        return str;
    }
}
