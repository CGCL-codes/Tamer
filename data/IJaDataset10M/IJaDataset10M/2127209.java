package org.escapek.client.ui.security.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.escapek.core.serviceManager.ServiceWrapper;
import org.escapek.core.services.interfaces.IRemoteSecurityService;
import org.escapek.core.exceptions.SecurityException;

public class UserContentProvider implements IStructuredContentProvider {

    public Object[] getElements(Object inputElement) {
        try {
            IRemoteSecurityService service = (IRemoteSecurityService) inputElement;
            return service.getAllUsers(ServiceWrapper.getInstance().getTicket().getId()).toArray();
        } catch (ClassCastException cce) {
        } catch (SecurityException se) {
        }
        return new Object[0];
    }

    public void dispose() {
    }

    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
