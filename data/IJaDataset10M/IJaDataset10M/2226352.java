package de.fraunhofer.isst.axbench.editors.axlmultipage.diagrams.editors;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IMenuManager;

/**
 * @brief  this class realize
 * @author  skaegebein
 * @version  0.9.0
 * @since  0.9.0
 */
public class AXBenchDiagramContextMenuProvider extends org.eclipse.gef.ContextMenuProvider {

    private ActionRegistry actionRegistry;

    public AXBenchDiagramContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
        super(viewer);
        setActionRegistry(registry);
    }

    @Override
    public void buildContextMenu(IMenuManager manager) {
        GEFActionConstants.addStandardActionGroups(manager);
    }

    /**
     * @brief  this method realizes
     * @return
     */
    private ActionRegistry getActionRegistry() {
        return actionRegistry;
    }

    /**
     * @brief  this method realizes
     * @param registry
     */
    private void setActionRegistry(ActionRegistry registry) {
        actionRegistry = registry;
    }
}
