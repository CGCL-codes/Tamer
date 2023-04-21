package tudresden.ocl20.pivot.metamodels.forms;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import tudresden.ocl20.logging.LoggingPlugin;

public class FormsMetaModelPlugin extends Plugin {

    /** The plug-in ID. */
    public static final String ID = "tudresden.ocl20.pivot.metamodels.forms";

    /** The shared instance. */
    private static FormsMetaModelPlugin plugin;

    /**
	 * <p>
	 * Creates a new {@link FormsMetaModelPlugin}.
	 * </p>
	 */
    public FormsMetaModelPlugin() {
        plugin = this;
    }

    /**
	 * <p>
	 * Returns the shared instance.
	 * </p>
	 * 
	 * @return the shared instance
	 */
    public static FormsMetaModelPlugin getDefault() {
        return plugin;
    }

    /**
	 * <p>
	 * Facade method for the classes in this plug-in that hides the dependency
	 * from the <code>tudresden.ocl20.logging</code> plug-in.
	 * </p>
	 * 
	 * @param clazz
	 *            The {@link Class} to return the {@link Logger} for.
	 * 
	 * @return A log4j {@link Logger}> instance.
	 * 
	 * @generated NOT
	 */
    public static Logger getLogger(Class<?> clazz) {
        return LoggingPlugin.getLogManager(plugin).getLogger(clazz);
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        LoggingPlugin.configureDefaultLogging(plugin);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }
}
