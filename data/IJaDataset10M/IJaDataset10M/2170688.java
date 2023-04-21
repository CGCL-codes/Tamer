package uk.ac.reload.straker;

import java.io.File;
import java.net.URL;
import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * The main application class for standalone RCP operation.
 * Creates and runs the Workbench, passing a <code>StrakerAdvisor</code>
 * as the workbench advisor.
 * 
 * @author Phillip Beauvoir
 * @version $Id: StrakerApp.java,v 1.10 2006/07/10 11:50:46 phillipus Exp $
 */
public class StrakerApp implements IPlatformRunnable {

    /**
     * Flag for whether we are running in standalone or as plugin
     */
    public static boolean IS_STANDALONE = false;

    /**
	 * Constructor
	 */
    public StrakerApp() {
    }

    public Object run(Object args) throws Exception {
        IS_STANDALONE = true;
        String workspaceDir = StrakerPlugin.getDefault().getResourceString("%workspace.dir");
        if (workspaceDir != null) {
            File location = new File(System.getProperty("user.dir"), workspaceDir);
            setWorkbenchDataLocation(location.toURL());
        }
        Display display = PlatformUI.createDisplay();
        try {
            int code = PlatformUI.createAndRunWorkbench(display, new StrakerWorkbenchAdvisor());
            return code == PlatformUI.RETURN_RESTART ? EXIT_RESTART : EXIT_OK;
        } finally {
            if (display != null) {
                display.dispose();
            }
        }
    }

    /**
	 * Set the file location of the data store.<p>
	 * We want to do this for an RCP standalone app but not when this is running as a plugin
	 * becauae the location of the Workbench instance will be set already.
	 * This has to be done before the Workbench starts.
	 * Note that the launch configuration in the Eclipse IDE should have "@noDefault"
     * as the workspace for the launch configuration.
	 * 
	 * @param url A url to a folder
	 */
    public static void setWorkbenchDataLocation(URL url) {
        Location instanceLoc = Platform.getInstanceLocation();
        if (instanceLoc == null) {
            StrakerPlugin.getDefault().logError("Instance Location is null, cannot set it in setWorkbenchDataLocation(URL)", null);
        } else if (!instanceLoc.isSet()) {
            instanceLoc.release();
            instanceLoc.setURL(url, false);
        }
    }
}
