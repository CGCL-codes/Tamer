package com.orientechnologies.tools.oexplorer;

import java.io.*;

public class CommandOutputConsumer extends Thread {

    public CommandOutputConsumer() {
        start();
    }

    public void run() {
        InputStream in = null;
        int b = -1;
        while (true) {
            try {
                synchronized (lock) {
                    lock.wait();
                    in = proc.getInputStream();
                    while (proc != null) {
                        synchronized (CommandOutputConsumer.class) {
                            while (true) {
                                b = in.read();
                                if (b > -1) MainFrame.getInstance().appendLog(String.valueOf((char) b)); else break;
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public void startConsume(Process iProcess) {
        proc = iProcess;
        synchronized (lock) {
            lock.notify();
        }
    }

    public void stopConsume() {
        synchronized (CommandOutputConsumer.class) {
            proc = null;
        }
    }

    public static CommandOutputConsumer getInstance() {
        if (_instance == null) synchronized (CommandOutputConsumer.class) {
            if (_instance == null) _instance = new CommandOutputConsumer();
        }
        return _instance;
    }

    private Process proc;

    private Object lock = new Object();

    private static CommandOutputConsumer _instance = null;
}
