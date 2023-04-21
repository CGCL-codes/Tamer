package org.ceno.tracker.cli.internal;

import org.ceno.communication.cli.IConnectorService;
import org.ceno.communication.cli.IEventCommunicator;
import org.ceno.tracker.cli.ICenoMessagesService;
import org.ceno.tracker.cli.ICenoTrackerService;
import org.ceno.tracker.cli.IDeveloperResourceStatesObserverService;
import org.ceno.tracker.cli.IMessagesObserverService;
import org.ceno.tracker.cli.IPeriodicSchedulerService;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

    private static ILog log;

    private static Activator plugin;

    public static final String PLUGIN_ID = "org.ceno.tracker.cli";

    /**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
    public static Activator getDefault() {
        return plugin;
    }

    public static void log(final int severity, final String message) {
        log.log(new Status(severity, PLUGIN_ID, message));
    }

    public static void log(final int severity, final String message, final Throwable throwable) {
        log.log(new Status(severity, PLUGIN_ID, message, throwable));
    }

    private IConnectorService connService;

    private IEventCommunicator eventService;

    private IPeriodicSchedulerService schedulerService;

    /**
	 * The constructor
	 */
    public Activator() {
    }

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        log = Platform.getLog(context.getBundle());
        final ServiceReference cliServiceReference = context.getServiceReference(IEventCommunicator.class.getCanonicalName());
        if (cliServiceReference != null) {
            log(Status.INFO, "Found EventCommunicator Service");
        }
        eventService = (IEventCommunicator) context.getService(cliServiceReference);
        final ServiceReference connServiceReference = context.getServiceReference(IConnectorService.class.getCanonicalName());
        if (connServiceReference != null) {
            log(Status.INFO, "Found ceno Connection Service");
        }
        connService = (IConnectorService) context.getService(connServiceReference);
        final ScheduledDeveloperResourceStatesObserverService developerResourceStatesObserverService = new ScheduledDeveloperResourceStatesObserverService(eventService, connService);
        final ScheduledMessagesObserverService messagesObserverService = new ScheduledMessagesObserverService(eventService, connService);
        final CachedCenoTrackerService trackerService = new CachedCenoTrackerService();
        developerResourceStatesObserverService.register(trackerService);
        final CachedCenoMessagesService messagesService = new CachedCenoMessagesService();
        messagesObserverService.register(messagesService);
        schedulerService = new JDKPeriodicSchedulerService();
        schedulerService.register(messagesObserverService, 3000);
        schedulerService.register(developerResourceStatesObserverService, 6000);
        context.registerService(IMessagesObserverService.class.getCanonicalName(), messagesObserverService, null);
        context.registerService(IDeveloperResourceStatesObserverService.class.getCanonicalName(), developerResourceStatesObserverService, null);
        context.registerService(ICenoTrackerService.class.getCanonicalName(), trackerService, null);
        context.registerService(ICenoMessagesService.class.getCanonicalName(), messagesService, null);
        context.registerService(IPeriodicSchedulerService.class.getCanonicalName(), schedulerService, null);
        log(Status.INFO, "Registered Services");
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        plugin = null;
        schedulerService.stop();
        super.stop(context);
    }
}
