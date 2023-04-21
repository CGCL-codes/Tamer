package net.sf.pando.fipa_console;

import java.util.Queue;
import net.sf.pando.common.MessageEnvelope;

public class ConsIncomingMessageThread implements Runnable {

    private boolean stopped;

    private Thread thread;

    private Queue<MessageEnvelope> incomingQueue;

    private ConsWriter consWriter;

    public ConsIncomingMessageThread(ConsWriter consWriter, Queue<MessageEnvelope> incomingQueue) {
        this.consWriter = consWriter;
        stopped = true;
        this.incomingQueue = incomingQueue;
    }

    public synchronized void start() {
        thread = new Thread(this, "start incoming messages processing thread");
        thread.start();
        stopped = false;
    }

    public synchronized void stop() {
        thread.interrupt();
        try {
            while (!stopped) {
                wait(1000);
            }
        } catch (InterruptedException e) {
        }
        thread = null;
    }

    public synchronized void run() {
        synchronized (this) {
            try {
                while (true) {
                    Thread.sleep(2500);
                    while (incomingQueue.size() > 0) {
                        MessageEnvelope local = incomingQueue.remove();
                        consWriter.write("\n\n" + "------------------ Message ------------------" + "\n" + "From an Agent: " + local.getFrom() + "\n" + "Content: " + "\n" + local.getAcl_representation() + "\n");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean isStopped() {
        return stopped;
    }
}
