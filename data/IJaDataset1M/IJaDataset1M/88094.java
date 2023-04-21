package udpInvoker.main;

import udpInvoker.util.UdpUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: esr
 * Date: 13.okt.2008
 * Time: 14:18:41
 * Copyright 2008  Espen Skjervold, FFI
 */
public class UdpMulticastReceiver implements Runnable {

    private static UdpMulticastReceiver instance;

    private HashMap<MessageSubscriber, Object> subscribers;

    private UdpMulticastReceiverThread udpMulticastReceiverThread;

    public static UdpMulticastReceiver getInstance() {
        if (instance == null) instance = new UdpMulticastReceiver();
        return instance;
    }

    private UdpMulticastReceiver() {
        udpMulticastReceiverThread = new UdpMulticastReceiverThread();
        udpMulticastReceiverThread.start();
        subscribers = new HashMap<MessageSubscriber, Object>();
        Thread subscriptionThread = new Thread(this);
        subscriptionThread.start();
    }

    public void setUnicastAddress(String address, int port) {
        udpMulticastReceiverThread.setUnicastAddress(address, port);
    }

    public synchronized void subscribe(MessageSubscriber messageSubscriber) {
        if (!subscribers.containsKey(messageSubscriber)) subscribers.put(messageSubscriber, null);
    }

    public void run() {
        while (true) {
            try {
                byte[] buffer = udpMulticastReceiverThread.getMessageBuffer();
                HashMap subs = (HashMap) subscribers.clone();
                Iterator iterator = subs.keySet().iterator();
                while (iterator.hasNext()) {
                    MessageSubscriber subscriber = (MessageSubscriber) iterator.next();
                    subscriber.addMessageToQue(buffer);
                }
                Thread.yield();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
