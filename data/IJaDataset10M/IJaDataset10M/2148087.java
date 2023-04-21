package com.logica.smpp.util;

import com.logica.smpp.OutbindEvent;
import com.logica.smpp.OutbindEventListener;
import com.logica.smpp.SmppObject;

/**
 * The SimpleOutbindListener class is example class for <tt>OutbindEventListener</tt>.
 * It stores all <tt>OutbindEvent</tt> passed to it's <tt>handleOutbind</tt>
 * method to <tt>Queue</tt> and notifies thread waiting on <tt>signaller</tt> object.<p>
 * The typical usage would be:<p>
 * <blockquote><pre>
 * Connection conn = new TCPIPConnection("123.123.123.123",1234);
 * OutbindReceiver rcvr = new OutbindReceiver(conn);
 * // <tt>this</tt> as parameter means that the listener will notify
 * // the calling thread
 * SimpleOutbindListener listener = new SimpleOutbindListener(this);
 * OutbindEvent event;
 * rcvr.setOutbindListener(listener);
 * rcvr.startReceiving();
 * while (true) {
 *    listener.waitOutbind(1000);
 *    event = listener.getOutbindEvent();
 *    if (event != null) {
 *       // process the event
 *    }
 * }
 * </pre></blockquote>
 *
 * @author Logica Mobile Networks SMPP Open Source Team
 * @version 1.0, 11 Jun 2001
 */
public class SimpleOutbindListener extends SmppObject implements OutbindEventListener {

    private Queue eventQueue = new Queue();

    private Object signaller = null;

    public SimpleOutbindListener() {
        signaller = this;
    }

    public SimpleOutbindListener(Object signaller) {
        this.signaller = signaller;
    }

    public void handleOutbind(OutbindEvent outbind) {
        eventQueue.enqueue(outbind);
        signaller.notify();
    }

    public OutbindEvent getOutbindEvent() {
        if (!eventQueue.isEmpty()) {
            return (OutbindEvent) eventQueue.dequeue();
        } else {
            return null;
        }
    }

    public void waitOutbind() throws InterruptedException {
        signaller.wait();
    }

    public void waitOutbind(long timeout) throws InterruptedException {
        signaller.wait(timeout);
    }

    public void waitOutbind(long timeout, int nanos) throws InterruptedException {
        signaller.wait(timeout, nanos);
    }
}
