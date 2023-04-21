package org.opengis.wrapper.netcdf;

import java.util.Map;
import java.util.HashMap;
import java.util.ServiceLoader;
import org.opengis.util.Factory;
import org.opengis.util.FactoryException;

/**
 * The factories needed for {@code geoapi-netcdf} working.
 *
 * @author  Martin Desruisseaux (Geomatys)
 * @version 3.1
 * @since   3.1
 */
final class Factories {

    /**
     * The factories, created when first needed.
     */
    private static final Map<Class<? extends Factory>, Factory> FACTORIES = new HashMap<Class<? extends Factory>, Factory>();

    /**
     * Do now allow instantiation.
     */
    private Factories() {
    }

    /**
     * Returns an instance of the factory of the given type.
     *
     * @param  type The factory type.
     * @return An instance of the factory of the given type, or {@code null}.
     * @throws FactoryException If no factory can be found for the given type.
     */
    public static <T extends Factory> T getFactory(final Class<T> type) throws FactoryException {
        synchronized (FACTORIES) {
            final T factory = type.cast(FACTORIES.get(type));
            if (factory != null) {
                return factory;
            }
            for (final T candidate : ServiceLoader.load(type)) {
                FACTORIES.put(type, candidate);
                return candidate;
            }
        }
        throw new FactoryException("No " + type.getSimpleName() + " found.");
    }
}
