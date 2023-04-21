package org.jrcaf.core;

import org.eclipse.core.runtime.Plugin;
import org.jrcaf.core.converter.IConverterRegistry;
import org.jrcaf.core.internal.CoreRegistry;
import org.jrcaf.core.model.IModelRegistry;
import org.jrcaf.model.datasources.IDatasourceRegistry;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class JRCAFCorePlugin extends Plugin {

    /** The plugin id. */
    public static final String ID_PLUGIN = "org.jrcaf.core";

    /** Empty "" String */
    public static final String EMPTY_STRING = "";

    private static JRCAFCorePlugin plugin;

    private static CoreRegistry coreRegistry;

    /**
	 * The constructor.
	 */
    public JRCAFCorePlugin() {
        super();
        plugin = this;
    }

    /**
	 * This method is called when the plug-in is stopped
	 * @param context 
	 * @throws Exception 
	 */
    @Override
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    /**
	 * Returns the shared instance.
	 * @return The shared instance.
	 */
    public static JRCAFCorePlugin getDefault() {
        return plugin;
    }

    /**
    * @return Returns the registry of models
    * @category Getter
    */
    public static IModelRegistry getModelRegistry() {
        if (coreRegistry == null) coreRegistry = new CoreRegistry();
        return coreRegistry;
    }

    /**
    * @return Returns the registry of converters
    * @category Getter
    */
    public static IConverterRegistry getConverterRegistry() {
        if (coreRegistry == null) coreRegistry = new CoreRegistry();
        return coreRegistry;
    }

    /**
    * @return Returns the registry of datasources
    * @category Getter
    */
    public static IDatasourceRegistry getDatasourceRegistry() {
        if (coreRegistry == null) coreRegistry = new CoreRegistry();
        return coreRegistry;
    }
}
