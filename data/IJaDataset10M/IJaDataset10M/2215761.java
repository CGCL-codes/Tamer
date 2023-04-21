package net.zarubsys.unianalyzer;

import java.lang.reflect.InvocationTargetException;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

/**
 * Application
 *
 * @author  &lt;A HREF=&quot;mailto:lukas.zaruba@media-solutions.cz&quot;&gt;Lukas Zaruba&lt;/A&gt;, MEDIA SOLUTIONS CZECH REPUBLIC Ltd.
 * @version $Revision$ $Date$
 */
public class Application implements IApplication {

    private static final Logger log = Logger.getLogger(Application.class);

    public Object start(IApplicationContext context) throws Exception {
        log.info("Starting...");
        Display display = PlatformUI.createDisplay();
        try {
            int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
            ProgressMonitorDialog dlg = new ProgressMonitorDialog(new Shell(display));
            dlg.run(false, false, new IRunnableWithProgress() {

                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    try {
                        ResourcesPlugin.getWorkspace().save(true, monitor);
                    } catch (CoreException e) {
                    }
                }
            });
            if (returnCode == PlatformUI.RETURN_RESTART) {
                log.info("Restarting...");
                return IApplication.EXIT_RESTART;
            } else {
                log.info("Stopping...");
                return IApplication.EXIT_OK;
            }
        } finally {
            display.dispose();
        }
    }

    public void stop() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench == null) return;
        final Display display = workbench.getDisplay();
        display.syncExec(new Runnable() {

            public void run() {
                if (!display.isDisposed()) workbench.close();
            }
        });
    }
}
