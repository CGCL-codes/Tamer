package org.gamegineer.common.internal.persistence.schemes.serializable.services.persistencedelegateregistry;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentLegal;
import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;
import org.gamegineer.common.internal.persistence.Debug;
import org.gamegineer.common.internal.persistence.Loggers;
import org.gamegineer.common.persistence.schemes.serializable.IPersistenceDelegate;
import org.gamegineer.common.persistence.schemes.serializable.services.persistencedelegateregistry.IPersistenceDelegateRegistry;
import org.gamegineer.common.persistence.schemes.serializable.services.persistencedelegateregistry.PersistenceDelegateRegistryConstants;
import org.osgi.framework.ServiceReference;

/**
 * Implementation of
 * {@link org.gamegineer.common.persistence.schemes.serializable.services.persistencedelegateregistry.IPersistenceDelegateRegistry}
 * .
 */
@ThreadSafe
public final class PersistenceDelegateRegistry implements IPersistenceDelegateRegistry {

    /** The instance lock. */
    private final Object lock_;

    /**
     * The collection of persistence delegates registered from the service
     * registry. The key is the persistence delegate service reference; the
     * value is the persistence delegate proxy registration token.
     */
    @GuardedBy("lock_")
    private final Map<ServiceReference, PersistenceDelegateProxyRegistration> persistenceDelegateProxyRegistrations_;

    /**
     * The collection of registered persistence delegates. The key is the type
     * name; the value is the associated persistence delegate.
     */
    @GuardedBy("lock_")
    private final Map<String, IPersistenceDelegate> persistenceDelegates_;

    /**
     * Initializes a new instance of the {@code PersistenceDelegateRegistry}
     * class.
     */
    public PersistenceDelegateRegistry() {
        lock_ = new Object();
        persistenceDelegateProxyRegistrations_ = new HashMap<ServiceReference, PersistenceDelegateProxyRegistration>();
        persistenceDelegates_ = new HashMap<String, IPersistenceDelegate>();
    }

    private static Set<String> getDelegatorTypeNames(final ServiceReference persistenceDelegateReference) {
        assert persistenceDelegateReference != null;
        final Object propertyValue = persistenceDelegateReference.getProperty(PersistenceDelegateRegistryConstants.PROPERTY_DELEGATORS);
        if (propertyValue instanceof String) {
            return Collections.singleton((String) propertyValue);
        } else if (propertyValue instanceof String[]) {
            return new HashSet<String>(Arrays.asList((String[]) propertyValue));
        }
        Loggers.getDefaultLogger().warning(Messages.PersistenceDelegateRegistry_getDelegatorTypeNames_noDelegators(persistenceDelegateReference));
        return Collections.emptySet();
    }

    @Override
    public IPersistenceDelegate getPersistenceDelegate(final Class<?> type) {
        assertArgumentNotNull(type, "type");
        return getPersistenceDelegate(type.getName());
    }

    @Override
    public IPersistenceDelegate getPersistenceDelegate(final String typeName) {
        assertArgumentNotNull(typeName, "typeName");
        synchronized (lock_) {
            return persistenceDelegates_.get(typeName);
        }
    }

    @Override
    public Set<String> getTypeNames() {
        synchronized (lock_) {
            return new HashSet<String>(persistenceDelegates_.keySet());
        }
    }

    @Override
    public void registerPersistenceDelegate(final Class<?> type, final IPersistenceDelegate persistenceDelegate) {
        assertArgumentNotNull(type, "type");
        assertArgumentNotNull(persistenceDelegate, "persistenceDelegate");
        synchronized (lock_) {
            registerPersistenceDelegate(type.getName(), persistenceDelegate);
        }
    }

    /**
     * Registers the persistence delegate associated with the specified service
     * reference.
     * 
     * <p>
     * This method attempts to register the persistence delegate associated with
     * the specified service reference for each type specified in the {@code
     * PersistenceDelegateRegistryConstants.PROPERTY_DELEGATORS} property of the
     * service registration. If a persistence delegate has already been
     * registered for a type associated with the service registration, this
     * method logs the collision but otherwise continues on normally attempting
     * to register the persistence delegate for any remaining types.
     * </p>
     * 
     * @param persistenceDelegateReference
     *        The persistence delegate service reference; must not be {@code
     *        null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code persistenceDelegateReference} is {@code null}.
     */
    public void registerPersistenceDelegate(final ServiceReference persistenceDelegateReference) {
        assertArgumentNotNull(persistenceDelegateReference, "persistenceDelegateReference");
        synchronized (lock_) {
            final PersistenceDelegateProxy persistenceDelegateProxy = new PersistenceDelegateProxy(persistenceDelegateReference);
            final Set<String> typeNames = new HashSet<String>();
            for (final String typeName : getDelegatorTypeNames(persistenceDelegateReference)) {
                try {
                    registerPersistenceDelegate(typeName, persistenceDelegateProxy);
                    typeNames.add(typeName);
                } catch (final IllegalArgumentException e) {
                    Loggers.getDefaultLogger().log(Level.WARNING, Messages.PersistenceDelegateRegistry_registerPersistenceDelegateFromServiceReference_registrationFailed(typeName, persistenceDelegateReference), e);
                }
            }
            if (!typeNames.isEmpty()) {
                final PersistenceDelegateProxyRegistration persistenceDelegateProxyRegistration = new PersistenceDelegateProxyRegistration(typeNames, persistenceDelegateProxy);
                persistenceDelegateProxyRegistrations_.put(persistenceDelegateReference, persistenceDelegateProxyRegistration);
            }
        }
    }

    /**
     * Registers the specified persistence delegate for the specified type.
     * 
     * @param typeName
     *        The name of the type associated with the persistence delegate;
     *        must not be {@code null}.
     * @param persistenceDelegate
     *        The persistence delegate; must not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If a persistence delegate is already registered for the specified
     *         type.
     */
    @GuardedBy("lock_")
    private void registerPersistenceDelegate(final String typeName, final IPersistenceDelegate persistenceDelegate) {
        assert typeName != null;
        assert persistenceDelegate != null;
        assert Thread.holdsLock(lock_);
        assertArgumentLegal(!persistenceDelegates_.containsKey(typeName), "typeName", Messages.PersistenceDelegateRegistry_registerPersistenceDelegate_type_registered(typeName));
        persistenceDelegates_.put(typeName, persistenceDelegate);
        Debug.getDefault().trace(Debug.OPTION_SERIALIZABLE, String.format("Registered persistence delegate '%1$s' for type '%2$s'", persistenceDelegate, typeName));
    }

    @Override
    public void unregisterPersistenceDelegate(final Class<?> type, final IPersistenceDelegate persistenceDelegate) {
        assertArgumentNotNull(type, "type");
        assertArgumentNotNull(persistenceDelegate, "persistenceDelegate");
        synchronized (lock_) {
            unregisterPersistenceDelegate(type.getName(), persistenceDelegate);
        }
    }

    /**
     * Unregisters the persistence delegate associated with the specified
     * service reference.
     * 
     * <p>
     * This method attempts to unregister the persistence delegate associated
     * with the specified service reference for each type for which it was
     * registered in a previous call to
     * {@link #registerPersistenceDelegate(ServiceReference)}.
     * </p>
     * 
     * @param persistenceDelegateReference
     *        The persistence delegate service reference; must not be {@code
     *        null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code persistenceDelegateReference} is {@code null}.
     */
    public void unregisterPersistenceDelegate(final ServiceReference persistenceDelegateReference) {
        assertArgumentNotNull(persistenceDelegateReference, "persistenceDelegateReference");
        synchronized (lock_) {
            final PersistenceDelegateProxyRegistration persistenceDelegateProxyRegistration = persistenceDelegateProxyRegistrations_.remove(persistenceDelegateReference);
            if (persistenceDelegateProxyRegistration != null) {
                final PersistenceDelegateProxy persistenceDelegateProxy = persistenceDelegateProxyRegistration.persistenceDelegateProxy;
                for (final String typeName : persistenceDelegateProxyRegistration.typeNames) {
                    unregisterPersistenceDelegate(typeName, persistenceDelegateProxy);
                }
                persistenceDelegateProxy.dispose();
            }
        }
    }

    /**
     * Unregisters the persistence delegate for the specified type.
     * 
     * @param typeName
     *        The name of the type associated with the persistence delegate;
     *        must not be {@code null}.
     * @param persistenceDelegate
     *        The persistence delegate; must not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If the specified persistence delegate was not previously
     *         registered for the specified type.
     */
    @GuardedBy("lock_")
    private void unregisterPersistenceDelegate(final String typeName, final IPersistenceDelegate persistenceDelegate) {
        assert typeName != null;
        assert persistenceDelegate != null;
        assert Thread.holdsLock(lock_);
        assertArgumentLegal(persistenceDelegate.equals(persistenceDelegates_.get(typeName)), "typeName", Messages.PersistenceDelegateRegistry_unregisterPersistenceDelegate_type_unregistered(typeName));
        persistenceDelegates_.remove(typeName);
        Debug.getDefault().trace(Debug.OPTION_SERIALIZABLE, String.format("Unregistered persistence delegate '%1$s' for type '%2$s'", persistenceDelegate, typeName));
    }

    /**
     * A registration token for a persistence delegate registered from the
     * service registry.
     */
    @Immutable
    private static final class PersistenceDelegateProxyRegistration {

        /** The persistence delegate proxy. */
        final PersistenceDelegateProxy persistenceDelegateProxy;

        /**
         * The collection of type names under which the persistence delegate is
         * registered.
         */
        final Set<String> typeNames;

        /**
         * Initializes a new instance of the {@code
         * PersistenceDelegateProxyRegistration} class.
         * 
         * @param typeNames
         *        The collection of type names under which the persistence
         *        delegate is registered; must not be {@code null}.
         * @param persistenceDelegateProxy
         *        The persistence delegate proxy; must not be {@code null}.
         */
        PersistenceDelegateProxyRegistration(@SuppressWarnings("hiding") final Set<String> typeNames, @SuppressWarnings("hiding") final PersistenceDelegateProxy persistenceDelegateProxy) {
            assert typeNames != null;
            assert persistenceDelegateProxy != null;
            this.typeNames = Collections.unmodifiableSet(new HashSet<String>(typeNames));
            this.persistenceDelegateProxy = persistenceDelegateProxy;
        }
    }
}
