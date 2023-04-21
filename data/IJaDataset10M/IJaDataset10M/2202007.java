package tudresden.ocl20.pivot.standardlibrary.java;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import tudresden.ocl20.logging.LoggingPlugin;
import tudresden.ocl20.pivot.essentialocl.standardlibrary.factory.IStandardLibraryFactory;
import tudresden.ocl20.pivot.standardlibrary.java.factory.JavaStandardLibraryFactory;

/**
 * <p>
 * The activator class controls the plug-in life cycle.
 * </p>
 * 
 * <p>
 * This plug-in provides the OCL standard library which implements the OCL types
 * in Java.
 * </p>
 * 
 * @author Ronny Brandt.
 */
public class JavaStandardlibraryPlugin extends Plugin {

    /** The plug-in ID. */
    public static final String PLUGIN_ID = "tudresden.ocl20.pivot.standardlibrary.java";

    /** The shared instance. */
    private static JavaStandardlibraryPlugin plugin;

    /**
	 * <p>
	 * Returns the shared instance.
	 * </p>
	 * 
	 * @return the shared instance
	 */
    public static JavaStandardlibraryPlugin getDefault() {
        return plugin;
    }

    /**
	 * <p>
	 * Returns the {@link IStandardLibraryFactory} of the
	 * {@link JavaStandardlibraryPlugin}.
	 * </p>
	 * 
	 * @return The {@link IStandardLibraryFactory} of the
	 *         {@link JavaStandardlibraryPlugin}.
	 */
    public static IStandardLibraryFactory getStandardLibraryFactory() {
        return JavaStandardLibraryFactory.INSTANCE;
    }

    /**
	 * <p>
	 * The constructor.
	 * </p>
	 */
    public JavaStandardlibraryPlugin() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        LoggingPlugin.configureDefaultLogging(plugin);
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }
}
