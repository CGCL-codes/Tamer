package com.dukesoftware.utils.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import com.dukesoftware.utils.net.deprecated.SendAndRecieveTask;

/**
 * 2005/10/5	
 * MultiClientServer Program for java.
 * 
 * 
 *
 */
public class MultiThreadServer implements Runnable {

    private final int CORE_THREADS = 10;

    private ServerSocket serverSocket;

    private int threadCount = 0;

    private final List<WorkerThread> freeThreads = new ArrayList<WorkerThread>();

    private final int port;

    private final int max_thread;

    private volatile boolean shutdown;

    public MultiThreadServer(int port, int max_threads) {
        this.port = port;
        this.max_thread = max_threads;
    }

    private void startThread() {
        WorkerThread worker = new WorkerThread(this, SendAndRecieveTask.getInstance());
        threadCount++;
        worker.start();
    }

    public synchronized void run() {
        while (threadCount < CORE_THREADS) {
            startThread();
        }
        try {
            serverSocket = new ServerSocket(port);
            System.out.println(serverSocket.toString());
            while (!shutdown) {
                Socket socket = serverSocket.accept();
                System.out.println(serverSocket.getLocalSocketAddress());
                while (freeThreads.isEmpty()) {
                    if (threadCount < max_thread) {
                        startThread();
                    }
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                    }
                }
                if (!shutdown) {
                    (freeThreads.remove(freeThreads.size() - 1)).assign(socket);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            finalProcess();
        }
    }

    private void finalProcess() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        shutdown = true;
        notifyAll();
    }

    public void addFreeThread(WorkerThread t) {
        synchronized (this) {
            freeThreads.add(t);
        }
        notify();
    }
}
