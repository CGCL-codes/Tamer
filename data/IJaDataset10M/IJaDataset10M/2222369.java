package applicationWorkbench;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IMenuManager;

public class MasePlannerContextMenuProvider extends ContextMenuProvider {

    public MasePlannerContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
        super(viewer);
    }

    @Override
    public void buildContextMenu(IMenuManager menu) {
    }
}
