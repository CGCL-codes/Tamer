package motudo.screen;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * The activator class controls the plug-in life cycle
 * @author game@motudo.com (motudo)
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "motudo.screen";

    private static Activator plugin;

    private static Injector injector;

    public static final boolean TEST_MODE = true;

    /**
	 * The constructor
	 */
    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        this.injector = createGuice();
    }

    private Injector createGuice() {
        Injector injector = Guice.createInjector(new ScreenModule());
        return injector;
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
    public static Activator getDefault() {
        return plugin;
    }

    public static Injector getInjector() {
        return injector;
    }

    /**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
