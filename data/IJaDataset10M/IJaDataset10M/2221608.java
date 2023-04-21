package werkzeugkasten.editor.startup;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import werkzeugkasten.editor.Activator;
import werkzeugkasten.editor.listener.PainterDispatcher;

public class EventRegister implements IStartup {

    public void earlyStartup() {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        workbench.getDisplay().asyncExec(new Runnable() {

            public void run() {
                IWorkbench workbench = PlatformUI.getWorkbench();
                IWorkbenchWindow[] windows = workbench.getWorkbenchWindows();
                PainterDispatcher dispatcher = new PainterDispatcher();
                for (IWorkbenchWindow w : windows) {
                    Activator.getDefault().addPainter(w);
                    w.getActivePage().addPartListener(dispatcher);
                }
            }
        });
    }
}
