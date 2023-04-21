package de.uka.aifb.owl.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import de.uka.aifb.owl.diagramManager.DiagramManager;
import de.uka.aifb.owl.diagramManager.DiagramManagerFactory;
import de.uka.aifb.owl.gui.navigator.DiagramTreeElement;
import de.uka.aifb.owl.gui.util.DiagramUtil;

public class CreateViewAction extends Action implements IWorkbenchWindowActionDelegate {

    private ISelection _actualSelection;

    /** Called when the action is created. */
    public void init(IWorkbenchWindow window) {
    }

    /** Called when the action is discarded. */
    public void dispose() {
    }

    /** Called when the action is executed. */
    public void run(IAction action) {
        DiagramUtil.checkDiagramPerspective(true);
        DiagramTreeElement diagramTreeElement = (DiagramTreeElement) ((StructuredSelection) _actualSelection).getFirstElement();
        DiagramManager diagramManager = DiagramManagerFactory.getDiagramManagerInstance(diagramTreeElement.getProjectName(), diagramTreeElement.getUri());
        String viewName = diagramManager.createNewView(diagramTreeElement.getUri());
        diagramManager.openDiagram(viewName);
    }

    /** Called when objects in the editor are selected or deselected. */
    public void selectionChanged(IAction action, ISelection selection) {
        _actualSelection = selection;
    }
}
