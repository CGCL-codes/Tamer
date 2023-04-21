package com.jiexplorer.rcp.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import com.jiexplorer.rcp.ui.INodeView;
import com.jiexplorer.rcp.ui.wizards.SequenceRenameWizard;

public class BatchSeqRenameAction extends Action implements IViewActionDelegate {

    private IViewPart view;

    private ISelection selection;

    /**
	 * The constructor.
	 */
    public BatchSeqRenameAction(final String text) {
        super(text);
    }

    /**
	 * The action has been activated. The argument of the method represents the
	 * 'real' action sitting in the workbench UI.
	 * 
	 * @see IWorkbenchWindowActionDelegate#run
	 */
    @Override
    public void run() {
        final SequenceRenameWizard wizard = new SequenceRenameWizard();
        wizard.init(view.getSite().getWorkbenchWindow().getWorkbench(), ((INodeView) view).getStructuredSelection());
        final WizardDialog dialog = new WizardDialog(view.getSite().getShell(), wizard);
        dialog.open();
    }

    /**
	 * Selection in the workbench has been changed. We can change the state of
	 * the 'real' action here if we want, but this can only happen after the
	 * delegate has been created.
	 * 
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
    public void selectionChanged(final IAction action, final ISelection selection) {
        this.selection = selection;
    }

    /**
	 * We can use this method to dispose of any system resources we previously
	 * allocated.
	 * 
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
    public void dispose() {
    }

    public void run(final IAction action) {
        run();
    }

    public void init(final IViewPart view) {
        this.view = view;
    }
}
