package pm.eclipse.prompt.internal.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class ShellUtil {

    /**
	 * Return the modal shell that is currently open. If there isn't one then return null.
	 * 
	 * @param shell
	 *            A shell to exclude from the search. May be <code>null</code>.
	 * 
	 * @return Shell or <code>null</code>.
	 */
    public static Shell getModalShellExcluding(Shell shell) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        Shell[] shells = workbench.getDisplay().getShells();
        int modal = SWT.APPLICATION_MODAL | SWT.SYSTEM_MODAL | SWT.PRIMARY_MODAL;
        for (int i = 0; i < shells.length; i++) {
            if (shells[i].equals(shell)) {
                break;
            }
            if (shells[i].isVisible()) {
                int style = shells[i].getStyle();
                if ((style & modal) != 0) {
                    return shells[i];
                }
            }
        }
        return null;
    }

    /**
	 * Utility method to get the best parenting possible for a dialog. If there is a modal shell create it so as to avoid two modal dialogs. If not then return the shell of the
	 * active workbench window. If neither can be found return null.
	 * 
	 * @return Shell or <code>null</code>
	 */
    public static Shell getDefaultParent() {
        Shell modal = getModalShellExcluding(null);
        if (modal != null) {
            return modal;
        }
        return getNonModalShell();
    }

    /**
	 * Get the active non modal shell. If there isn't one return null.
	 * 
	 * @return Shell
	 */
    public static Shell getNonModalShell() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window == null) {
            IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
            if (windows.length > 0) return windows[0].getShell();
        } else return window.getShell();
        return null;
    }
}
