package com.berdaflex.filearranger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import com.berdaflex.filearranger.core.FileArranger;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Siarhei Berdachuk
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "com.berdaflex.filearranger";

    private static Activator plugin;

    private BundleContext context;

    private int explorerNum = 0;

    private String currentExplorerId = null;

    private Map<String, ExplorerConfig> explorers = null;

    /**
	 * The constructor
	 */
    public Activator() {
        explorers = new ConcurrentHashMap<String, ExplorerConfig>();
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        this.context = context;
        FileArranger.getDefault().startup();
    }

    public void stop(BundleContext context) throws Exception {
        if (explorers != null) {
            explorers.clear();
            explorers = null;
        }
        FileArranger.getDefault().shutdown();
        super.stop(context);
        plugin = null;
        this.context = null;
    }

    /**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
    public static Activator getDefault() {
        return plugin;
    }

    /**
	 * Get Plugin BundleContext.
	 * 
	 * @return this Plugin BundleContext.
	 */
    public BundleContext getContext() {
        return context;
    }

    /**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    /**
	 * Obtain next secondary navigator identifier.
	 * 
	 * @return next id.
	 */
    public String getNextNavigatorId() {
        if ((explorers == null) || ((explorers != null) && (explorers.isEmpty()))) {
            explorerNum = 0;
        }
        explorerNum++;
        return Integer.toString(explorerNum);
    }

    /**
	 * Get configuration information for specified navigator identifier.
	 * 
	 * @param key
	 *            navigator identifier
	 * @return configuration information.
	 */
    public ExplorerConfig getNavigatorConfig(String key) {
        if (explorers != null) {
            ExplorerConfig config = explorers.get(key);
            return config;
        }
        return null;
    }

    /**
	 * Set configuration information for specified navigator identifier. Used
	 * for storing additional configuration information.
	 * 
	 * @param key
	 *            navigator identifier
	 * 
	 * @param explorerConfig
	 *            navigator configuration.
	 */
    public void setExplorerConfig(String key, ExplorerConfig explorerConfig) {
        if (explorers != null) {
            explorers.put(key, explorerConfig);
        }
    }

    /**
	 * Remove configuration information for specified navigator identifier from
	 * configurationslist.
	 * 
	 * @param key
	 *            navigator identifier
	 */
    public void removeNavigatorConfig(String key) {
        if (explorers != null) {
            explorers.remove(key);
        } else {
            currentExplorerId = null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString()).append(" explorers=");
        for (String key : explorers.keySet()) {
            sb.append(key).append(":").append(explorers.get(key).toString());
        }
        sb.append(", currentExplorerId=").append(currentExplorerId);
        return sb.toString();
    }

    /**
	 * Get secondary identifier for current ExplorerView. Used for searching
	 * current ExplorerView.
	 * 
	 * @return secondary ExplorerView identifier.
	 */
    public String getCurrentExplorerId() {
        if ((currentExplorerId == null) && ((explorers != null) && !explorers.isEmpty())) {
            currentExplorerId = "0";
        }
        return currentExplorerId;
    }

    /**
	 * Set secondary identifier for current ExplorerView. Filled with selection
	 * listener in ApplicationActionBarAdvisor.
	 * 
	 * @param currentExplorerId
	 *            secondary ExplorerView identifier
	 */
    public void setCurrentExplorerId(String currentExplorerId) {
        this.currentExplorerId = currentExplorerId;
    }
}
