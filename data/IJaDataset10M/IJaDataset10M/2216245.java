package net.sf.sasl.distributed.lock;

/**
 * A lock session factory is the root to create and configure new lock sessions.
 * The implementation of the lock session factory should be thread safe. A lock
 * session factory must definitely not create sessions which are robust against
 * all sorts of evil code, for example creating two lock sessions for the same
 * thread and locking over them the same lock key.
 * 
 * @author Philipp Förmer
 * @since 0.0.1 (sasl-distributed-lock-library)
 */
public interface ILockSessionFactory {

    /**
	 * Creates and configures a fresh new lock session and returns it.
	 * 
	 * @return non null
	 * @since 0.0.1 (sasl-distributed-lock-library)
	 */
    public ILockSession openNewLockSession();

    /**
	 * Returns an active lock session for the current thread, if there exists
	 * one (a call to getActiveThreadSharedSession(true) was done before). If
	 * there exists no active lock session and allowCreate is true, than a new
	 * lock session will be created for the thread and returned. Else null will
	 * be returned.
	 * 
	 * @param allowCreate
	 *            true or false.
	 * @return null or non null
	 * @since 0.0.1 (sasl-distributed-lock-library)
	 */
    public ILockSession getActiveThreadSharedSession(boolean allowCreate);

    /**
	 * Releases the lock session factory. All critical acquired resources by the
	 * lock sessions, for example held lock sessions, should get released
	 * immediately.
	 * 
	 * @see org.springframework.beans.factory.DisposableBean#destroy()
	 * @since 0.0.1 (sasl-distributed-lock-library)
	 */
    public void release();
}
