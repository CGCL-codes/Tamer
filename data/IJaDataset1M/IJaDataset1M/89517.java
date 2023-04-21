package com.scbeta.net.xiep;

import com.scbeta.helpers.EventHelper;
import com.scbeta.helpers.SingleEventListener;
import com.scbeta.net.xiep.eventArgs.XiepClientEventArgs;
import com.scbeta.net.xiep.helpers.XiepIoHelper;
import com.scbeta.net.xiep.packages.AbstractXiepPackage;
import com.scbeta.net.xiep.packages.EventPackage;
import com.scbeta.net.xiep.packages.RequestPackage;
import com.scbeta.net.xiep.packages.ResponsePackage;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.EventObject;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * XIEP(Xml-based Information Exchange Protocol)客户端
 * @author aaa
 */
public class XiepClient {

    private Socket socket;

    private String serverHostName;

    private int serverPort;

    private int heartBeatInterval = 10;

    private int heartBeatTimeout = 30;

    private Map<String, ResponsePackage> mapRequestResponse;

    public int getLocalPort() {
        if (socket == null) {
            return -1;
        } else {
            return socket.getLocalPort();
        }
    }

    public String getServerHostName() {
        return serverHostName;
    }

    public void setServerHostName(String value) {
        serverHostName = value;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int value) {
        serverPort = value;
    }

    public int getHeartBeatInterval() {
        return heartBeatInterval;
    }

    public void setHeartBeatInterval(int value) {
        heartBeatInterval = value;
    }

    public int getHeartBeatTimeout() {
        return heartBeatTimeout;
    }

    public void setHeartBeatTimeout(int value) {
        heartBeatTimeout = value;
    }

    public boolean getIsConnected() {
        return socket != null;
    }

    private EventHelper eventHelper = new EventHelper();

    public EventHelper getEventHelper() {
        return eventHelper;
    }

    public interface ServerEventCameListener extends SingleEventListener<XiepClientEventArgs> {
    }

    public void addServerEventCameListener(ServerEventCameListener listener) {
        eventHelper.addListener(listener);
    }

    public interface ServerDisconnectedListener extends SingleEventListener<EventObject> {
    }

    public void addServerDisconnectedListener(ServerDisconnectedListener listener) {
        eventHelper.addListener(listener);
    }

    public synchronized void removeListener(SingleEventListener listener) {
        eventHelper.removeListener(listener);
    }

    public XiepClient(Socket socket) {
        this.socket = socket;
        init();
    }

    public XiepClient(String serverHostName, int serverPort) {
        this.serverHostName = serverHostName;
        this.serverPort = serverPort;
        init();
    }

    private void init() {
        mapRequestResponse = new LinkedHashMap<String, ResponsePackage>();
    }

    public void start() throws IOException {
        if (!this.getIsConnected()) {
            socket = new Socket();
            socket.setSoTimeout(0);
            socket.connect(new InetSocketAddress(serverHostName, serverPort));
        }
        Thread trdRecv = new Thread(new Runnable() {

            @Override
            public void run() {
                receiveResponseThreadFunction();
            }
        });
        trdRecv.start();
        Thread trdHeartBeat = new Thread(new Runnable() {

            @Override
            public void run() {
                heartBeatThreadFunction();
            }
        });
        trdHeartBeat.start();
    }

    public void stop() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(XiepClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        socket = null;
    }

    public static Boolean IsResponseSuccess(ResponsePackage responsePackage) {
        if (responsePackage == null) {
            return false;
        }
        return "Success".equals(responsePackage.getResponse());
    }

    public ResponsePackage SendRequest(RequestPackage requestPackage) {
        return SendRequest(requestPackage, 10);
    }

    public ResponsePackage SendRequest(RequestPackage requestPackage, int timeoutSeconds) {
        String requestId = requestPackage.getRequestId();
        if (!XiepIoHelper.SendPackage(socket, requestPackage)) {
            return null;
        }
        synchronized (mapRequestResponse) {
            mapRequestResponse.put(requestId, null);
        }
        long startWaitResponseTime = System.currentTimeMillis();
        ResponsePackage responsePackage = null;
        while (true) {
            synchronized (mapRequestResponse) {
                responsePackage = mapRequestResponse.get(requestId);
            }
            long usedSeconds = (System.currentTimeMillis() - startWaitResponseTime) / 1000;
            if (responsePackage != null || usedSeconds > timeoutSeconds) {
                break;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
        synchronized (mapRequestResponse) {
            if (mapRequestResponse.containsKey(requestId)) {
                mapRequestResponse.remove(requestId);
            }
        }
        return responsePackage;
    }

    private void heartBeatThreadFunction() {
        Socket currentSocket = socket;
        while (socket != null && currentSocket == socket) {
            SendRequest(new RequestPackage("XiepPing", null));
            try {
                Thread.sleep(heartBeatInterval * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(XiepClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void receiveResponseThreadFunction() {
        try {
            while (true) {
                AbstractXiepPackage recvPackage = XiepIoHelper.ReceivePackage(socket);
                if (recvPackage == null) {
                    throw new IOException("获取数据包失败。");
                }
                if (ResponsePackage.class.isInstance(recvPackage)) {
                    ResponsePackage responsePackage = (ResponsePackage) recvPackage;
                    String requestId = responsePackage.getRequestId();
                    synchronized (mapRequestResponse) {
                        if (mapRequestResponse.containsKey(requestId)) {
                            mapRequestResponse.put(requestId, responsePackage);
                        }
                    }
                } else if (EventPackage.class.isInstance(recvPackage)) {
                    EventPackage eventPackage = (EventPackage) recvPackage;
                    eventHelper.beginPerformEvent(ServerEventCameListener.class, new XiepClientEventArgs(this, eventPackage));
                }
            }
        } catch (Exception ex) {
            socket = null;
            eventHelper.beginPerformEvent(ServerDisconnectedListener.class, null);
            return;
        }
    }
}
