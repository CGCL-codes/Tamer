package org.exist.eclipse.browse.internal.connection;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPage;
import org.exist.eclipse.IConnection;
import org.exist.eclipse.browse.connection.IConnectionListener;
import org.exist.eclipse.browse.internal.BrowsePlugin;
import org.exist.eclipse.exception.ConnectionException;

/**
 * @author Pascal Schmidiger
 * 
 */
public class OpenConnectionListener implements IConnectionListener {

    public void actionPerformed(IConnection connection) {
        try {
            connection.open();
        } catch (ConnectionException e) {
            String message = "Error while open connection";
            Status status = new Status(IStatus.ERROR, BrowsePlugin.getId(), message, e);
            BrowsePlugin.getDefault().errorDialog(message, e.getMessage(), status);
        }
    }

    public void init(IWorkbenchPage page) {
    }
}
