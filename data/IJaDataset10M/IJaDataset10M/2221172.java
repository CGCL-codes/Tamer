package fr.macymed.modulo.platform.registry;

import fr.macymed.modulo.framework.module.ServiceRegistry;

/** 
 * <p>
 * This class creates {@link fr.macymed.modulo.framework.module.ServiceRegistry} for a specified module. In fact it uses {@link fr.macymed.modulo.platform.registry.ServiceRegistryWrapper} and {@link fr.macymed.modulo.platform.registry.ModuleServiceRegistry}.
 * </p>
 * @author <a href="mailto:alexandre.cartapanis@macymed.fr">Cartapanis Alexandre</a>
 * @version 1.1.5
 * @since Modulo Platform 2.0
 */
public class ServiceRegistryWrapperFactory {

    /**
     * <p>
     * Creates a new ServiceRegistry, using the specified module ID.
     * </p>
     * <p>
     * In fact this method creates a {@link ServiceRegistryWrapper} using the {@link ModuleServiceRegistryImpl}.
     * </p>
     * @param _module The id of the module we want a registry for.
     * @return <code>ServiceRegistry</code> - A service registry suitable for any service actions.
     */
    public static ServiceRegistry createRegistry(String _module) {
        return new ServiceRegistryWrapper(_module, ModuleServiceRegistryImpl.getInstance());
    }
}
