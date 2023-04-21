package csound.osgi.apiservice.internal;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IPreferencesService;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import wade.csound.preferences.PreferenceConstants;
import csound.osgi.api.ICsound;

public class Activator implements BundleActivator {

    private static BundleContext context;

    private ICsound csoundService = null;

    public void start(BundleContext bundleContext) throws Exception {
        Activator.context = bundleContext;
        try {
            csoundService = new DSApiService();
            context.registerService(ICsound.class.getName(), csoundService, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("The Csound bundle has been activated");
    }

    public void stop(BundleContext bundleContext) throws Exception {
        csoundService.cleanup();
        Activator.context = null;
        System.out.println("The csound bundle has stopped");
    }
}
