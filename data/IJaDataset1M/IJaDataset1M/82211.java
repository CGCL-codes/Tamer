package org.ocd.msgbus;

import java.util.*;

public class MsgBusProcessor {

    protected Vector msgPool;

    protected ProcessorThread thread;

    public MsgBusProcessor() {
        msgPool = new Vector();
        thread = new ProcessorThread();
        thread.start();
    }

    /**
   * Add Message to the Pool to be delivered
   */
    public synchronized void addMessage(MsgBusTopic topic, MsgBusMessage msg, IMsgBusListener resultListener) {
        msgPool.addElement(new Bundle(topic, msg, resultListener));
        notify();
    }

    /**
   * Returns the next Bundle to Deliver
   * This method blocks until a Bundle is ready
   * @return bundle
   */
    protected synchronized Bundle getNextBundle() {
        try {
            if (msgPool.size() == 0) wait();
        } catch (InterruptedException _exp) {
            _exp.printStackTrace();
        }
        Bundle _next = (Bundle) msgPool.firstElement();
        msgPool.removeElementAt(0);
        return _next;
    }

    /**
   * Inner class used to bundle a Topic and a Msg together
   */
    private class Bundle {

        MsgBusMessage message = null;

        MsgBusTopic topic = null;

        IMsgBusListener resultListener = null;

        public Bundle(MsgBusTopic topic, MsgBusMessage msg, IMsgBusListener resultListener) {
            message = msg;
            this.topic = topic;
            this.resultListener = resultListener;
        }
    }

    /**
   * Inner Class used as the Thread to process the msg Pool
   *
   */
    private class ProcessorThread extends Thread {

        /**
     * The run method of this thread processes all messages waiting to be delivered
     */
        public void run() {
            while (true) {
                try {
                    Bundle _next = getNextBundle();
                    _next.topic.fire(_next.message, _next.resultListener, false);
                } catch (Throwable _exp) {
                    _exp.printStackTrace();
                }
            }
        }
    }
}
