package jacky.lanlan.song.concurrent;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

/**
 * 有优先级的Semaphore。
 * <p>
 * 当acquire的数量耗尽后，后来的线程都将排队等待，FIFO，第一个等待的线程在acquire可用时将第一个
 * 申请到。
 */
public class FIFOSemaphore {

    private Semaphore semaphore;

    private BlockingQueue<Long> waitQueue = new LinkedBlockingQueue<Long>();

    private Map<Long, CountDownLatch> signals = new WeakHashMap<Long, CountDownLatch>();

    private Lock regLock = new ReentrantLock();

    private Lock unregLock = new ReentrantLock();

    public FIFOSemaphore(int initial) {
        semaphore = new Semaphore(initial);
    }

    /**
	 * 获得一个许可证。
	 * <p>
	 * <i>当没有许可证可用时，该方法将阻塞直到有许可证可用。</i>
	 * @throws InterruptedException 如果当前线程被打断
	 * @see Semaphore#acquire()
	 */
    public void acquire() throws InterruptedException {
        if (semaphore.tryAcquire()) return;
        regLock.lock();
        try {
            registerToQueue();
        } finally {
            regLock.unlock();
        }
        signals.get(Thread.currentThread().getId()).await();
        semaphore.acquire();
    }

    private void registerToQueue() {
        long id = Thread.currentThread().getId();
        waitQueue.add(id);
        if (signals.containsKey(id)) {
            signals.remove(id);
        }
        signals.put(id, new CountDownLatch(1));
    }

    private void notifyFirstWaiterAndUnreg() {
        long id = waitQueue.poll();
        CountDownLatch latch = signals.get(id);
        signals.remove(id);
        latch.countDown();
    }

    /**
	 * 释放许可证。
	 * @see Semaphore#release()
	 */
    public void release() {
        semaphore.release();
        unregLock.lock();
        try {
            if (isSomeOneWaiting()) {
                notifyFirstWaiterAndUnreg();
            }
        } finally {
            unregLock.unlock();
        }
    }

    /**
	 * 是否有线程在等待。
	 * <p>
	 * <i>注意，返回的是方法调用那一刻的等待情况，结果可能过期。</i>
	 */
    public boolean isSomeOneWaiting() {
        return !waitQueue.isEmpty();
    }

    /**
	 * 得到正在等待的线程ID列表。
	 * <p>
	 * <i>注意，返回的是方法调用那一刻的等待情况，列表可能过期。</i>
	 */
    public List<Long> getWaiterIDList() {
        List<Long> list = new ArrayList<Long>(waitQueue.size());
        for (long id : waitQueue) {
            list.add(id);
        }
        return list;
    }
}
