package com.arsenal.rtcomm.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import com.arsenal.log.Log;
import com.arsenal.util.ReThread;
import java.io.*;

public class SocketListener implements Runnable {

    SocketServer socketServer = null;

    ServerSocket serverSocket = null;

    int port;

    public SocketListener(SocketServer socketServer, int port) {
        this.socketServer = socketServer;
        this.port = port;
        Log.debug(this, "Attempting to create SocketListener on port: " + this.port);
        if (port != 0) {
            try {
                serverSocket = new ServerSocket(port);
                ReThread thread = new ReThread(this, "SocketListener");
                thread.start();
            } catch (IOException e) {
                Log.debug(this, e.getMessage(), e);
                return;
            }
            Log.info(this, "SocketServer running on port " + port);
        } else {
            Log.info(this, "Socket connections turned OFF");
        }
    }

    public void run() {
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                Runnable runner = new Runnable() {

                    public void run() {
                        try {
                            socketServer.processNewConnection(socket);
                        } catch (Exception e) {
                            Log.debug(this, e.getMessage(), e);
                        }
                    }
                };
                new ReThread(runner, "SocketListener.run").start();
            } catch (Exception e) {
                Log.debug(this, e.getMessage(), e);
            }
        }
    }
}
