package cx.ath.contribs.klex.forester.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import cx.ath.contribs.klex.forester.model.DialogElement;
import cx.ath.contribs.klex.forester.views.Navigator;

public class ElementPasteRecursiveAction extends Action implements IObjectActionDelegate {

    DialogElement element = null;

    IWorkbenchPart targetPart = null;

    Navigator sourceComponent = null;

    Navigator targetComponent = null;

    DialogElement sourceElement = null;

    DialogElement targetElement = null;

    public ElementPasteRecursiveAction() {
    }

    public void run(IAction action) {
        targetElement.paste(true, sourceComponent, targetComponent);
    }

    public void selectionChanged(IAction action, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSelection = (IStructuredSelection) selection;
            targetElement = (DialogElement) structuredSelection.getFirstElement();
        }
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        this.targetPart = targetPart;
        sourceComponent = Navigator.sourceComponent;
        targetComponent = (Navigator) targetPart;
    }
}
