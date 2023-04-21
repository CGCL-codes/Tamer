package xbird.util.concurrent.lock;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 * @link http://gee.cs.oswego.edu/dl/cpj/
 * @link http://usbing.com/books/concurrentprogramminginjava-designprinciplesandpatterns/
 */
public final class VolatileBackoffLock implements ILock {

    private final int _spinsBeforeYield;

    private final int _spinsBeforeSleep;

    private final long _initSleepTime;

    private volatile boolean busy = false;

    public VolatileBackoffLock() {
        this(100, 200, 1);
    }

    public VolatileBackoffLock(int spinsBeforeYield, int spinsBeforeSleep, long initSleepTime) {
        this._spinsBeforeSleep = spinsBeforeSleep;
        this._spinsBeforeYield = spinsBeforeYield;
        this._initSleepTime = initSleepTime;
    }

    public void lock() {
        final int spinsBeforeYield = _spinsBeforeYield;
        final int spinsBeforeSleep = _spinsBeforeSleep;
        long sleepTime = _initSleepTime;
        int spins = 0;
        while (true) {
            if (!busy) {
                synchronized (this) {
                    if (!busy) {
                        busy = true;
                        return;
                    }
                }
            }
            if (spins < spinsBeforeYield) {
                ++spins;
            } else if (spins < spinsBeforeSleep) {
                ++spins;
                Thread.yield();
            } else {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
                sleepTime = (3 * sleepTime) >> 1 + 1;
            }
        }
    }

    public boolean tryLock() {
        if (!busy) {
            synchronized (this) {
                if (!busy) {
                    busy = true;
                    return true;
                }
            }
        }
        return false;
    }

    public void unlock() {
        this.busy = false;
    }

    public boolean isLocked() {
        return busy;
    }
}
