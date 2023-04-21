package de.cabanis.unific.runtime;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import de.cabanis.unific.UnificationPlatform;

/**
 * @author Nicolas Cabanis
 */
public class UnificApplication implements IPlatformRunnable {

    private Logger logger = Logger.getLogger(getClass());

    public Object run(Object arg) throws Exception {
        UnificationPlatform.initialize();
        logger.debug("initializing platform gui...");
        Display display = PlatformUI.createDisplay();
        try {
            int code = PlatformUI.createAndRunWorkbench(display, new UnificWorkbenchAdvisor());
            UnificationPlatform.shutdown();
            return code == PlatformUI.RETURN_RESTART ? EXIT_RESTART : EXIT_OK;
        } finally {
            if (display != null) display.dispose();
        }
    }
}
