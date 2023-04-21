package org.plazmaforge.studio.dbdesigner.rcp;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

public class DBDesignerApplication implements IPlatformRunnable {

    /**
     * 
     * @see org.eclipse.core.runtime.IPlatformRunnable#run(java.lang.Object)
     */
    public Object run(Object args) throws Exception {
        Display display = null;
        try {
            display = PlatformUI.createDisplay();
            WorkbenchAdvisor advisor = new DBDesignerWorkbenchAdvisor();
            int rc = PlatformUI.createAndRunWorkbench(display, advisor);
            return (rc == PlatformUI.RETURN_RESTART ? IPlatformRunnable.EXIT_RESTART : IPlatformRunnable.EXIT_OK);
        } finally {
            if (display != null) {
                display.dispose();
            }
        }
    }
}
