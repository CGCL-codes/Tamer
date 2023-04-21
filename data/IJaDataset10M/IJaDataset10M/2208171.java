package cx.ath.contribs.klex.forester.actions;

import java.util.Iterator;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPart;
import cx.ath.contribs.klex.forester.model.DialogElement;
import cx.ath.contribs.klex.forester.views.Navigator;

public class ElementExpandAction extends Action implements IViewActionDelegate, IObjectActionDelegate {

    public static final String ID = "ElementExpandAction.action";

    private Navigator view;

    DialogElement element = null;

    IStructuredSelection elements = null;

    public void init(IViewPart view) {
        this.view = (Navigator) view;
    }

    public void run(IAction action) {
        if (elements != null) {
            Iterator it = elements.iterator();
            while (it.hasNext()) {
                element = (DialogElement) it.next();
                view.getTreeViewer().expandToLevel(element, TreeViewer.ALL_LEVELS);
                view.getTreeViewer().refresh();
            }
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            elements = (IStructuredSelection) selection;
            element = (DialogElement) elements.getFirstElement();
        }
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.view = (Navigator) targetPart;
    }
}
