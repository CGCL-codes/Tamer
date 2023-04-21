package org.remotercp.provisioning.local.ui;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.remotercp.common.tracker.GenericServiceTracker;
import org.remotercp.common.tracker.IGenericServiceListener;
import org.remotercp.domain.provisioning.service.IProvisioningService;
import org.remotercp.provisioning.local.ui.views.IInstallFeatureServiceListener;
import org.remotercp.service.connection.session.ISessionService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.remotercp.provisioning.local.ui";

    private static Activator plugin;

    private InstallFeatureServiceTracker featureServiceTracker;

    private GenericServiceTracker<ISessionService> sessionServiceTracker;

    /**
	 * The constructor
	 */
    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        featureServiceTracker = new InstallFeatureServiceTracker(context);
        featureServiceTracker.open();
        sessionServiceTracker = new GenericServiceTracker<ISessionService>(context, ISessionService.class);
        sessionServiceTracker.open();
    }

    public void addSessionServiceListener(IGenericServiceListener<ISessionService> listener) {
        sessionServiceTracker.addServiceListener(listener);
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        featureServiceTracker.close();
        featureServiceTracker = null;
        sessionServiceTracker.close();
        sessionServiceTracker = null;
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

    public void registerInstallFeatureServiceListener(IInstallFeatureServiceListener listener) {
        featureServiceTracker.addServiceListener(listener);
    }

    private static class InstallFeatureServiceTracker extends ServiceTracker {

        private IProvisioningService installFeatureService;

        private final List<IInstallFeatureServiceListener> listeners;

        public InstallFeatureServiceTracker(BundleContext context) {
            super(context, IProvisioningService.class.getName(), null);
            listeners = new ArrayList<IInstallFeatureServiceListener>(2);
        }

        @Override
        public Object addingService(ServiceReference reference) {
            installFeatureService = (IProvisioningService) super.addingService(reference);
            for (IInstallFeatureServiceListener current : listeners) {
                current.bindInstallFeatureService(installFeatureService);
            }
            return installFeatureService;
        }

        public void addServiceListener(IInstallFeatureServiceListener listener) {
            listeners.add(listener);
            if (installFeatureService != null) {
                listener.bindInstallFeatureService(installFeatureService);
            }
        }
    }
}
