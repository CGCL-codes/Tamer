package org.eaiframework.impl.jdk;

import java.util.Queue;
import org.eaiframework.EAIException;
import org.eaiframework.LifecycleEnum;
import org.eaiframework.Message;
import org.eaiframework.support.AbstractMessageConsumer;

public class JdkMessageConsumer extends AbstractMessageConsumer {

    private Queue<Message> messageQueue;

    private MessageConsumerThread thread;

    public JdkMessageConsumer() {
    }

    public void init() throws EAIException {
        state = LifecycleEnum.CREATED;
    }

    public void start() throws EAIException {
        if (LifecycleEnum.STARTED.equals(state)) {
            return;
        }
        if (messageQueue == null) {
            messageQueue = JdkEndpointRegistry.getInstance().get(this.endpoint);
        }
        synchronized (messageQueue) {
            state = LifecycleEnum.STARTED;
        }
        thread = new MessageConsumerThread();
        thread.start();
    }

    public void stop() throws EAIException {
        if (!state.equals(LifecycleEnum.STARTED)) {
            return;
        }
        synchronized (messageQueue) {
            state = LifecycleEnum.STOPPED;
            messageQueue.notify();
        }
    }

    public void destroy() throws EAIException {
        state = LifecycleEnum.DESTROYED;
    }

    private class MessageConsumerThread extends Thread {

        public void run() {
            while (state.equals(LifecycleEnum.STARTED)) {
                try {
                    synchronized (messageQueue) {
                        if (messageQueue.isEmpty()) {
                            messageQueue.wait();
                        } else {
                            while (!messageQueue.isEmpty()) {
                                try {
                                    notifyListeners(messageQueue.poll());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                }
                if (pollingInterval != -1) {
                    try {
                        Thread.sleep(pollingInterval);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
