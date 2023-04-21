package com.scbeta.net.base;

import com.scbeta.helpers.EventHelper;
import com.scbeta.helpers.NetHelper;
import com.scbeta.helpers.SingleEventListener;
import com.scbeta.net.base.eventArgs.*;
import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 基于TCP/IP协议网络平台类
 * @author aaa
 */
public class TcpIpServer {

    private ServerSocket tcpListener;

    private DatagramSocket udpListener;

    private int bufferSize = 1024;

    private int writeTimeOut = 0;

    private int readTimeOut = 0;

    private int backlog = 50;

    private int tcpListenPort = -1;

    private int udpListenPort = -1;

    private boolean udpListenBroadcast = false;

    public ServerSocket getTcpListener() {
        return tcpListener;
    }

    public DatagramSocket getUdpListener() {
        return udpListener;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int value) {
        bufferSize = value;
    }

    public int getWriteTimeOut() {
        return writeTimeOut;
    }

    public void setWriteTimeOut(int value) {
        if (value <= 0) {
            value = 10 * 1000;
        }
        writeTimeOut = value;
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int value) {
        if (value <= 0) {
            value = 10 * 1000;
        }
        readTimeOut = value;
    }

    public int getBacklog() {
        return backlog;
    }

    public void setBacklog(int value) {
        backlog = value;
    }

    public int getTcpListenPort() {
        return tcpListenPort;
    }

    public int getTcpListeningPort() {
        if (tcpListener == null) {
            return -1;
        }
        if (tcpListenPort != 0) {
            return tcpListenPort;
        }
        return tcpListener.getLocalPort();
    }

    public void setTcpListenPort(int value) {
        if (value > 65535 || value < -1) {
            value = 0;
        }
        tcpListenPort = value;
    }

    public int getUdpListenPort() {
        return udpListenPort;
    }

    public int getUdpListeningPort() {
        if (udpListener == null) {
            return -1;
        }
        if (udpListenPort != 0) {
            return udpListenPort;
        }
        return udpListener.getLocalPort();
    }

    public void setUdpListenPort(int value) {
        if (value > 65535 || value < -1) {
            value = 0;
        }
        udpListenPort = value;
    }

    public boolean getUdpListenBroadcast() {
        return udpListenBroadcast;
    }

    public void setUdpListenBroadcast(boolean value) {
        udpListenBroadcast = value;
    }

    private EventHelper eventHelper = new EventHelper();

    public EventHelper getEventHelper() {
        return eventHelper;
    }

    public interface NewTcpConnectedListener extends SingleEventListener<NewTcpConnectedArgs> {
    }

    public void addNewTcpConnectedListener(NewTcpConnectedListener listener) {
        eventHelper.addListener(listener);
    }

    public interface NewUdpConnectedListener extends SingleEventListener<NewUdpConnectedArgs> {
    }

    public void addNewUdpConnectedListener(NewUdpConnectedListener listener) {
        eventHelper.addListener(listener);
    }

    public synchronized void removeListener(SingleEventListener listener) {
        eventHelper.removeListener(listener);
    }

    public void start() throws SocketException, IOException {
        InetAddress localAddress = null;
        if (getUdpListenPort() != -1) {
            if (getUdpListenPort() == 0) {
                while (true) {
                    int randomPort = NetHelper.GetRandomPort();
                    try {
                        udpListener = new DatagramSocket(randomPort, localAddress);
                        break;
                    } catch (SocketException ex) {
                        Logger.getLogger(TcpIpServer.class.getName()).log(Level.SEVERE, null, ex);
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex1) {
                        }
                    }
                }
            } else {
                udpListener = new DatagramSocket(getUdpListenPort(), localAddress);
            }
            udpListener.setBroadcast(getUdpListenBroadcast());
            Thread trdListenUdp = new UdpListenThread(this);
            trdListenUdp.start();
        }
        if (getTcpListenPort() != -1) {
            if (getTcpListenPort() == 0) {
                while (true) {
                    int randomPort = NetHelper.GetRandomPort();
                    try {
                        tcpListener = new ServerSocket(randomPort, getBacklog(), localAddress);
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(TcpIpServer.class.getName()).log(Level.SEVERE, null, ex);
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException ex1) {
                        }
                    }
                }
            } else {
                tcpListener = new ServerSocket(getTcpListenPort(), getBacklog(), localAddress);
            }
            tcpListener.setSoTimeout(this.getReadTimeOut());
            Thread trdListenTcp = new TcpListenThread(this);
            trdListenTcp.start();
        }
    }

    public void stop() {
        if (udpListener != null && getUdpListenPort() != -1) {
            udpListener.close();
            udpListener = null;
        }
        if (tcpListener != null && getTcpListenPort() != -1) {
            try {
                tcpListener.close();
            } catch (IOException ex) {
                Logger.getLogger(TcpIpServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            tcpListener = null;
        }
    }
}
