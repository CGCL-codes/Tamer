package org.localstorm.comet;

import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
import org.mortbay.util.ajax.Continuation;

public class ContinuationsManager {

    private final ConcurrentSkipListMap<Long, Continuation> pending;

    private final WriteLock resumeLock;

    private final ReadLock forbidLock;

    public ContinuationsManager() {
        pending = new ConcurrentSkipListMap<Long, Continuation>();
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        resumeLock = rwLock.writeLock();
        forbidLock = rwLock.readLock();
    }

    public void register(Long id, Continuation cnt) {
        Continuation old = pending.put(id, cnt);
        if (old != null) {
            old.resume();
        }
    }

    public void unregister(Long id) {
        Continuation junk = pending.remove(id);
        if (junk != null) {
            junk.resume();
        }
    }

    public void resumeForSourceName(SourceName name) {
        try {
            resumeLock.lock();
            for (Continuation c : pending.values()) {
                Subscription subs = (Subscription) c.getObject();
                if (subs.contains(name)) {
                    c.resume();
                }
            }
        } finally {
            resumeLock.unlock();
        }
    }

    public void forbidResuming() {
        forbidLock.lock();
    }

    public void allowResuming() {
        forbidLock.unlock();
    }
}
