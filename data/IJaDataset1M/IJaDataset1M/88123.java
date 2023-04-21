package de.devisnik.eidle.ide;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class ActivityListener implements IStartup {

    private WorbenchActivityTracker itsTracker;

    public void earlyStartup() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        itsTracker = new WorbenchActivityTracker(workbench, new IDelayProvider() {

            public long getDelay() {
                return ActivationPreferences.getCountDown() * 1000;
            }
        });
        final Display display = workbench.getDisplay();
        display.asyncExec(new Runnable() {

            public void run() {
                final Listener listener = new Listener() {

                    public void handleEvent(Event event) {
                        itsTracker.notifyActivity();
                    }
                };
                display.addFilter(SWT.MouseDown, listener);
                display.addFilter(SWT.KeyDown, listener);
            }
        });
    }
}
