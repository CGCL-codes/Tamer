package org.asmatron.messengine.messaging;

public interface MessageListener<T extends Message<?>> {

    void onMessage(T message);
}
