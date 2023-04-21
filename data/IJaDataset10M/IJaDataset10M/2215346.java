package org.gamegineer.common.internal.persistence.memento;

import static org.gamegineer.common.core.runtime.Assert.assertArgumentNotNull;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IAdapterManager;

/**
 * Manages the adapters provided by this package.
 * 
 * <p>
 * The {@code unregister} method should be called before the bundle is stopped.
 * </p>
 */
public final class Adapters {

    /** The singleton instance. */
    private static final Adapters c_instance = new Adapters();

    /**
     * The adapter factory for the JavaBeans persistence framework persistence
     * delegate.
     */
    private IAdapterFactory m_beansAdapterFactory;

    /**
     * The adapter factory for the Java object serialization framework
     * persistence delegate.
     */
    private IAdapterFactory m_serializableAdapterFactory;

    /**
     * Initializes a new instance of the {@code Adapters} class.
     */
    private Adapters() {
        super();
    }

    public static Adapters getDefault() {
        return c_instance;
    }

    /**
     * Registers the adapters managed by this object.
     * 
     * @param manager
     *        The adapter manager; must not be {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code manager} is {@code null}.
     */
    public void register(final IAdapterManager manager) {
        assertArgumentNotNull(manager, "manager");
        m_beansAdapterFactory = new org.gamegineer.common.internal.persistence.memento.schemes.beans.MementoPersistenceDelegate.AdapterFactory();
        manager.registerAdapters(m_beansAdapterFactory, Memento.class);
        m_serializableAdapterFactory = new org.gamegineer.common.internal.persistence.memento.schemes.serializable.MementoPersistenceDelegate.AdapterFactory();
        manager.registerAdapters(m_serializableAdapterFactory, Memento.class);
    }

    /**
     * Unregisters the adapters managed by this object.
     * 
     * @param manager
     *        The adapter manager; must not be {@code null}.
     * 
     * @throws java.lang.NullPointerException
     *         If {@code manager} is {@code null}.
     */
    public void unregister(final IAdapterManager manager) {
        assertArgumentNotNull(manager, "manager");
        manager.unregisterAdapters(m_serializableAdapterFactory);
        m_serializableAdapterFactory = null;
        manager.unregisterAdapters(m_beansAdapterFactory);
        m_beansAdapterFactory = null;
    }
}
