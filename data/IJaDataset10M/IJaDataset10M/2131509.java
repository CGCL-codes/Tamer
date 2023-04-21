package uk.ac.bolton.archimate.canvas;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IMenuManager;
import uk.ac.bolton.archimate.editor.diagram.AbstractDiagramEditorContextMenuProvider;
import uk.ac.bolton.archimate.editor.diagram.actions.ResetAspectRatioAction;

/**
 * Provides a context menu.
 * 
 * @author Phillip Beauvoir
 */
public class CanvasEditorContextMenuProvider extends AbstractDiagramEditorContextMenuProvider {

    public static final String ID = "CanvasEditorContextMenuProvider";

    /**
     * Creates a new ContextMenuProvider associated with the given viewer
     * and action registry.
     * 
     * @param viewer the viewer
     * @param registry the action registry
     */
    public CanvasEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
        super(viewer, registry);
    }

    @Override
    public void buildContextMenu(IMenuManager menu) {
        super.buildContextMenu(menu);
        IMenuManager subMenu = menu.findMenuUsingPath("menu_position");
        subMenu.add(actionRegistry.getAction(ResetAspectRatioAction.ID));
    }
}
