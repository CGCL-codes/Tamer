package com.shimari.bot;

import java.util.Collection;
import java.io.*;
import com.shimari.framework.Registry;
import com.shimari.util.LineReader;

/**
 * ConsoleConnection interacts on the command line; it's useful
 * for testing the Bot without connecting to an actual IRC server.
 */
public class ConsoleConnection implements Connection, Runnable {

    Thread consoleThread = null;

    Collection messageQueue = null;

    /**
     * Initialize this component
     */
    public void init(Registry components) {
    }

    /**
     * Return true if the console thread is alive
     */
    public boolean isConnected() {
        return (consoleThread != null && consoleThread.isAlive());
    }

    /**
     * Print a message to the console
     */
    public void sendMessage(String target, String message) {
        System.out.println(target + ": " + message);
    }

    /**
     * Print a notice to the console
     */
    public void broadcastMessage(String message) {
        System.out.println("*** " + message + " ***");
    }

    /**
     * Print a notice to the console
     */
    public void broadcastNotice(String message) {
        System.out.println("--- " + message + " ---");
    }

    /**
     * Start the console thread
     */
    public void connect() throws java.io.IOException {
        consoleThread = new Thread(this);
        consoleThread.start();
    }

    /**
     * Stop the console thread
     */
    public void disconnect() {
        consoleThread.interrupt();
        consoleThread = null;
    }

    /** Set the message queue */
    public void setMessageQueue(java.util.Collection queue) {
        messageQueue = queue;
    }

    /**
     * Read messages from the console
     */
    public void run() {
        LineReader in = new LineReader(new InputStreamReader(System.in));
        System.out.println("Console enabled");
        try {
            while (!Thread.currentThread().isInterrupted()) {
                ConsoleMessage m = new ConsoleMessage(in.readLine(), this);
                synchronized (messageQueue) {
                    messageQueue.add(m);
                    messageQueue.notify();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.out.println("Console disabled");
    }
}
