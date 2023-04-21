package com.dukesoftware.utils.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import com.dukesoftware.utils.thread.ILoopTaskAdv;

/**
 * 
 * 
 *
 *
 */
public final class ServerCommunicator implements ILoopTaskAdv {

    private final String serverName;

    private final int serverPort;

    private final IThreadManager threadManager;

    private ServerSocketChannel serverSocketChannel;

    public ServerCommunicator(String name, int port, IThreadManager threadManager) {
        super();
        serverName = name;
        serverPort = port;
        this.threadManager = threadManager;
    }

    public final void executeInLoop() throws InterruptedException {
        try {
            SocketChannel socket = serverSocketChannel.accept();
            System.out.println(serverName + ": connected by client.");
            while (threadManager.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println(serverName + ": Wakeup");
                }
            }
            threadManager.assign(serverName, socket);
        } catch (ClosedByInterruptException e) {
            throw new InterruptedException(e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initOutOfLoop() {
        threadManager.stopWorkers();
        try {
            serverSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void endOutOfLoop() {
        threadManager.startWorkers();
        System.out.println(serverName + ": WorkerThreads started.");
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(serverPort));
            System.out.println(serverName + ": ServerSocket initialized.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
