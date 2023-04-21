package ca.ucalgary.cpsc.agilePlanner.applicationWorkbench;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;

/**
 * This class controls all aspects of the application's execution
 */
public class Application extends ca.ucalgary.cpsc.agilePlanner.util.Object implements IPlatformRunnable {

    public static final String PLUGIN_ID = "ca.ucalgary.cpsc.ase.agilePlanner";

    public Object run(Object args) throws Exception {
        WorkbenchAdvisor workbenchAdvisor = new ApplicationWorkbenchAdvisor();
        Display display = PlatformUI.createDisplay();
        try {
            Platform.endSplash();
            int returnCode = PlatformUI.createAndRunWorkbench(display, workbenchAdvisor);
            if (returnCode == PlatformUI.RETURN_RESTART) {
                return IPlatformRunnable.EXIT_RESTART;
            }
            return IPlatformRunnable.EXIT_OK;
        } finally {
            display.dispose();
        }
    }
}
