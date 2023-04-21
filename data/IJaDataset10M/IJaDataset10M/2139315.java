package de.sooja.framework.ui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import de.sooja.framework.ui.browser.BrowserDelegate;
import de.sooja.framework.ui.browser.ISoojaBrowser;
import de.sooja.framework.ui.browser.BrowserWindowListener;

/**
 * @author Mirko Jahn <mjahn@sooja.de>
 *
 * <h2> headline </h2>
 *
 * <p> long description </p>
 */
public class UIPlugin extends AbstractUIPlugin {

    private static UIPlugin plugin;

    private ResourceBundle resourceBundle;

    private int workBenchCount = 0;

    private static BrowserDelegate browserDelegate = null;

    /**
   * The constructor.
   */
    public UIPlugin() {
        super();
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle("de.sooja.framework.PluginResources");
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
    }

    /**
   * This method is called upon plug-in activation
   *
   * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
   */
    public void start(BundleContext context) throws Exception {
        super.start(context);
    }

    /**
   * This method is called when the plug-in is stopped
   * 
   * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
   */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }

    /**
   * Returns the shared instance.
   *
   * @return instance of the plugin
   */
    public static UIPlugin getDefault() {
        return plugin;
    }

    /**
   * Returns the string from the plugin's resource bundle,
   * or 'key' if not found.
   *
   * @param key 
   * @return
   */
    public static String getResourceString(String key) {
        ResourceBundle bundle = UIPlugin.getDefault().getResourceBundle();
        try {
            return (bundle != null) ? bundle.getString(key) : key;
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
   * Returns the plugin's resource bundle,
   *
   * @return the plugins ResourceBundle
   */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
   * Caches the image-access and returns ImageDescritors for given Images
   * 
   * @param name of the image in the icons-folder
   * @return cached desriptor of the image
   */
    public static ImageDescriptor getImageDescriptor(String name) {
        String iconPath = "icons/";
        try {
            URL installURL = getDefault().getDescriptor().getInstallURL();
            URL url = new URL(installURL, iconPath + name);
            return ImageDescriptor.createFromURL(url);
        } catch (MalformedURLException e) {
            return ImageDescriptor.getMissingImageDescriptor();
        }
    }

    protected static BrowserDelegate getBrowserDelegate() {
        if (browserDelegate == null) browserDelegate = new BrowserDelegate(getDefault().getWorkbench().getActiveWorkbenchWindow());
        return browserDelegate;
    }

    public ISoojaBrowser getBrowserView() {
        return getBrowserDelegate().getBrowserView();
    }

    public static void setBrowserView(ISoojaBrowser view, Object o) {
        getBrowserDelegate().setBrowserView(view, o);
    }

    public int countActiveWorkbenchs() {
        return workBenchCount;
    }

    public void removeActiveWorkbench() {
        --workBenchCount;
    }

    public void addActiveWorkbench() {
        ++workBenchCount;
    }

    public void addBrowserWindowListener(BrowserWindowListener listener) {
        getBrowserDelegate().addBrowserWindowListener(listener);
    }

    public void removeBrowserWindowListener(BrowserWindowListener listener) {
        getBrowserDelegate().removeBrowserWindowListener(listener);
    }
}
