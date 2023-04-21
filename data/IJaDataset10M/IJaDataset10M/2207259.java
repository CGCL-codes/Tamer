package com.waiml.ssentinel.datapump;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import org.apache.log4j.Logger;
import com.waiml.ssentinel.executors.SSentinelExecutor;
import com.waiml.ssentinel.processors.InterFaces.ISSentinelSytemProcessors;

public class ServerMonitor implements ISSentinelSytemProcessors {

    private Logger sLog = Logger.getLogger(this.getClass());

    private ServerSocket arrayServer;

    public static LinkedList<MonitorObject> list;

    public static final String SIGNAL_MONITORING_OBJECT_CODE = "SMO";

    private Socket client = null;

    public int priority = 5;

    private int miliSecondsToSleep = 1000;

    private ObjectInputStream ois = null;

    private ObjectOutputStream oos = null;

    MonitorObject signalMonitorObject = new MonitorObject();

    public void run() {
        ServerMonitor.list = new LinkedList<MonitorObject>();
        signalMonitorObject.setMonitoredThread(SIGNAL_MONITORING_OBJECT_CODE);
        signalMonitorObject.setTotalIterations(ServerMonitor.list.size());
        signalMonitorObject.setQueueSize(ServerMonitor.list.size());
        try {
            arrayServer = new ServerSocket(4000);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        sLog.info("Server listening on port 4000.");
        while (!SSentinelExecutor.closing) {
            try {
                arrayServer.setSoTimeout(miliSecondsToSleep);
                sLog.info("Waiting for connection.");
                client = arrayServer.accept();
                sLog.info("Connection accepted from: " + client.getInetAddress());
                ois = new ObjectInputStream(client.getInputStream());
                oos = new ObjectOutputStream(client.getOutputStream());
                while (!SSentinelExecutor.closing && client.isConnected()) {
                    if (ServerMonitor.list.isEmpty()) {
                        oos.writeObject(signalMonitorObject);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    } else {
                        MonitorObject output = ServerMonitor.list.remove();
                        output.setQueueSize(ServerMonitor.list.size());
                        oos.writeObject(output);
                    }
                    oos.flush();
                    if (SSentinelExecutor.closing) break;
                }
                ois.close();
                oos.close();
                client.close();
            } catch (SocketTimeoutException ste) {
                if (SSentinelExecutor.closing) {
                    break;
                }
            } catch (SocketException se) {
                sLog.info("SocketException.");
            } catch (IOException se) {
                sLog.info("IOException.");
            } catch (Exception e) {
                sLog.info("Exception.");
            } finally {
            }
        }
        sLog.info("ServerSocket closing : ");
    }

    public int getPriority() {
        return this.priority;
    }

    public synchronized void pushSerializedObject(MonitorObject serializedObject) {
        if (client != null && client.isConnected()) {
            ServerMonitor.list.add(serializedObject);
            if (ServerMonitor.list.size() > 50) {
                ServerMonitor.list = new LinkedList<MonitorObject>();
            }
        }
        sLog.debug("Queue size : " + ServerMonitor.list.size());
    }
}
