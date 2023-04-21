package net.sourceforge.ba_fo_ma.internal;

import net.sourceforge.ba_fo_ma.internal.advisor.DefaultWorkbenchAdvisor;
import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 *
 */
public class DefaultApplication implements IPlatformRunnable {

    /**
     * 
     */
    public DefaultApplication() {
    }

    /**
     * 
	 */
    public Object run(Object args) throws Exception {
        Display display = PlatformUI.createDisplay();
        try {
            int returnCode = PlatformUI.createAndRunWorkbench(display, new DefaultWorkbenchAdvisor());
            if (returnCode == PlatformUI.RETURN_RESTART) {
                return IPlatformRunnable.EXIT_RESTART;
            }
            return IPlatformRunnable.EXIT_OK;
        } finally {
            display.dispose();
        }
    }
}
