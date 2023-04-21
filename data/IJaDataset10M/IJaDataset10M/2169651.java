package org.eclipse.jst.server.jetty.xml.ui.internal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class XMLJettyUIPlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.eclipse.jst.server.jetty.xml.ui";

    private static XMLJettyUIPlugin plugin;

    /**
	 * The constructor
	 */
    public XMLJettyUIPlugin() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
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
    public static XMLJettyUIPlugin getDefault() {
        return plugin;
    }
}
