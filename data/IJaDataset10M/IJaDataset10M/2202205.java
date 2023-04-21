package org.escapek.client.ui.monitoring.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.escapek.core.EventLevel;
import org.escapek.core.internal.model.logging.RuntimeEvent;
import org.escapek.core.services.ILoggingService;
import org.escapek.logger.LoggerPlugin;

public class LogEventViewTableContentProvider implements IStructuredContentProvider {

    private ILoggingService service;

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }

    public Object[] getElements(Object inputElement) {
        try {
            service = (ILoggingService) inputElement;
            return service.getAllEvents(1000).toArray();
        } catch (NullPointerException npe) {
            LoggerPlugin.asyncLogToServer(new RuntimeEvent(EventLevel.ERROR, "No Event found", npe));
            return new Object[0];
        } catch (ClassCastException cce) {
            LoggerPlugin.asyncLogToServer(new RuntimeEvent(EventLevel.FATAL, "Bad argument provided", cce));
            return new Object[0];
        }
    }
}
