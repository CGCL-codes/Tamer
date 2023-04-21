package org.jarcraft.spi;

import java.util.List;
import org.jarcraft.InjectionFactory;
import org.jarcraft.ComponentConfiguration;

/**
 *
 * @author Leon van Zantvoort
 */
public interface InjectionFactoryProvider extends Provider {

    <T> List<InjectionFactory<T>> getInjectionFactories(ComponentConfiguration<T> configuration);
}
