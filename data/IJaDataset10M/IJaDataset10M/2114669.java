package org.gamegineer.common.persistence.schemes.serializable.services.persistencedelegateregistry;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentLegal;
import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import net.jcip.annotations.ThreadSafe;
import org.gamegineer.common.persistence.schemes.serializable.IPersistenceDelegate;

/**
 * Fake implementation of
 * {@link org.gamegineer.common.persistence.schemes.serializable.services.persistencedelegateregistry.IPersistenceDelegateRegistry}
 * .
 */
@ThreadSafe
public final class FakePersistenceDelegateRegistry implements IPersistenceDelegateRegistry {

    /** The collection of persistence delegates managed by this object. */
    private final ConcurrentMap<String, IPersistenceDelegate> persistenceDelegates_;

    /**
     * Initializes a new instance of the {@code FakePersistenceDelegateRegistry}
     * class.
     */
    public FakePersistenceDelegateRegistry() {
        persistenceDelegates_ = new ConcurrentHashMap<String, IPersistenceDelegate>();
    }

    @Override
    public IPersistenceDelegate getPersistenceDelegate(final Class<?> type) {
        assertArgumentNotNull(type, "type");
        return getPersistenceDelegate(type.getName());
    }

    @Override
    public IPersistenceDelegate getPersistenceDelegate(final String typeName) {
        assertArgumentNotNull(typeName, "typeName");
        return persistenceDelegates_.get(typeName);
    }

    @Override
    public Set<String> getTypeNames() {
        return new HashSet<String>(persistenceDelegates_.keySet());
    }

    @Override
    public void registerPersistenceDelegate(final Class<?> type, final IPersistenceDelegate persistenceDelegate) {
        assertArgumentNotNull(type, "type");
        assertArgumentNotNull(persistenceDelegate, "persistenceDelegate");
        assertArgumentLegal(persistenceDelegates_.putIfAbsent(type.getName(), persistenceDelegate) == null, "type");
    }

    @Override
    public void unregisterPersistenceDelegate(final Class<?> type, final IPersistenceDelegate persistenceDelegate) {
        assertArgumentNotNull(type, "type");
        assertArgumentNotNull(persistenceDelegate, "persistenceDelegate");
        assertArgumentLegal(persistenceDelegates_.remove(type.getName(), persistenceDelegate), "type");
    }
}
