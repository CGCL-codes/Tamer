package jimo.osgi.modules.updater.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import jimo.osgi.api.CommandContext;
import jimo.osgi.api.CommandHandler;
import jimo.osgi.api.EventAdminExt;
import jimo.osgi.api.JIMOConstants;
import jimo.osgi.api.util.BundleActivatorHelper;
import jimo.osgi.modules.updater.api.UpdateSource;
import jimo.osgi.modules.updater.api.Updater;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator extends BundleActivatorHelper {

    private static final String COMMANDNAME = "update";

    Timer timer;

    public static Activator INSTANCE;

    public void start(final BundleContext context) throws Exception {
        super.start(context);
        INSTANCE = this;
        FrameworkUpdater frameworkUpdater = new FrameworkUpdater(context);
        BundleUpdater bundleUpdater = new BundleUpdater(context);
        context.registerService(Updater.class.getName(), frameworkUpdater, null);
        context.registerService(Updater.class.getName(), bundleUpdater, null);
        Dictionary props = new Hashtable();
        props.put(JIMOConstants.SERVICE_COMMANDNAME, COMMANDNAME);
        context.registerService(CommandHandler.class.getName(), new CommandHandler() {

            public void onCommand(String command, CommandContext context) {
                doUpdate();
            }
        }, props);
        ManagedServiceFactory updateSourceFactory = new ManagedServiceFactory() {

            private Map mapRegistratons = Collections.synchronizedMap(new HashMap());

            public void deleted(String pid) {
                ServiceRegistration reg = (ServiceRegistration) mapRegistratons.get(pid);
                if (reg != null) {
                    mapRegistratons.remove(pid);
                    reg.unregister();
                }
            }

            public String getName() {
                return UpdaterConstants.UPDATESOURCEFACTORYNAME;
            }

            public void updated(String pid, final Dictionary properties) throws ConfigurationException {
                if (properties == null) return;
                ServiceRegistration reg = (ServiceRegistration) mapRegistratons.get(pid);
                if (reg != null) reg.unregister();
                Dictionary updaterProperties = new Hashtable();
                reg = context.registerService(UpdateSource.class.getName(), new UpdateSource() {

                    public URL getUpdateLocation() throws MalformedURLException {
                        URL url = new URL((String) properties.get(UpdaterConstants.UPDATESOURCE_URL));
                        return url;
                    }
                }, updaterProperties);
                mapRegistratons.put(pid, reg);
            }
        };
        Dictionary properties = new Hashtable();
        properties.put(Constants.SERVICE_PID, UpdaterConstants.FID_UPDATESOURCE);
        context.registerService(ManagedServiceFactory.class.getName(), updateSourceFactory, properties);
    }

    protected void doUpdate() {
        ServiceReference[] serviceReferences;
        try {
            serviceReferences = bundleContext.getServiceReferences(UpdateSource.class.getName(), null);
            if (serviceReferences == null) {
                return;
            }
            URL[] urls = new URL[serviceReferences.length];
            for (int i = 0; i < urls.length; i++) {
                UpdateSource source = (UpdateSource) bundleContext.getService(serviceReferences[i]);
                try {
                    urls[i] = source.getUpdateLocation();
                } catch (MalformedURLException e) {
                    ((LogService) logTracker.getService()).log(LogService.LOG_ERROR, "Update source url error", e);
                }
            }
            serviceReferences = bundleContext.getServiceReferences(Updater.class.getName(), null);
            if (serviceReferences == null) return;
            for (int i = 0; i < serviceReferences.length; i++) {
                Updater updater = (Updater) bundleContext.getService(serviceReferences[i]);
                updater.checkForUpdate(urls);
            }
        } catch (InvalidSyntaxException e) {
            ((LogService) logTracker.getService()).log(LogService.LOG_ERROR, "Syntax error", e);
        }
    }

    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        if (timer != null) timer.cancel();
    }
}
