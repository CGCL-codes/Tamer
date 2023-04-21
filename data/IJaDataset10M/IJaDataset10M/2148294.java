package org.proteusframework.core.spi;

import org.proteusframework.core.api.IPlatformDelegate;
import org.proteusframework.core.api.IPluginRegistrar;

/**
 * Primordial registrar factory is responsible for creating an {@link IPluginRegistrar} capable of producing specific
 * plugin archetype instances.
 *
 * @author Tacoma Four
 */
public interface IProteusRegistrarFactory extends IAbstractProvider {

    /**
     * Create the {@link IPluginRegistrar} factory and inject the given delegate into the instance before handing
     * it back.
     *
     * @param delegate          Platform delegate
     * @param delegateInterface Interface implemented by the delegate
     * @param <T>               Specifies the type of platform delegate that is being injected into the instantiated
     *                          factory
     * @return Non-null plugin registrar
     */
    <T extends IPlatformDelegate> IPluginRegistrar createFactory(IPlatformDelegate delegate, Class<T> delegateInterface);
}
