package com.coladoro.core.net.ipv4;

import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketConnector;

/**
 * @author tanis
 */
public interface NetCommunicator {

    public SocketAcceptor getAcceptor();

    public SocketConnector getConnector();
}
