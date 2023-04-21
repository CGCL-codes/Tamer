package xjava.net;

import java.net.*;
import java.io.*;

public class XServerSocket extends ServerSocket {

    private boolean showOutput = true;

    private XStreamParent xparent = null;

    private MultiListen ml = null;

    public int id = 0;

    public XServerSocket(int port) throws IOException {
        super(port);
    }

    public XServerSocket(XStreamParent xparent) throws IOException {
        super();
        this.xparent = xparent;
    }

    public XServerSocket(int port, XStreamParent xparent) throws IOException {
        super();
        this.xparent = xparent;
        multiListen(port);
    }

    public void showOutput() {
        showOutput = true;
    }

    public void hideOutput() {
        showOutput = false;
    }

    public boolean isOutputOn() {
        return showOutput;
    }

    public void setParent(XStreamParent xparent) {
        this.xparent = xparent;
    }

    public boolean multiListen(int port) throws IOException {
        if (xparent != null) {
            InetSocketAddress addr = new InetSocketAddress(port);
            bind(addr);
            ml = new MultiListen(this);
            return true;
        } else {
            if (showOutput) {
                System.out.println("XServerSocket: Failed to mulitlisten, no parent set.");
            }
            return false;
        }
    }

    public void accept(XSocket target) throws IOException {
        implAccept(target);
    }

    public XSocket accept() throws IOException {
        XSocket sck = new XSocket();
        if (!showOutput) {
            sck.hideDebug();
        }
        implAccept(sck);
        sck.initStreams();
        sck.server = true;
        return sck;
    }

    public void stopListen() {
        if (ml != null) {
            ml.thread.interrupt();
        }
    }

    void callParentNewClient(XSocket client) {
        xparent.newClient(client, this);
    }
}

class MultiListen implements Runnable {

    public Thread thread = new Thread(this);

    private XServerSocket sck = null;

    public MultiListen(XServerSocket sck) {
        this.sck = sck;
        thread.start();
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                sck.callParentNewClient(sck.accept());
            } catch (Exception e) {
                if (sck.isOutputOn()) {
                    System.out.println("XSocketServer: Stopped to listen for new clients (" + e + ")");
                }
                break;
            }
        }
    }
}
