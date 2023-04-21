package de.sooja.framework.ui;

import org.eclipse.core.runtime.IPlatformRunnable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import de.sooja.framework.update.AutomaticUpdatesJob;

/**
 * The application class for the Sooja Client. Creates and runs the
 * Workbench, passing a <code>SoojaAdvisor</code> as the workbench advisor.
 * 
 * @issue Couldn't run without initial perspective -- it failed with NPE on
 *        WorkbenchWindow.openPage (called from Workbench.openFirstTimeWindow).
 *        Advisor is currently required to override
 *        getInitialWindowPerspectiveId.
 */
public class SoojaApp implements IPlatformRunnable {

    /**
   * ID of the sooja client plug-in.
   */
    public static final String PLUGIN_ID = "de.sooja.framework.ui";

    /**
   * ID of the Sooja perspective.
   */
    public static final String SOOJA_PERSPECTIVE_ID = PLUGIN_ID + ".perspective";

    /**
   * ID of the browser view.
   */
    public static final String BROWSER_VIEW_ID = PLUGIN_ID + ".browser" + ".BrowserView";

    /**
   * ID of the bookmark view.
   */
    public static final String BOOKMARK_TREE_VIEW_ID = PLUGIN_ID + ".bookmark" + ".BookmarkTreeView";

    /**
   * ID of the soojaSites view.
   */
    public static final String SOOJASITES_VIEW_ID = PLUGIN_ID + ".soojasites" + ".SoojaSitesView";

    /**
   * Constructs a new <code>SoojaApp</code>.
   */
    public SoojaApp() {
        final AutomaticUpdatesJob a = new AutomaticUpdatesJob();
        a.schedule();
    }

    /**
   * @param args
   * @return Object
   * @throws Exception
   */
    public Object run(Object args) throws Exception {
        Display display = PlatformUI.createDisplay();
        try {
            int code = PlatformUI.createAndRunWorkbench(display, new SoojaAdvisor());
            return code == PlatformUI.RETURN_RESTART ? EXIT_RESTART : EXIT_OK;
        } finally {
            if (display != null) display.dispose();
        }
    }
}
