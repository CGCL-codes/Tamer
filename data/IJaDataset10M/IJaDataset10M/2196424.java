package org.ws4d.osgi.eventConverter.logging;

import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;
import org.ws4d.osgi.constants.GlobalConstants;
import org.ws4d.osgi.eventConverter.Activator;
import org.ws4d.osgi.eventConverter.util.EventConverterProperties;
import org.ws4d.osgi.eventConverter.util.IPropertyCallback;

public class LogServiceTracker extends ServiceTracker implements IPropertyCallback {

    public static LogService log = LogServiceImpl.getInstance();

    public static LogService osgiLog = null;

    private static LogServiceTracker thisInstance;

    public LogServiceTracker() {
        super(Activator.getBC(), LogService.class.getName(), null);
        EventConverterProperties.getInstance().registerPropertyListener(this);
    }

    public static void shutdown() {
        if (thisInstance != null) {
            thisInstance.close();
            thisInstance = null;
            log = null;
            osgiLog = null;
        }
    }

    public Object addingService(ServiceReference reference) {
        LogService logService = (LogService) context.getService(reference);
        LogServiceTracker.osgiLog = logService;
        return super.addingService(reference);
    }

    public void modifiedService(ServiceReference reference, Object service) {
        LogService logService = (LogService) context.getService(reference);
        LogServiceTracker.osgiLog = logService;
        super.modifiedService(reference, service);
    }

    public void removedService(ServiceReference reference, Object service) {
        LogServiceTracker.osgiLog = null;
        if (GlobalConstants.GLOBAL_LOGGING) LogServiceTracker.log.log(LogService.LOG_INFO, "[" + this.getClass().getName().substring(this.getClass().getName().lastIndexOf(".") + 1) + "] OSGi Logservice entfernt. Logging nun auf der Konsole.");
        super.removedService(reference, service);
    }

    public void propertyChanged(String property) {
        if (GlobalConstants.EventConverterProperties.PROP_LOG_OSGI.equals(property)) setLogTargetOSGi();
    }

    private void setLogTargetOSGi() {
        if (EventConverterProperties.getInstance().getBooleanProperty(GlobalConstants.EventConverterProperties.PROP_LOG_OSGI)) {
            this.open();
        } else if (!EventConverterProperties.getInstance().getBooleanProperty(GlobalConstants.EventConverterProperties.PROP_LOG_OSGI)) {
            this.close();
        }
    }
}
