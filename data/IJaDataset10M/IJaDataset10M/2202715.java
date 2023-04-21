package com.mycila.jms;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JMSClient {

    String getClientId();

    JMSMetaData getMetaData();

    boolean isStarted();

    void start();

    void stop();

    <T extends Serializable> JMSOutboundMessage<T> createMessage(T message);

    <T extends Serializable> JMSOutboundMessage<T> createMessage(T message, Map<String, Serializable> properties);

    void subscribe(String destination, JMSListener listener);

    void subscribe(String destination, String selector, JMSListener listener);

    void unsubscribe(JMSListener listener);

    <T extends Serializable> JMSInboundMessage<T> receive(String destination) throws TimeoutException;

    <T extends Serializable> JMSInboundMessage<T> receive(String destination, String selector) throws TimeoutException;

    <T extends Serializable> JMSInboundMessage<T> receive(String destination, long timeout, TimeUnit unit) throws TimeoutException;

    <T extends Serializable> JMSInboundMessage<T> receive(String destination, String selector, long timeout, TimeUnit unit) throws TimeoutException;
}
