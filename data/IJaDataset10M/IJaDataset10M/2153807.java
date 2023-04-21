package fr.cnes.sitools.engine;

import java.net.URL;
import java.util.List;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

/**
 * OSGi activator. It registers the NRE into the Restlet API and also introspect
 * the bundles to find connector or authentication helpers.
 * 
 * @author Jerome Louvel
 */
public class SitoolsActivator implements BundleActivator {

    /**
   * Registers the helpers for a given bundle.
   * 
   * @param bundle
   *          The bundle to inspect.
   * @param helpers
   *          The helpers list to update.
   * @param constructorClass
   *          The class to use as constructor parameter.
   * @param descriptorPath
   *          The descriptor file path.
   */
    private void registerHelper(Bundle bundle, List<?> helpers, Class<?> constructorClass, String descriptorPath) {
        URL configUrl = bundle.getEntry(descriptorPath);
        if (configUrl == null) {
            configUrl = bundle.getEntry("/src/" + descriptorPath);
        }
        if (configUrl != null) {
            registerHelper(bundle, helpers, constructorClass, configUrl);
        }
    }

    /**
   * Registers the helpers for a given bundle.
   * 
   * @param bundle
   *          The bundle to inspect.
   * @param helpers
   *          The helpers list to update.
   * @param constructorClass
   *          The class to use as constructor parameter.
   * @param descriptorUrl
   *          The descriptor URL to inspect.
   */
    private void registerHelper(final Bundle bundle, List<?> helpers, Class<?> constructorClass, URL descriptorUrl) {
        SitoolsEngine.getInstance().registerHelpers(new ClassLoader() {

            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                return bundle.loadClass(name);
            }
        }, descriptorUrl, helpers, constructorClass);
    }

    /**
   * Registers the helpers for a given bundle.
   * 
   * @param bundle
   *          The bundle to inspect.
   */
    private void registerHelpers(Bundle bundle) {
        registerHelper(bundle, SitoolsEngine.getInstance().getDatasetConverters(), null, SitoolsEngine.DESCRIPTOR_DATASET_CONVERTER_PATH);
        registerHelper(bundle, SitoolsEngine.getInstance().getDatasetFilters(), null, SitoolsEngine.DESCRIPTOR_DATASET_FILTER_PATH);
        registerHelper(bundle, SitoolsEngine.getInstance().getDatasetSvas(), null, SitoolsEngine.DESCRIPTOR_DATASET_SVA_PATH);
        registerHelper(bundle, SitoolsEngine.getInstance().getRegisteredApplicationPlugins(), null, SitoolsEngine.DESCRIPTOR_APPLICATION_PLUGIN_PATH);
    }

    /**
   * Starts the OSGi bundle by registering the engine with the bundle of the
   * Restlet API.
   * 
   * @param context
   *          The bundle context.
   * @throws Exception
   *           when occurs.
   */
    public void start(BundleContext context) throws Exception {
        SitoolsEngine.register(true);
        for (final Bundle bundle : context.getBundles()) {
            registerHelpers(bundle);
        }
        context.addBundleListener(new BundleListener() {

            public void bundleChanged(BundleEvent event) {
                switch(event.getType()) {
                    case BundleEvent.INSTALLED:
                        registerHelpers(event.getBundle());
                        break;
                    case BundleEvent.UNINSTALLED:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
   * Stops the OSGi bundle by unregistering the engine with the bundle of the
   * Restlet API.
   * 
   * @param context
   *          The bundle context.
   * @throws Exception
   *           when occurs.
   */
    public void stop(BundleContext context) throws Exception {
        SitoolsEngine.clear();
    }
}
