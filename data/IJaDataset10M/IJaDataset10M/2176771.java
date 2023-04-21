package jshomeorg.simplytrain.toolset;

import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @todo maybe replace by java.util.concurrent
 * @author js
 */
public class LockedList<T> extends LinkedList<T> {

    ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

    public void readLock() {
        rwl.readLock().lock();
    }

    public void readUnlock() {
        rwl.readLock().unlock();
    }

    public void writeLock() {
        rwl.writeLock().lock();
    }

    public void writeUnlock() {
        rwl.writeLock().unlock();
    }

    public boolean isWriteLocked() {
        return rwl.isWriteLocked();
    }
}
