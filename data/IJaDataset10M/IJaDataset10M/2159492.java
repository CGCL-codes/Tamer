package org.nomadpim.core.ui.notification;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class PopupNotifierService implements INotifierService {

    public void objectAdded(final ExtendedNotification o) {
        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

            public void run() {
                Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
                NotificationSlideIntoPopup popup = new NotificationSlideIntoPopup(shell, o, o);
                popup.open();
            }
        });
    }

    public void objectChanged(ExtendedNotification o) {
    }

    public void objectRemoved(ExtendedNotification o) {
    }
}
