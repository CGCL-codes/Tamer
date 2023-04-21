package com.adam.framework.jdbc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.adam.framework.util.OrderComparator;
import com.adam.framework.util.Ordered;

/**
 * Central helper that manages resources and transaction synchronizations per thread.
 * To be used by resource management code but not by typical application code.
 *
 * <p>Supports one resource per key without overwriting, that is, a resource needs
 * to be removed before a new one can be set for the same key.
 * Supports a list of transaction synchronizations if synchronization is active.
 *
 * <p>Resource management code should check for thread-bound resources, e.g. JDBC
 * Connections or Hibernate Sessions, via <code>getResource</code>. Such code is
 * normally not supposed to bind resources to threads, as this is the responsibility
 * of transaction managers. A further option is to lazily bind on first use if
 * transaction synchronization is active, for performing transactions that span
 * an arbitrary number of resources.
 *
 * <p>Transaction synchronization must be activated and deactivated by a transaction
 * manager via {@link #initSynchronization()} and {@link #clearSynchronization()}.
 * This is automatically supported by {@link AbstractPlatformTransactionManager},
 * and thus by all standard Spring transaction managers, such as
 * {@link org.springframework.transaction.jta.JtaTransactionManager} and
 * {@link org.springframework.jdbc.datasource.DataSourceTransactionManager}.
 *
 * <p>Resource management code should only register synchronizations when this
 * manager is active, which can be checked via {@link #isSynchronizationActive};
 * it should perform immediate resource cleanup else. If transaction synchronization
 * isn't active, there is either no current transaction, or the transaction manager
 * doesn't support transaction synchronization.
 *
 * <p>Synchronization is for example used to always return the same resources
 * within a JTA transaction, e.g. a JDBC Connection or a Hibernate Session for
 * any given DataSource or SessionFactory, respectively.
 *
 * @author Juergen Hoeller
 * @since 02.06.2003
 * @see #isSynchronizationActive
 * @see #registerSynchronization
 * @see TransactionSynchronization
 * @see AbstractPlatformTransactionManager#setTransactionSynchronization
 * @see org.springframework.transaction.jta.JtaTransactionManager
 * @see org.springframework.jdbc.datasource.DataSourceTransactionManager
 * @see org.springframework.jdbc.datasource.DataSourceUtils#getConnection
 */
public abstract class TransactionSynchronizationManager {

    private static final Log logger = LogFactory.getLog(TransactionSynchronizationManager.class);

    private static final ThreadLocal resources = new ThreadLocal();

    private static final ThreadLocal synchronizations = new ThreadLocal();

    private static final Comparator synchronizationComparator = new OrderComparator();

    private static final ThreadLocal currentTransactionName = new ThreadLocal();

    private static final ThreadLocal currentTransactionReadOnly = new ThreadLocal();

    private static final ThreadLocal currentTransactionIsolationLevel = new ThreadLocal();

    private static final ThreadLocal actualTransactionActive = new ThreadLocal();

    /**
	 * Return all resources that are bound to the current thread.
	 * <p>Mainly for debugging purposes. Resource managers should always invoke
	 * <code>hasResource</code> for a specific resource key that they are interested in.
	 * @return a Map with resource keys (usually the resource factory) and resource
	 * values (usually the active resource object), or an empty Map if there are
	 * currently no resources bound
	 * @see #hasResource
	 */
    public static Map getResourceMap() {
        Map map = (Map) resources.get();
        return (map != null ? Collections.unmodifiableMap(map) : Collections.EMPTY_MAP);
    }

    /**
	 * Check if there is a resource for the given key bound to the current thread.
	 * @param key the key to check (usually the resource factory)
	 * @return if there is a value bound to the current thread
	 * @see ResourceTransactionManager#getResourceFactory() 
	 */
    public static boolean hasResource(Object key) {
        Assert.notNull(key, "Key must not be null");
        Map map = (Map) resources.get();
        return (map != null && map.containsKey(key));
    }

    /**
	 * Retrieve a resource for the given key that is bound to the current thread.
	 * @param key the key to check (usually the resource factory)
	 * @return a value bound to the current thread (usually the active
	 * resource object), or <code>null</code> if none
	 * @see ResourceTransactionManager#getResourceFactory()
	 */
    public static Object getResource(Object key) {
        Assert.notNull(key, "Key must not be null");
        Map map = (Map) resources.get();
        if (map == null) {
            return null;
        }
        Object value = map.get(key);
        if (value != null && logger.isDebugEnabled()) {
            logger.debug("Retrieved value [" + value + "] for key [" + key + "] bound to thread [" + Thread.currentThread().getName() + "]");
        }
        return value;
    }

    /**
	 * Bind the given resource for the given key to the current thread.
	 * @param key the key to bind the value to (usually the resource factory)
	 * @param value the value to bind (usually the active resource object)
	 * @throws IllegalStateException if there is already a value bound to the thread
	 * @see ResourceTransactionManager#getResourceFactory()
	 */
    public static void bindResource(Object key, Object value) throws IllegalStateException {
        Assert.notNull(key, "Key must not be null");
        Assert.notNull(value, "Value must not be null");
        Map map = (Map) resources.get();
        if (map == null) {
            map = new HashMap();
            resources.set(map);
        }
        if (map.containsKey(key)) {
            throw new IllegalStateException("Already value [" + map.get(key) + "] for key [" + key + "] bound to thread [" + Thread.currentThread().getName() + "]");
        }
        map.put(key, value);
        if (logger.isDebugEnabled()) {
            logger.debug("Bound value [" + value + "] for key [" + key + "] to thread [" + Thread.currentThread().getName() + "]");
        }
    }

    /**
	 * Unbind a resource for the given key from the current thread.
	 * @param key the key to unbind (usually the resource factory)
	 * @return the previously bound value (usually the active resource object)
	 * @throws IllegalStateException if there is no value bound to the thread
	 * @see ResourceTransactionManager#getResourceFactory()
	 */
    public static Object unbindResource(Object key) throws IllegalStateException {
        Assert.notNull(key, "Key must not be null");
        Map map = (Map) resources.get();
        if (map == null || !map.containsKey(key)) {
            throw new IllegalStateException("No value for key [" + key + "] bound to thread [" + Thread.currentThread().getName() + "]");
        }
        Object value = map.remove(key);
        if (map.isEmpty()) {
            resources.set(null);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Removed value [" + value + "] for key [" + key + "] from thread [" + Thread.currentThread().getName() + "]");
        }
        return value;
    }

    /**
	 * Return if transaction synchronization is active for the current thread.
	 * Can be called before register to avoid unnecessary instance creation.
	 * @see #registerSynchronization
	 */
    public static boolean isSynchronizationActive() {
        return (synchronizations.get() != null);
    }

    /**
	 * Activate transaction synchronization for the current thread.
	 * Called by a transaction manager on transaction begin.
	 * @throws IllegalStateException if synchronization is already active
	 */
    public static void initSynchronization() throws IllegalStateException {
        if (isSynchronizationActive()) {
            throw new IllegalStateException("Cannot activate transaction synchronization - already active");
        }
        logger.debug("Initializing transaction synchronization");
        synchronizations.set(new LinkedList());
    }

    /**
	 * Register a new transaction synchronization for the current thread.
	 * Typically called by resource management code.
	 * <p>Note that synchronizations can implement the
	 * {@link org.springframework.core.Ordered} interface.
	 * They will be executed in an order according to their order value (if any).
	 * @param synchronization the synchronization object to register
	 * @throws IllegalStateException if transaction synchronization is not active
	 * @see org.springframework.core.Ordered
	 */
    public static void registerSynchronization(TransactionSynchronization synchronization) throws IllegalStateException {
        Assert.notNull(synchronization, "TransactionSynchronization must not be null");
        if (!isSynchronizationActive()) {
            throw new IllegalStateException("Transaction synchronization is not active");
        }
        List synchs = (List) synchronizations.get();
        synchs.add(synchronization);
    }

    /**
	 * Return an unmodifiable snapshot list of all registered synchronizations
	 * for the current thread.
	 * @return unmodifiable List of TransactionSynchronization instances
	 * @throws IllegalStateException if synchronization is not active
	 * @see TransactionSynchronization
	 */
    public static List getSynchronizations() throws IllegalStateException {
        if (!isSynchronizationActive()) {
            throw new IllegalStateException("Transaction synchronization is not active");
        }
        List synchs = (List) synchronizations.get();
        Collections.sort(synchs, synchronizationComparator);
        return Collections.unmodifiableList(new ArrayList(synchs));
    }

    /**
	 * Deactivate transaction synchronization for the current thread.
	 * Called by the transaction manager on transaction cleanup.
	 * @throws IllegalStateException if synchronization is not active
	 */
    public static void clearSynchronization() throws IllegalStateException {
        if (!isSynchronizationActive()) {
            throw new IllegalStateException("Cannot deactivate transaction synchronization - not active");
        }
        logger.debug("Clearing transaction synchronization");
        synchronizations.set(null);
    }

    /**
	 * Expose the name of the current transaction, if any.
	 * Called by the transaction manager on transaction begin and on cleanup.
	 * @param name the name of the transaction, or <code>null</code> to reset it
	 * @see org.springframework.transaction.TransactionDefinition#getName()
	 */
    public static void setCurrentTransactionName(String name) {
        currentTransactionName.set(name);
    }

    /**
	 * Return the name of the current transaction, or <code>null</code> if none set.
	 * To be called by resource management code for optimizations per use case,
	 * for example to optimize fetch strategies for specific named transactions.
	 * @see org.springframework.transaction.TransactionDefinition#getName()
	 */
    public static String getCurrentTransactionName() {
        return (String) currentTransactionName.get();
    }

    /**
	 * Expose a read-only flag for the current transaction.
	 * Called by the transaction manager on transaction begin and on cleanup.
	 * @param readOnly <code>true</code> to mark the current transaction
	 * as read-only; <code>false</code> to reset such a read-only marker
	 * @see org.springframework.transaction.TransactionDefinition#isReadOnly()
	 */
    public static void setCurrentTransactionReadOnly(boolean readOnly) {
        currentTransactionReadOnly.set(readOnly ? Boolean.TRUE : null);
    }

    /**
	 * Return whether the current transaction is marked as read-only.
	 * To be called by resource management code when preparing a newly
	 * created resource (for example, a Hibernate Session).
	 * <p>Note that transaction synchronizations receive the read-only flag
	 * as argument for the <code>beforeCommit</code> callback, to be able
	 * to suppress change detection on commit. The present method is meant
	 * to be used for earlier read-only checks, for example to set the
	 * flush mode of a Hibernate Session to "FlushMode.NEVER" upfront.
	 * @see org.springframework.transaction.TransactionDefinition#isReadOnly()
	 * @see TransactionSynchronization#beforeCommit(boolean)
	 * @see org.hibernate.Session#flush
	 * @see org.hibernate.Session#setFlushMode
	 * @see org.hibernate.FlushMode#NEVER
	 */
    public static boolean isCurrentTransactionReadOnly() {
        return (currentTransactionReadOnly.get() != null);
    }

    /**
	 * Expose an isolation level for the current transaction.
	 * Called by the transaction manager on transaction begin and on cleanup.
	 * @param isolationLevel the isolation level to expose, according to the
	 * JDBC Connection constants (equivalent to the corresponding Spring
	 * TransactionDefinition constants), or <code>null</code> to reset it
	 * @see java.sql.Connection#TRANSACTION_READ_UNCOMMITTED
	 * @see java.sql.Connection#TRANSACTION_READ_COMMITTED
	 * @see java.sql.Connection#TRANSACTION_REPEATABLE_READ
	 * @see java.sql.Connection#TRANSACTION_SERIALIZABLE
	 * @see org.springframework.transaction.TransactionDefinition#ISOLATION_READ_UNCOMMITTED
	 * @see org.springframework.transaction.TransactionDefinition#ISOLATION_READ_COMMITTED
	 * @see org.springframework.transaction.TransactionDefinition#ISOLATION_REPEATABLE_READ
	 * @see org.springframework.transaction.TransactionDefinition#ISOLATION_SERIALIZABLE
	 * @see org.springframework.transaction.TransactionDefinition#getIsolationLevel()
	 */
    public static void setCurrentTransactionIsolationLevel(Integer isolationLevel) {
        currentTransactionIsolationLevel.set(isolationLevel);
    }

    /**
	 * Return the isolation level for the current transaction, if any.
	 * To be called by resource management code when preparing a newly
	 * created resource (for example, a JDBC Connection).
	 * @return the currently exposed isolation level, according to the
	 * JDBC Connection constants (equivalent to the corresponding Spring
	 * TransactionDefinition constants), or <code>null</code> if none
	 * @see java.sql.Connection#TRANSACTION_READ_UNCOMMITTED
	 * @see java.sql.Connection#TRANSACTION_READ_COMMITTED
	 * @see java.sql.Connection#TRANSACTION_REPEATABLE_READ
	 * @see java.sql.Connection#TRANSACTION_SERIALIZABLE
	 * @see org.springframework.transaction.TransactionDefinition#ISOLATION_READ_UNCOMMITTED
	 * @see org.springframework.transaction.TransactionDefinition#ISOLATION_READ_COMMITTED
	 * @see org.springframework.transaction.TransactionDefinition#ISOLATION_REPEATABLE_READ
	 * @see org.springframework.transaction.TransactionDefinition#ISOLATION_SERIALIZABLE
	 * @see org.springframework.transaction.TransactionDefinition#getIsolationLevel()
	 */
    public static Integer getCurrentTransactionIsolationLevel() {
        return (Integer) currentTransactionIsolationLevel.get();
    }

    /**
	 * Expose whether there currently is an actual transaction active.
	 * Called by the transaction manager on transaction begin and on cleanup.
	 * @param active <code>true</code> to mark the current thread as being associated
	 * with an actual transaction; <code>false</code> to reset that marker
	 */
    public static void setActualTransactionActive(boolean active) {
        actualTransactionActive.set(active ? Boolean.TRUE : null);
    }

    /**
	 * Return whether there currently is an actual transaction active.
	 * This indicates whether the current thread is associated with an actual
	 * transaction rather than just with active transaction synchronization.
	 * <p>To be called by resource management code that wants to discriminate
	 * between active transaction synchronization (with or without backing
	 * resource transaction; also on PROPAGATION_SUPPORTS) and an actual
	 * transaction being active (with backing resource transaction;
	 * on PROPAGATION_REQUIRES, PROPAGATION_REQUIRES_NEW, etc).
	 * @see #isSynchronizationActive()
	 */
    public static boolean isActualTransactionActive() {
        return (actualTransactionActive.get() != null);
    }

    /**
	 * Clear the entire transaction synchronization state for the current thread:
	 * registered synchronizations as well as the various transaction characteristics.
	 * @see #clearSynchronization()
	 * @see #setCurrentTransactionName
	 * @see #setCurrentTransactionReadOnly
	 * @see #setCurrentTransactionIsolationLevel
	 * @see #setActualTransactionActive
	 */
    public static void clear() {
        clearSynchronization();
        setCurrentTransactionName(null);
        setCurrentTransactionReadOnly(false);
        setCurrentTransactionIsolationLevel(null);
        setActualTransactionActive(false);
    }
}
