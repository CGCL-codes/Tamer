package net.sourceforge.javautil.network.transport;

import java.net.InetAddress;

/**
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public interface INetworkTransport {

    InetAddress getFrom();

    int getPort();
}
