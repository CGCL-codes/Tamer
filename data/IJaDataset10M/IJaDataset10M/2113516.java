package net.sf.pdfizer;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class SearchSubjectsActionDelegate implements IActionDelegate {

    private static final Logger LOG = Logger.getLogger(SearchSubjectsActionDelegate.class);

    public void run(IAction action) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
            page.hideView(page.findView(SubjectOccurencesView.ID));
            page.showView(SubjectOccurencesView.ID);
        } catch (PartInitException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }
}
