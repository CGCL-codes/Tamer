package com.dukesoftware.utils.net;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * 
 *
 */
public interface IServerProcess {

    void process(Socket socket);

    void init(ServerSocket socket);

    void finish();
}
