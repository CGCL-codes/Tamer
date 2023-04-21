package net.sourceforge.eclipservices.views.events;

import java.util.EventObject;
import org.eclipse.jface.viewers.Viewer;
import net.sourceforge.eclipservices.services.IService;

public class ServiceControlEvent extends EventObject {

    private IService service;

    private static final long serialVersionUID = 2621720772573103138L;

    public ServiceControlEvent(Object source, IService service) {
        super(source);
        if (service == null) {
            throw new IllegalArgumentException("null source");
        }
        this.service = service;
    }

    public Viewer getViewer() {
        return (Viewer) getSource();
    }

    public IService getService() {
        return service;
    }

    public int getStatus() {
        return service.getStatus();
    }
}
