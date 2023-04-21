package com.dfruits.logging;

import java.io.InputStream;
import java.util.Properties;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import com.dfruits.logging.impl.DelayedLogService;

/**
 * The activator class controls the plug-in life cycle
 */
public class LogServicePlugin extends Plugin {

    public static final String PLUGIN_ID = "com.dfruits.logging";

    private static LogServicePlugin plugin;

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        context.registerService(LogService.class.getName(), new DelayedLogService(), new Properties());
    }

    public LogService getLogService() {
        ServiceReference ref = getBundle().getBundleContext().getServiceReference(LogService.class.getName());
        return (LogService) getBundle().getBundleContext().getService(ref);
    }

    public InputStream getResource(String resource) {
        try {
            return getDefault().getBundle().getEntry(resource).openStream();
        } catch (Exception e) {
            return null;
        }
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static LogServicePlugin getDefault() {
        return plugin;
    }
}
