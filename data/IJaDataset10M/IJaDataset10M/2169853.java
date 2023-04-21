package com.sun.midp.push.persistence;

import java.io.IOException;
import java.util.Map;
import com.sun.midp.push.controller.ConnectionInfo;

public class ProxyStore implements Store {

    private final Store impl;

    protected Store getImpl() {
        return impl;
    }

    public ProxyStore(Store impl) {
        this.impl = impl;
    }

    public void listConnections(ConnectionsConsumer connectionsConsumer) {
        impl.listConnections(connectionsConsumer);
    }

    public void addConnection(int midletSuiteID, ConnectionInfo connection) throws IOException {
        impl.addConnection(midletSuiteID, connection);
    }

    public void addConnections(int midletSuiteID, ConnectionInfo[] connections) throws IOException {
        impl.addConnections(midletSuiteID, connections);
    }

    public void removeConnection(int midletSuiteID, ConnectionInfo connection) throws IOException {
        impl.removeConnection(midletSuiteID, connection);
    }

    public void removeConnections(int midletSuiteID) throws IOException {
        impl.removeConnections(midletSuiteID);
    }

    public void listAlarms(AlarmsConsumer alarmsConsumer) {
        impl.listAlarms(alarmsConsumer);
    }

    public void addAlarm(int midletSuiteID, String midlet, long time) throws IOException {
        impl.addAlarm(midletSuiteID, midlet, time);
    }

    public void removeAlarm(int midletSuiteID, String midlet) throws IOException {
        impl.removeAlarm(midletSuiteID, midlet);
    }
}
