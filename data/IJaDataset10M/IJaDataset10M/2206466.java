package com.wuala.loader2.copied.rmi;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import com.wuala.loader2.copied.client.messages.Ping;
import com.wuala.loader2.copied.client.messages.Pong;
import com.wuala.loader2.copied.rmi.BufferSizeEstimator.IBytable;

public abstract class PingPongClient extends Thread implements IPongDispatcherSource {

    private LinkedList<IBytable> pingQueue;

    protected LinkedList<IDispatcher> pongDispatchers;

    private ChannelManager channelManager;

    private boolean running;

    private short seqNum;

    public PingPongClient(String name) {
        super(name);
        seqNum = (short) System.nanoTime();
        channelManager = new ChannelManager();
        pingQueue = new LinkedList<IBytable>();
        pongDispatchers = new LinkedList<IDispatcher>();
        running = true;
        this.setDaemon(true);
        this.start();
    }

    public void disableRetryDelay() {
        channelManager.disableRetryDelay();
    }

    public abstract Pong createPong(ByteBuffer data);

    public boolean isRunning() {
        return running;
    }

    public boolean mightBeOnline() {
        return getProblem() == null;
    }

    protected void post(Ping ping, IDispatcher dispatcher) {
        CouldNotConnectException problem = getProblem();
        if (problem == null) {
            synchronized (pingQueue) {
                synchronized (pongDispatchers) {
                    ping.sequenceNumber = this.seqNum++;
                    pingQueue.add(ping);
                    pongDispatchers.add(dispatcher);
                }
            }
            synchronized (this) {
                this.notifyAll();
                channelManager.wakeup();
            }
        } else if (dispatcher != null) {
            dispatcher.handleConnectionProblem(problem);
        }
    }

    protected CouldNotConnectException getProblem() {
        return channelManager.getProblem();
    }

    public void run() {
        try {
            while (running) {
                synchronized (this) {
                    while (running && (!channelManager.hasWork(pongDispatchers))) {
                        wait(channelManager.getConnectionWait());
                        if (pingQueue.isEmpty()) {
                            channelManager.considerClose();
                        }
                    }
                }
                if (running) {
                    try {
                        sendAndReceive();
                    } catch (IOException e) {
                        handleConnectionProblem(e);
                    }
                }
            }
        } catch (InterruptedException e) {
            running = false;
        } finally {
            channelManager.close();
        }
        assert !running;
    }

    protected void sendAndReceive() throws IOException {
        if (!channelManager.hasConnection()) {
            openConnection(channelManager);
        }
        channelManager.doSomeWork();
    }

    protected abstract void openConnection(ChannelManager channelManager) throws CouldNotConnectException;

    protected void handleConnectionProblem(IOException e) {
        IDispatcher[] toBeNotified = null;
        synchronized (pingQueue) {
            synchronized (pongDispatchers) {
                toBeNotified = pongDispatchers.toArray(new IDispatcher[pongDispatchers.size()]);
                pingQueue.clear();
                pongDispatchers.clear();
                pongDispatchers.notifyAll();
            }
        }
        if (toBeNotified != null) {
            for (IDispatcher disp : toBeNotified) {
                if (disp != null) {
                    disp.handleConnectionProblem(e);
                }
            }
        }
        channelManager.close();
    }

    public void waitUntilAllProcessed() throws InterruptedException {
        synchronized (pongDispatchers) {
            while (hasMoreDispatchers()) {
                pongDispatchers.wait();
            }
        }
    }

    public synchronized void shutdown() {
        running = false;
        channelManager.shutdown();
        this.notifyAll();
    }

    public boolean hasMoreDispatchers() {
        return !pongDispatchers.isEmpty();
    }

    public IDispatcher consumeNextDispatcher(short seqNumber) {
        synchronized (pongDispatchers) {
            IDispatcher disp = pongDispatchers.removeFirst();
            if (pongDispatchers.isEmpty()) {
                pongDispatchers.notifyAll();
            }
            return disp;
        }
    }

    public int getOutgoingQueueSize() {
        synchronized (pingQueue) {
            return pingQueue.size();
        }
    }

    protected LinkedList<IBytable> getPings() {
        return pingQueue;
    }
}
