package uk.ac.bolton.archimate.canvas;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import uk.ac.bolton.archimate.editor.actions.ArchimateEditorActionFactory;
import uk.ac.bolton.archimate.editor.diagram.AbstractDiagramEditorActionBarContributor;
import uk.ac.bolton.archimate.editor.diagram.actions.BorderColorAction;
import uk.ac.bolton.archimate.editor.diagram.actions.LockObjectAction;
import uk.ac.bolton.archimate.editor.diagram.actions.ResetAspectRatioAction;
import uk.ac.bolton.archimate.editor.diagram.actions.TextPositionAction;

/**
 * Action Bar contributor for Canvas Editor
 * 
 * @author Phillip Beauvoir
 */
public class CanvasEditorActionBarContributor extends AbstractDiagramEditorActionBarContributor {

    @Override
    protected IMenuManager contributeToEditMenu(IMenuManager menuManager) {
        IMenuManager editMenu = super.contributeToEditMenu(menuManager);
        IMenuManager textPositionMenu = new MenuManager(Messages.CanvasEditorActionBarContributor_0);
        for (String id : TextPositionAction.ACTION_IDS) {
            textPositionMenu.add(getAction(id));
        }
        editMenu.appendToGroup(GROUP_EDIT_MENU, textPositionMenu);
        editMenu.appendToGroup(GROUP_EDIT_MENU, getAction(BorderColorAction.ID));
        editMenu.insertAfter(ArchimateEditorActionFactory.RENAME.getId(), new Separator("lock"));
        editMenu.appendToGroup("lock", getAction(LockObjectAction.ID));
        return editMenu;
    }

    @Override
    protected IMenuManager createViewMenu(IMenuManager menuManager) {
        IMenuManager viewMenu = super.createViewMenu(menuManager);
        IMenuManager subMenu = viewMenu.findMenuUsingPath("menu_position");
        subMenu.add(getAction(ResetAspectRatioAction.ID));
        return viewMenu;
    }

    @Override
    public void contributeToToolBar(IToolBarManager toolBarManager) {
        super.contributeToToolBar(toolBarManager);
        toolBarManager.appendToGroup(GROUP_TOOLBAR_END, getAction(ResetAspectRatioAction.ID));
    }
}
