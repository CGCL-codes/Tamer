package org.datanucleus.store.hbase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

public class HBaseConnectionPool {

    private final List<HBaseManagedConnection> connections;

    private final ThreadLocal<WeakReference<HBaseManagedConnection>> connectionForCurrentThread;

    private final Timer evictorThread;

    private int timeBetweenEvictionRunsMillis = 15 * 1000;

    public HBaseConnectionPool() {
        connectionForCurrentThread = new ThreadLocal<WeakReference<HBaseManagedConnection>>();
        connections = new CopyOnWriteArrayList<HBaseManagedConnection>();
        evictorThread = new Timer("HBase Connection Evictor", true);
        startConnectionEvictorThread(evictorThread);
    }

    public void registerConnection(HBaseManagedConnection managedConnection) {
        connections.add(managedConnection);
        connectionForCurrentThread.set(new WeakReference<HBaseManagedConnection>(managedConnection));
    }

    public HBaseManagedConnection getPooledConnection() {
        WeakReference<HBaseManagedConnection> ref = connectionForCurrentThread.get();
        if (ref == null) {
            return null;
        } else {
            HBaseManagedConnection managedConnection = ref.get();
            if (managedConnection != null && !managedConnection.isDisposed()) {
                return managedConnection;
            } else {
                return null;
            }
        }
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    private void disposeTimedOutConnections() {
        List<HBaseManagedConnection> timedOutConnections = new ArrayList<HBaseManagedConnection>();
        for (HBaseManagedConnection managedConnection : connections) {
            if (managedConnection.isExpired()) {
                timedOutConnections.add(managedConnection);
            }
        }
        for (HBaseManagedConnection managedConnection : timedOutConnections) {
            managedConnection.dispose();
            connections.remove(managedConnection);
        }
    }

    private void startConnectionEvictorThread(Timer connectionTimeoutThread) {
        TimerTask timeoutTask = new TimerTask() {

            public void run() {
                disposeTimedOutConnections();
            }
        };
        evictorThread.schedule(timeoutTask, timeBetweenEvictionRunsMillis, timeBetweenEvictionRunsMillis);
    }
}
