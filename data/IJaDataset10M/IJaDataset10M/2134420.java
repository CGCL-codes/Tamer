package com.voztele.sipspy.connections;

public class ConnectionEvent {

    private Connection source;

    private int command;

    public ConnectionEvent(Connection c, int s) {
        this.source = c;
        this.command = s;
    }

    public Connection getSource() {
        return source;
    }

    public int getEventType() {
        return command;
    }
}
