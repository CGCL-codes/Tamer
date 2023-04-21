package com.peterhi.server;

import java.net.SocketAddress;
import org.xsocket.connection.INonBlockingConnection;

public class ClientAlreadyExistsException extends Exception {

    public ClientAlreadyExistsException(String email) {
        super("client already exists: '" + email + "'");
    }

    public ClientAlreadyExistsException(int id) {
        super("client already exists: [" + id + "]");
    }

    public ClientAlreadyExistsException(INonBlockingConnection conn) {
        super("client already exists: {" + connToString(conn) + "}");
    }

    public ClientAlreadyExistsException(Server.ClientHandle ch) {
        super("client already exists -> " + ch);
    }

    private static String connToString(INonBlockingConnection conn) {
        if (conn == null || conn.getAttachment() == null) {
            return "" + conn;
        }
        return "" + conn.getAttachment();
    }
}
