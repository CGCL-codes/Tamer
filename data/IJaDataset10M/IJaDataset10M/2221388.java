package workspace.contextMenu;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import tree.HaxeTree;
import tree.utils.ReferencesListBuilder;
import workspace.Activator;
import workspace.HashMapForLists;
import workspace.NodeLink;
import workspace.WorkspaceUtils;
import workspace.elements.HaxeProject;
import workspace.views.CallHierarchyView;

public class CallHierarchyAction extends HxEditorMenuAction {

    CallHierarchyView view = null;

    private HashMapForLists<NodeLink> makeCallsList(final HaxeTree node) {
        ReferencesListBuilder builder = new ReferencesListBuilder();
        builder.visit(node);
        return builder.getResult();
    }

    @Override
    public void run(IAction action) {
        try {
            view = (CallHierarchyView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(CallHierarchyView.ID);
            HaxeTree node = getCurrentNode();
            String fileName = Activator.getInstance().getCurrentFile().getName();
            HaxeProject proj = Activator.getInstance().getCurrentHaxeProject();
            if (!WorkspaceUtils.isNodeValidForCallAnalysis(node)) {
                node = WorkspaceUtils.getValidNodeForCallAnalysis(node);
            }
            if (node != null) {
                view.init("", node, makeCallsList(node));
            }
        } catch (PartInitException e) {
            System.out.println("Couldn't open the view, id: " + CallHierarchyView.ID);
            Activator.logger.error("CallHierarchyAction.run: {}", e.getMessage());
        } catch (ClassCastException e) {
            System.out.println(e.getMessage());
            Activator.logger.error("CallHierarchyAction.run: {}", e.getMessage());
        }
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
    }
}
