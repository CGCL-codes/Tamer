package com.parfumball.eclipse.plugin;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.*;
import org.osgi.framework.BundleContext;
import com.parfumball.ParfumBallException;
import com.parfumball.analyzer.PacketAnalyzerRegistry;
import com.parfumball.ethereal.EtherealLoader;
import com.parfumball.pcap.PcapWrapper;
import java.net.URL;
import java.util.*;

/**
 * The main plugin class to be used in the desktop.
 */
public class ParfumballPlugin extends AbstractUIPlugin implements IParfumballPlugin {

    private static ParfumballPlugin plugin;

    private ResourceBundle resourceBundle;

    /**
	 * The constructor.
	 */
    public ParfumballPlugin() {
        super();
        plugin = this;
        try {
            resourceBundle = ResourceBundle.getBundle("parfumball.ParfumballPluginResources");
        } catch (MissingResourceException x) {
            resourceBundle = null;
        }
    }

    private void loadLibraries() throws Throwable {
        EtherealLoader.loadEtherealLibs(null);
        EtherealLoader.initEthereal("/");
    }

    /**
	 * Loads the Parfumball native code, initializes the PcapWrapper and the
	 * PacketAnalyzerRegistry.
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        try {
            loadLibraries();
        } catch (Throwable t) {
            throw new Exception(t);
        }
        PcapWrapper wrapper = PcapWrapper.newInstance();
        PacketAnalyzerRegistry registry = PacketAnalyzerRegistry.getRegistry();
        Path path = new Path(PARFUMBALL_PACKET_ANALYZER_REGISTRY);
        URL url = find(path);
        if (url == null) {
            throw new ParfumBallException("Unable to load the PacketAnalyzerRegistry.");
        }
        registry.loadRegistry(url);
    }

    /**
	 * This method is called when the plug-in is stopped
	 */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
    }

    /**
	 * Returns the shared instance.
	 */
    public static ParfumballPlugin getDefault() {
        return plugin;
    }

    /**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
    public static String getResourceString(String key) {
        ResourceBundle bundle = ParfumballPlugin.getDefault().getResourceBundle();
        try {
            return (bundle != null) ? bundle.getString(key) : key;
        } catch (MissingResourceException e) {
            return key;
        }
    }

    /**
	 * Returns the plugin's resource bundle,
	 */
    public ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
	 * Logs to the plugin log. 
	 * 
	 * @param severity
	 * @param message
	 */
    public void log(int severity, String message) {
        ILog logger = getLog();
        Status msg = new Status(severity, PARFUMBALL_PLUGIN_ID, 0, message, null);
        logger.log(msg);
    }

    public void log(int severity, String message, Throwable t) {
        ILog logger = getLog();
        Status msg = new Status(severity, PARFUMBALL_PLUGIN_ID, 0, message, t);
        logger.log(msg);
    }
}
