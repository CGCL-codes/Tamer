package org.homemotion.events;

public interface MessageListener {

    public void receive(Object message, String target);
}
