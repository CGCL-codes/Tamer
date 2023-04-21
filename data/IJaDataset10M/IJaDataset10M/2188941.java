package com.metanology.mde.ui.plugin.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import com.metanology.mde.core.codeFactory.LogFactory;
import com.metanology.mde.core.ui.plugin.MDEPlugin;
import com.metanology.mde.utils.Messages;
import com.metanology.mde.ui.pimExplorer.PIMTreeView;

public class OpenPIM implements IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    private static final String viewId = "com.metanology.mde.ui.pimExplorer.PIMTreeView";

    /**
	 * The constructor.
	 */
    public OpenPIM() {
    }

    /**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
    public void run(IAction action) {
        IWorkbench wb = MDEPlugin.getDefault().getWorkbench();
        IWorkbenchPage page;
        PIMTreeView pimView;
        if (wb.getWorkbenchWindows().length > 0 && wb.getWorkbenchWindows()[0].getPages().length > 0) {
            page = wb.getWorkbenchWindows()[0].getPages()[0];
            try {
                page.showView(viewId);
                pimView = (PIMTreeView) page.findView(viewId);
                pimView.handleOpenPIMAction();
            } catch (Exception ex) {
                LogFactory.getLog().error(Messages.get(Messages.ERR_LOAD_PIM_ARG1, ex.toString()));
                ex.printStackTrace();
            }
        }
    }

    /**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
    public void selectionChanged(IAction action, ISelection selection) {
    }

    /**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
    public void dispose() {
    }

    /**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }
}
