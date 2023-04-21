package devaluator.plugin;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import devaluator.plugin.view.Devaluator;

public class AnalyzerPackagesViewActionDelegate implements IViewActionDelegate {

    private IViewPart view;

    @Override
    public void init(IViewPart view) {
        this.view = view;
    }

    @Override
    public void run(IAction action) {
        try {
            ((Devaluator) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(view.getSite().getId())).setOutput(2);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
    }
}
