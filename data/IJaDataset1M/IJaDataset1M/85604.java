package net.sourceforge.x360mediaserve.upnpmediaserver.osgi;

import net.sourceforge.x360mediaserve.api.services.FormatHandler;
import net.sourceforge.x360mediaserve.upnpmediaserver.impl.MediaServer;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class FormatHandlerTrackerCustomiser implements ServiceTrackerCustomizer {

    BundleContext context;

    MediaServer server;

    public FormatHandlerTrackerCustomiser(BundleContext context, MediaServer server) {
        super();
        this.context = context;
        this.server = server;
    }

    public Object addingService(ServiceReference reference) {
        FormatHandler formatHandler = (FormatHandler) context.getService(reference);
        server.setFormatHandler(formatHandler);
        return formatHandler;
    }

    public void modifiedService(ServiceReference reference, Object service) {
    }

    public void removedService(ServiceReference reference, Object service) {
    }
}
