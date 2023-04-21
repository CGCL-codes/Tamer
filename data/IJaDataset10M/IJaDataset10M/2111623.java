package game.evolution.treeEvolution.evolutionControl;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Semaphore;

public class ElapsedTime {

    protected ThreadMXBean threadManagement;

    protected long threadId;

    protected long computationTimeMs;

    protected Semaphore lock;

    public ElapsedTime() {
        threadId = Thread.currentThread().getId();
        threadManagement = ManagementFactory.getThreadMXBean();
        lock = new Semaphore(1);
    }

    public long getTimeS() {
        return computationTimeMs / 1000 + threadManagement.getThreadCpuTime(threadId) / 1000000000;
    }

    public void addToElapsedTimeMs(long computationTimeMs) {
        lock.acquireUninterruptibly();
        this.computationTimeMs += computationTimeMs;
        lock.release();
    }
}
