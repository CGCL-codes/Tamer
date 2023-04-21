package java.util.concurrent.locks;

/**
 * A minimalist <code>java.util.concurrent.ReadWriteLock</code> interface
 * to support the Reactive4Java's concurrency dependant operators.
 * Lock in GWT is basically a NO-OP construct.
 * @author akarnokd, 2011.03.21.
 */
public interface ReadWriteLock {

    /** @return the read lock. */
    Lock readLock();

    /** @return the write lock. */
    Lock writeLock();
}
