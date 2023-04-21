package cx.ath.mancel01.dependencyshot.dynamic.config;

import cx.ath.mancel01.dependencyshot.DependencyShot;
import cx.ath.mancel01.dependencyshot.api.DSBinder;
import cx.ath.mancel01.dependencyshot.api.Stage;
import cx.ath.mancel01.dependencyshot.dynamic.registry.OSGiServiceRegistryImpl;
import cx.ath.mancel01.dependencyshot.dynamic.registry.ServiceRegistry;
import cx.ath.mancel01.dependencyshot.dynamic.registry.ServiceRegistryProvider;
import cx.ath.mancel01.dependencyshot.graph.Binder;
import cx.ath.mancel01.dependencyshot.injection.InjectorImpl;
import cx.ath.mancel01.dependencyshot.spi.ConfigurationHandler;
import org.osgi.framework.BundleContext;

/**
 *
 * @author Mathieu ANCELIN
 */
public class OSGiConfigurator extends ConfigurationHandler {

    private BundleContext context;

    private Binder binder;

    private ServiceRegistry registry;

    @Override
    public InjectorImpl getInjector(Stage stage) {
        InjectorImpl injector = null;
        if (context == null) {
            throw new IllegalStateException("You must set the current bundle context.");
        }
        if (binder == null) {
            injector = DependencyShot.getInjector();
        } else {
            injector = DependencyShot.getInjector(binder);
        }
        ServiceRegistryProvider.OSGiEnvHolder holder = injector.getInstance(ServiceRegistryProvider.OSGiEnvHolder.class);
        holder.setOsgi(true);
        registry = injector.getInstance(ServiceRegistry.class);
        if (registry == null) {
            throw new IllegalStateException("Injection for bundle context failed.");
        }
        ((OSGiServiceRegistryImpl) registry).setContext(context);
        return injector;
    }

    @Override
    public boolean isAutoEnabled() {
        return false;
    }

    public OSGiConfigurator withBinder(DSBinder binder) {
        this.binder = (Binder) binder;
        return this;
    }

    public OSGiConfigurator context(BundleContext context) {
        this.context = context;
        return this;
    }
}
