package com.mousefeed.eclipse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

/**
 * Hooks up the plugin listeners.
 * @author Andriy Palamarchuk
 */
public class Startup implements IStartup {

    /**
     * Hooks up event listeners.
     */
    public void earlyStartup() {
        getDisplay().asyncExec(new Runnable() {

            public void run() {
                getDisplay().addFilter(SWT.Selection, new GlobalSelectionListener());
            }
        });
    }

    /**
     * Current workbench display.
     * Not <code>null</code>.
     * @return the workbench display.
     */
    public Display getDisplay() {
        return PlatformUI.getWorkbench().getDisplay();
    }
}
