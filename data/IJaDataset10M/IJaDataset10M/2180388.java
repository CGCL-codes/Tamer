package net.sf.sasl.distributed.lock;

/**
 * Interface defines methods which must be offered by a lock container. What a
 * lock container is concrete, depends on the implementation, but normally it
 * should be some collection of acquired locks.
 * 
 * @author Philipp Förmer
 * @since 0.0.1 (sasl-distributed-lock-library)
 */
public interface ILockContainer {

    /**
	 * Releases all locks that this lock container holds. If the container has
	 * nested lock containers than they will be released via {@link #release()}.
	 * The container stays active after the operation.
	 * 
	 * @throws LockOperationException
	 *             if an exception occurs during releasing locks and nested
	 *             containers.
	 * @since 0.0.1 (sasl-distributed-lock-library)
	 */
    public void clear() throws LockOperationException;

    /**
	 * Releases all resources associated with this lock container. If the
	 * container has got nested containers, than all nested containers of the
	 * container must get released and all locks, which are held by the
	 * container.<br>
	 * After the operation the lock element should be disconnected from all
	 * types of lock operations, which means it is inactive and garbage.
	 * 
	 * @throws LockOperationException
	 * @since 0.0.1 (sasl-distributed-lock-library)
	 */
    public void release() throws LockOperationException;

    /**
	 * Returns true if this lock container is active which means it was not yet
	 * released via {@link #release()}. False else. An inactive lock container
	 * should be disconnected from all types of lock operations.
	 * 
	 * @return true or false.
	 * @since 0.0.1 (sasl-distributed-lock-library)
	 */
    public boolean isActive();

    /**
	 * Returns a name of the lock container, for example "lock-transaction-X".
	 * 
	 * @return null or non null
	 * @since 0.0.1 (sasl-distributed-lock-library)
	 */
    public String getName();
}
