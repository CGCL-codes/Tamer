package storage.tx.concurrency;

import java.util.*;
import storage.file.Block;
import storage.tx.concurrency.LockAbortException;

class LockTable {

    private static final long MAX_TIME = 10000;

    private Map<Block, Integer> locks = new HashMap<Block, Integer>();

    public synchronized void sLock(Block blk) {
        try {
            long timestamp = System.currentTimeMillis();
            while (hasXlock(blk) && !waitingTooLong(timestamp)) wait(MAX_TIME);
            if (hasXlock(blk)) throw new LockAbortException();
            int val = getLockVal(blk);
            locks.put(blk, val + 1);
        } catch (InterruptedException e) {
            throw new LockAbortException();
        }
    }

    synchronized void xLock(Block blk) {
        try {
            long timestamp = System.currentTimeMillis();
            while (hasOtherSLocks(blk) && !waitingTooLong(timestamp)) wait(MAX_TIME);
            if (hasOtherSLocks(blk)) throw new LockAbortException();
            locks.put(blk, -1);
        } catch (InterruptedException e) {
            throw new LockAbortException();
        }
    }

    synchronized void unlock(Block blk) {
        int val = getLockVal(blk);
        if (val > 1) locks.put(blk, val - 1); else {
            locks.remove(blk);
            notifyAll();
        }
    }

    private boolean hasXlock(Block blk) {
        return getLockVal(blk) < 0;
    }

    private boolean hasOtherSLocks(Block blk) {
        return getLockVal(blk) > 1;
    }

    private boolean waitingTooLong(long starttime) {
        return System.currentTimeMillis() - starttime > MAX_TIME;
    }

    private int getLockVal(Block blk) {
        Integer ival = locks.get(blk);
        return (ival == null) ? 0 : ival.intValue();
    }
}
