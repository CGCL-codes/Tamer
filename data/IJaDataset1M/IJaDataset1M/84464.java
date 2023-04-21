package eu.redseeds.sc.editor.rsl.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import org.eclipse.jface.dialogs.MessageDialog;
import eu.redseeds.sc.editor.rsl.editors.domain.NotionEditorInput;

public class NotionEditorOpenAction extends Action implements ISelectionChangedListener, IWorkbenchWindowActionDelegate {

    private IWorkbenchWindow window;

    private IWorkbenchSite wSite;

    private static String ID_NOTION_EDITOR = "eu.redseeds.sc.editor.rsl.editors.NotionEditor";

    public NotionEditorOpenAction() {
    }

    /**
	 * The constructor.
	 */
    public NotionEditorOpenAction(IWorkbenchSite site) {
        wSite = site;
        setEnabled(true);
    }

    /**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
    @Override
    public void run() {
        try {
            IWorkbenchPage activePage = (wSite != null ? wSite.getPage() : window.getActivePage());
            activePage.openEditor(new NotionEditorInput(), ID_NOTION_EDITOR, false);
        } catch (PartInitException e) {
            MessageDialog.openInformation(window.getShell(), "Current Software Case Browser", e.toString());
        }
    }

    public void calculateEnabled(ISelection selection) {
        setEnabled(true);
    }

    public void selectionChanged(SelectionChangedEvent event) {
        ISelection selection = event.getSelection();
        calculateEnabled(selection);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void init(IWorkbenchWindow window) {
        this.window = window;
    }

    @Override
    public void run(IAction action) {
        run();
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
    }
}
