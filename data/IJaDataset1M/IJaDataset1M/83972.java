package org.spbgu.pmpu.athynia.central.classloader.network;

import java.nio.channels.SocketChannel;

/**
 * Author: Selivanov
 * Date: 10.03.2007
 * Time: 1:31:23
 */
public class ServerDataEvent {

    public Server server;

    public SocketChannel socket;

    public byte[] data;

    public ServerDataEvent(Server server, SocketChannel socket, byte[] data) {
        this.server = server;
        this.socket = socket;
        this.data = data;
    }
}
