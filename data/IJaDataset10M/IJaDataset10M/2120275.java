package net.sf.gsearch.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class GSearchTestPlugin extends Plugin {

    private static GSearchTestPlugin plugin;

    /**
	 * The constructor.
	 */
    public GSearchTestPlugin() {
        plugin = this;
    }

    /**
	 * This method is called upon plug-in activation
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    /**
	 * Returns the shared instance.
	 */
    public static GSearchTestPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns the workspace instance.
	 */
    public static IWorkspace getWorkspace() {
        return ResourcesPlugin.getWorkspace();
    }

    /**
	 * Returns the <code>java.io.File</code> for the given <code>IPath</code>.
	 * 
	 * @param path
	 * @return the file
	 */
    public static File getFileInPlugin(IPath path) {
        try {
            URL installURL = new URL(getInstallURL(), path.toString());
            URL localURL = Platform.asLocalURL(installURL);
            return new File(localURL.getFile());
        } catch (IOException e) {
            return null;
        }
    }

    /**
	 * Returns the url of the plugin.
	 * 
	 * @return the install URL
	 */
    public static URL getInstallURL() {
        return getDefault().getBundle().getEntry("/");
    }
}
