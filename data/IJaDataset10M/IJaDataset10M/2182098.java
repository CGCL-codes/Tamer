package com.leemba.monitor.server.sensor.active.http;

/**
 *
 * @author mrjohnson
 */
public interface HttpClientListener {

    public void responseReceived(ClientHttpResponse response);

    public void exceptionCaught(Throwable exception);
}
