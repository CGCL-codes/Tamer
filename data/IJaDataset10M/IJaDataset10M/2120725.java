package org.dcmn8;

import org.dcmn8.gui.DefaultWorkbenchAdvisor;
import org.dcmn8.gui.util.UIThread;
import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

/**
 * @author copris
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DCmn8Main implements IPlatformRunnable {

    public Object run(Object args) {
        WorkbenchAdvisor workbenchAdvisor = new DefaultWorkbenchAdvisor();
        Display display = PlatformUI.createDisplay();
        UIThread.init(new UIThread(display));
        try {
            int returnCode = PlatformUI.createAndRunWorkbench(display, workbenchAdvisor);
            if (returnCode == PlatformUI.RETURN_RESTART) {
                return IPlatformRunnable.EXIT_RESTART;
            } else {
                return IPlatformRunnable.EXIT_OK;
            }
        } finally {
            display.dispose();
        }
    }
}
