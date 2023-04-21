package com.dukesoftware.utils.net;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import com.dukesoftware.utils.thread.pattern.workerthread.ChannelWithThreadPool;
import com.dukesoftware.utils.thread.pattern.workerthread.IPutChannel;

public abstract class ThreadManagerAdapter<T> implements IThreadManager {

    private final ComThread[] threadPool;

    private final List<ComThread> freeThreads = new ArrayList<ComThread>();

    private final ChannelWithThreadPool<T> channel;

    public ThreadManagerAdapter(int queSize, int threadSize) {
        channel = new ChannelWithThreadPool<T>(queSize, threadSize);
        threadPool = new ComThread[threadSize];
    }

    public final void startWorkers() {
        createWorkers();
        channel.startWorkers();
        for (int i = 0; i < threadPool.length; i++) {
            threadPool[i].start();
        }
    }

    public final void stopWorkers() {
        for (int i = 0; i < threadPool.length; i++) {
            threadPool[i].shutdown();
        }
        channel.shutdown();
    }

    /**
	 */
    private void createWorkers() {
        for (int i = 0; i < threadPool.length; i++) {
            threadPool[i] = createCommunicationThread(channel);
        }
    }

    /**
	 * 
	 * @param channel
	 * @return
	 */
    protected abstract ComThread createCommunicationThread(IPutChannel channel);

    /**
	 * @return
	 */
    public final synchronized boolean isEmpty() {
        return freeThreads.isEmpty();
    }

    /**
	 * @param name
	 * @param socket
	 */
    public final synchronized void assign(String name, SocketChannel socket) {
        int pos = freeThreads.size() - 1;
        if (pos > 0) {
            freeThreads.remove(pos).assign(name, socket);
        }
    }

    @Override
    public synchronized <S extends ComThread> void addThread(S thread) {
        freeThreads.add(thread);
        notify();
    }
}
