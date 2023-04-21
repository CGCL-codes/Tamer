package xbird.util.concurrent.lock;

/**
 * 
 * <DIV lang="en">
 * do {
 *    seq = readBegin();
 *    ..
 * } while(readRetry(seq));
 * </DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public final class SeqLock {

    private int counter;

    private volatile int mfence;

    private final ILock spin;

    public SeqLock() {
        this.counter = 0;
        this.spin = new AtomicBackoffLock();
        this.mfence = 0;
    }

    @SuppressWarnings("unused")
    public int readBegin() {
        int ret = counter;
        int lfence = mfence;
        return ret;
    }

    @SuppressWarnings("unused")
    public boolean readRetry(int v) {
        int lfence = mfence;
        return (v & 1) == 1 || counter != v;
    }

    public void writeLock() {
        spin.lock();
        ++counter;
        mfence = 0;
    }

    public void writeUnlock() {
        mfence = 0;
        counter++;
        spin.unlock();
    }

    /**
     * assumes only one writer
     */
    public void writeBegin() {
        ++counter;
        mfence = 0;
    }

    /**
     * assumes only one writer
     */
    public void writeEnd() {
        mfence = 0;
        counter++;
    }
}
