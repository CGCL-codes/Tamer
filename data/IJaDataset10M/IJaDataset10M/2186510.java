package uk.ac.bolton.archimate.editor.ui.components;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

/**
 * Manages de-activating and restoring of editing type global Action Handlers setting them to null
 * so that they cannot be invoked by the user when editing text in a text cell editor.<p>
 * This ensures that edit key shortcuts are bound to the cell editor, not the application. 
 * 
 * @author Phillip Beauvoir
 */
public class CellEditorGlobalActionHandler {

    private IActionBars fActionBars;

    private String[] actionIds = new String[] { ActionFactory.CUT.getId(), ActionFactory.COPY.getId(), ActionFactory.PASTE.getId(), ActionFactory.DELETE.getId(), ActionFactory.SELECT_ALL.getId(), ActionFactory.FIND.getId(), ActionFactory.RENAME.getId(), ActionFactory.UNDO.getId(), ActionFactory.REDO.getId() };

    private IAction[] actions = new IAction[actionIds.length];

    public CellEditorGlobalActionHandler() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (window != null && window.getActivePage() != null && window.getActivePage().getActivePart() != null) {
            IWorkbenchPartSite site = window.getActivePage().getActivePart().getSite();
            if (site instanceof IEditorSite) {
                fActionBars = ((IEditorSite) site).getActionBars();
            } else if (site instanceof IViewSite) {
                fActionBars = ((IViewSite) site).getActionBars();
            }
        }
    }

    /**
     * Clear the Global Action Handlers for the Active Part
     */
    public void clearGlobalActions() {
        if (fActionBars != null) {
            for (int i = 0; i < actionIds.length; i++) {
                actions[i] = fActionBars.getGlobalActionHandler(actionIds[i]);
            }
            for (int i = 0; i < actionIds.length; i++) {
                fActionBars.setGlobalActionHandler(actionIds[i], null);
            }
            fActionBars.updateActionBars();
        }
    }

    /**
     * Restore the Global Action Handlers that were set to null
     */
    public void restoreGlobalActions() {
        if (fActionBars != null) {
            for (int i = 0; i < actionIds.length; i++) {
                fActionBars.setGlobalActionHandler(actionIds[i], actions[i]);
            }
            fActionBars.updateActionBars();
        }
    }
}
