package org.apache.ws.addressing.uuid;

import org.apache.commons.discovery.DiscoveryException;
import org.apache.commons.discovery.tools.DiscoverSingleton;

/**
 * A {@link UUIdGenerator} factory.
 *
 * @author Ian P. Springer
 * @author Rodrigo Ruiz
 */
public final class UUIdGeneratorFactory {

    /**
   * Generator instance.
   */
    private static final UUIdGenerator GEN;

    static {
        UUIdGenerator gen;
        try {
            gen = (UUIdGenerator) DiscoverSingleton.find(UUIdGenerator.class);
        } catch (DiscoveryException e) {
            try {
                gen = new JugUUIdGenerator();
            } catch (Throwable t) {
                gen = new AxisUUIdGenerator();
            }
        }
        GEN = gen;
    }

    /**
   * Hidden constructor.
   */
    private UUIdGeneratorFactory() {
    }

    /**
   * Creates a generator, using the default implementation (JUG dependent).
   *
   * @return Generator instance
   */
    public static UUIdGenerator getGenerator() {
        return GEN;
    }
}
