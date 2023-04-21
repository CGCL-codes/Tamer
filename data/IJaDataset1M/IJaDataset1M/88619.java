package org.match.transports;

import org.match.event.Event;

public class ConsoleTransport<U, V, T> implements Transport<U, V, T> {

    private String name = "Console Transport";

    public ConsoleTransport() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void stopAndDisconnect() {
    }

    public boolean send(T message) {
        System.out.println(message.toString());
        return true;
    }

    public void raiseEvent(Event<U, V, T> evt) {
    }
}
