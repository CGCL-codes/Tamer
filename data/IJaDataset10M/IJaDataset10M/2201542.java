package com.daffodilwoods.daffodildb.server.datasystem.utility;

import java.util.*;

public class ReadWriteLocker implements ReadWriteLock {

    boolean tableLocked = false;

    TableLock tableLock = new TableLock();

    Map map = Collections.synchronizedMap(new HashMap());

    Object monitor = new Object();

    private class TableLock {

        Object owner;

        int waitingThreads = 0;

        synchronized void incrementWaiters() {
            ++waitingThreads;
        }

        synchronized void decrementWaitersToZero() {
            waitingThreads = 0;
        }

        synchronized int waitersCount() {
            return waitingThreads;
        }
    }

    private class RowLock {

        Object owner;

        int readersCount;

        private int waitingThreads;

        synchronized void increment() {
            ++readersCount;
        }

        synchronized void decrement() {
            --readersCount;
        }

        synchronized int readersCount() {
            return readersCount;
        }

        synchronized void incrementWaiters() {
            ++waitingThreads;
        }

        synchronized void decrementWaitersToZero() {
            waitingThreads = 0;
        }

        synchronized int waitingThreads() {
            return waitingThreads;
        }
    }

    public ReadWriteLocker() {
    }

    private void waitOnTableLock() {
        synchronized (tableLock) {
            try {
                tableLock.wait(5);
            } catch (InterruptedException ie) {
            }
        }
    }

    private void notifyOnTableLock() {
        synchronized (tableLock) {
            tableLock.notifyAll();
        }
    }

    private void waitOnRowLock(RowLock rw) {
        synchronized (rw) {
            try {
                rw.wait(5);
            } catch (InterruptedException ie) {
            }
        }
    }

    private void notifyOnRowLock(RowLock rw) {
        synchronized (rw) {
            rw.notifyAll();
        }
    }

    public void lockTable() {
        for (; ; ) {
            if (tableLocked) {
                waitOnTableLock();
            } else if (map.size() > 0) {
                tableLock.incrementWaiters();
                waitOnTableLock();
            } else {
                synchronized (monitor) {
                    if (map.size() == 0) {
                        tableLocked = true;
                        tableLock.decrementWaitersToZero();
                        tableLock.owner = Thread.currentThread();
                        return;
                    }
                }
            }
        }
    }

    public void releaseTable() {
        synchronized (monitor) {
            tableLock.owner = null;
            tableLocked = false;
            notifyOnTableLock();
        }
    }

    public void lockRowForRead(Object rowIdentity) {
        for (; ; ) {
            RowLock rw = (RowLock) map.get(rowIdentity);
            if (rw != null) {
                if (rw.owner == Thread.currentThread()) {
                    synchronized (monitor) {
                        rw.increment();
                        map.put(rowIdentity, rw);
                        return;
                    }
                } else if (tableLock.waitersCount() > 0) {
                    waitOnTableLock();
                } else if (rw.owner == null && rw.waitingThreads() == 0) {
                    synchronized (monitor) {
                        if (tableLocked == false) {
                            rw = (RowLock) map.get(rowIdentity);
                            rw = rw == null ? new RowLock() : rw;
                            if (rw != null && rw.owner == null && rw.waitingThreads() == 0) {
                                rw.increment();
                                map.put(rowIdentity, rw);
                                return;
                            }
                        }
                    }
                } else {
                    rw.incrementWaiters();
                    waitOnRowLock(rw);
                }
            } else if (tableLocked) {
                waitOnTableLock();
            } else if (tableLock.waitersCount() > 0) {
                waitOnTableLock();
            } else {
                synchronized (monitor) {
                    if (tableLocked == false) {
                        rw = (RowLock) map.get(rowIdentity);
                        rw = rw == null ? new RowLock() : rw;
                        if (rw.owner == null) {
                            rw.increment();
                            map.put(rowIdentity, rw);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void releaseRowForRead(Object rowIdentity) {
        synchronized (monitor) {
            RowLock rw = (RowLock) map.get(rowIdentity);
            rw.decrement();
            if (rw.readersCount() == 0) {
                map.remove(rowIdentity);
                if (tableLock.waitersCount() > 0) notifyOnTableLock();
                if (rw.waitingThreads() > 0) notifyOnRowLock(rw);
            }
        }
    }

    public void lockRowForWrite(Object rowIdentity) {
        for (; ; ) {
            RowLock rw = (RowLock) map.get(rowIdentity);
            if (rw != null) {
                if (tableLock.waitersCount() > 0) {
                    waitOnTableLock();
                } else {
                    rw.incrementWaiters();
                    waitOnRowLock(rw);
                }
            } else if (tableLocked) {
                waitOnTableLock();
            } else if (tableLock.waitersCount() > 0) {
                waitOnTableLock();
            } else {
                synchronized (monitor) {
                    if (tableLocked == false) {
                        rw = (RowLock) map.get(rowIdentity);
                        rw = rw == null ? new RowLock() : rw;
                        if (rw.readersCount() == 0) {
                            rw.increment();
                            rw.owner = Thread.currentThread();
                            map.put(rowIdentity, rw);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void releaseRowForWrite(Object rowIdentity) {
        synchronized (monitor) {
            RowLock rw = (RowLock) map.get(rowIdentity);
            rw.decrement();
            map.remove(rowIdentity);
            if (tableLock.waitersCount() > 0) {
                notifyOnTableLock();
            }
            if (rw.waitingThreads() > 0) notifyOnRowLock(rw);
        }
    }
}
