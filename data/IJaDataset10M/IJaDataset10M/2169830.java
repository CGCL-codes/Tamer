package com.fh.auge.core;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;
import com.fh.auge.core.internal.RepositoryImpl;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

    public static final String PLUGIN_ID = "com.fh.auge.core";

    private static Activator plugin;

    private Repository repository;

    /**
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /**
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
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

    public Repository getRepository() {
        if (repository == null) {
            repository = new RepositoryImpl();
        }
        return repository;
    }

    public void logInfo(String message) {
        getLog().log(new Status(Status.INFO, PLUGIN_ID, message));
    }

    public void logError(String message, Throwable throwable) {
        getLog().log(new Status(Status.ERROR, PLUGIN_ID, message, throwable));
    }
}
