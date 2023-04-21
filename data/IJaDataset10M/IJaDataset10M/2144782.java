package edu.hawaii.ics.csdl.jupiter.event;

import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import edu.hawaii.ics.csdl.jupiter.ui.marker.MarkerTextPartListener;
import edu.hawaii.ics.csdl.jupiter.util.JupiterLogger;

/**
 *
 * @author Takuya Yamashita
 * @version $Id: WindowListenerAdapter.java 40 2007-05-30 00:24:50Z hongbing $
 */
public class WindowListenerAdapter implements IWindowListener {

    /** Jupiter logger */
    private JupiterLogger log = JupiterLogger.getLogger();

    /**
   * @see org.eclipse.ui.IWindowListener#windowActivated(org.eclipse.ui.IWorkbenchWindow)
   */
    public void windowActivated(IWorkbenchWindow window) {
    }

    /**
   * @see org.eclipse.ui.IWindowListener#windowDeactivated(org.eclipse.ui.IWorkbenchWindow)
   */
    public void windowDeactivated(IWorkbenchWindow window) {
    }

    /**
   * @see org.eclipse.ui.IWindowListener#windowClosed(org.eclipse.ui.IWorkbenchWindow)
   */
    public void windowClosed(IWorkbenchWindow window) {
        log.debug("the window was closed.");
    }

    /**
   * @see org.eclipse.ui.IWindowListener#windowOpened(org.eclipse.ui.IWorkbenchWindow)
   */
    public void windowOpened(IWorkbenchWindow window) {
        log.debug("New window was opened.");
        IWorkbenchPage page = window.getActivePage();
        try {
            page.addSelectionListener(new ReviewSelectionListener());
            page.addPartListener(new MarkerTextPartListener());
        } catch (NullPointerException e) {
            log.warning("Could not register either selection listener or part listener.");
        }
    }
}
