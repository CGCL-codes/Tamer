package xbird.util.concurrent.lock;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public interface IReadWriteLock {

    void readLock();

    void readUnlock();

    void writeLock();

    void writeUnlock();
}
