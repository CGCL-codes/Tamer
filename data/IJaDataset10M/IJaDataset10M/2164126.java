package net.sf.persistant;

/**
 * <p>
 * <code>Persistant</code> is the utility class through which a client creates a {@link PersistantContext} instance. It
 * serves as the client entry point into the Persistant API. 
 * </p>
 */
public class Persistant {

    /**
     * <p>
     * Construct a {@link Persistant} instance.
     * </p>
     */
    private Persistant() {
        super();
    }

    /**
     * <p>
     * Create a {@link PersistantContextBuilder} through which a context can be configured and built.
     * </p>
     * <p>
     * This method takes an explicit {@link PersistenceAdapter} instance, which it wraps internally in a static
     * {@link PersistenceAdapterLocator} instance. Because this locator will not be serializable, some downstream
     * objects (such as lazy pageable lists) will not live through a serialization/deserialization process. If you
     * believe that lazy objects may be serialized in your application, you should use
     * {@link #buildContext(PersistenceAdapterLocator)} instead, passing it a locator which can obtain a
     * {@link PersistenceAdapter} instance even after it has been through a serialization cycle. 
     * </p>
     * 
     * @param persistenceAdapter the persistence adapter which will be used by the resources generated from the context.
     * @return {@link PersistantContextBuilder} instance.
     */
    public static PersistantContextBuilder buildContext(final PersistenceAdapter persistenceAdapter) {
        return buildContext(new StaticPersistenceAdapterLocator(persistenceAdapter));
    }

    /**
     * <p>
     * Create a {@link PersistantContextBuilder} through which a context can be configured and built.
     * </p>
     * 
     * @param persistenceAdapterLocator the persistence adapter locator.
     * @return {@link PersistantContextBuilder} instance.
     */
    public static PersistantContextBuilder buildContext(final PersistenceAdapterLocator persistenceAdapterLocator) {
        return new PersistantContextBuilderImpl(persistenceAdapterLocator);
    }
}
