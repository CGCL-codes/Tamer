package net.sourceforge.javautil.network.socket;

import java.net.InetAddress;

/**
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public interface ISocketTransportEvent {

    ISocketTransport getTransport();

    InetAddress getFrom();

    int getPort();

    byte[] getData();

    int getOffset();

    int getLength();
}
