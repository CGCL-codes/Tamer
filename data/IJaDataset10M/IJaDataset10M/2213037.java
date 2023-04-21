package com.belmont.backup;

import java.util.*;

public class Producer implements IEventProducer, Runnable, IBackupConstants {

    static final int MAX_POOL_SIZE = 300;

    static Producer sharedInstance;

    Thread msgThread;

    Vector<IEventObserver> observers = new Vector<IEventObserver>();

    Vector<ProducerMessage> messageQueue = new Vector<ProducerMessage>();

    Vector<ProducerMessage> messagePool = new Vector<ProducerMessage>();

    static class ProducerMessage {

        Object sender;

        int msg;

        Object arg;

        ProducerMessage(Object sender, int msg, Object arg) {
            this.sender = sender;
            this.msg = msg;
            this.arg = arg;
        }
    }

    public static synchronized Producer getInstance() {
        if (sharedInstance == null) {
            sharedInstance = new Producer();
            sharedInstance.start();
        }
        return sharedInstance;
    }

    public synchronized void start() {
        if (msgThread == null) {
            msgThread = new Thread(this);
            msgThread.start();
        }
    }

    public synchronized void stop() {
        if (msgThread != null) {
            msgThread.interrupt();
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
            msgThread = null;
        }
    }

    public synchronized void removeObserver(IEventObserver client) {
        observers.removeElement(client);
    }

    public synchronized void addObserver(IEventObserver client, int start, int end) {
        removeObserver(client);
        observers.addElement(client);
    }

    public synchronized void sendNotify(Object sender, int msg, Object arg) {
        ProducerMessage m = null;
        int s = messagePool.size();
        if (s > 0) {
            m = messagePool.elementAt(0);
            messagePool.removeElementAt(0);
            m.sender = sender;
            m.msg = msg;
            m.arg = arg;
        } else {
            m = new ProducerMessage(sender, msg, arg);
        }
        messageQueue.addElement(m);
        notifyAll();
    }

    void messageLoop() throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Iterator<ProducerMessage> msgs = null;
                Vector<ProducerMessage> localq = null;
                synchronized (this) {
                    while (messageQueue.size() == 0) {
                        wait();
                    }
                    localq = messageQueue;
                    messageQueue = new Vector<ProducerMessage>();
                }
                msgs = localq.iterator();
                while (msgs.hasNext()) {
                    ProducerMessage msg = msgs.next();
                    Iterator<IEventObserver> clients = observers.iterator();
                    while (clients.hasNext()) {
                        IEventObserver observer = clients.next();
                        try {
                            observer.notify(msg.sender, msg.msg, msg.arg);
                        } catch (Throwable ex) {
                            Utils.log(LOG_ERROR, "Exception in observer notify ", ex);
                        }
                    }
                }
                synchronized (this) {
                    int s = messagePool.size();
                    if (s < MAX_POOL_SIZE) {
                        s = MAX_POOL_SIZE - s;
                        int l = localq.size();
                        int i;
                        for (i = 0; i < s && i < l; i++) {
                            messagePool.addElement(localq.elementAt(i));
                        }
                    }
                }
            } catch (Exception ex) {
                Utils.log(LOG_ERROR, "Message loop error ", ex);
            }
        }
    }

    public void run() {
        try {
            messageLoop();
        } catch (InterruptedException ex) {
        }
    }
}
