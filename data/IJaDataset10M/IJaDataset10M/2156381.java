package net.sf.graphiti.ui.editors;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.RetargetAction;

/**
 * Fill the action tool bar. Retargets actions Print, undo/redo, delete,
 * cut/copy/paste, and zoom in/out.
 * 
 * @author Samuel Beaussier
 * @author Nicolas Isch
 * @author Matthieu Wipliez
 * 
 */
public class GraphActionBarContributor extends ActionBarContributor {

    @Override
    protected void buildActions() {
        IWorkbenchWindow iww = getPage().getWorkbenchWindow();
        addRetargetAction((RetargetAction) ActionFactory.COPY.create(iww));
        addRetargetAction((RetargetAction) ActionFactory.CUT.create(iww));
        addRetargetAction((RetargetAction) ActionFactory.DELETE.create(iww));
        addRetargetAction((RetargetAction) ActionFactory.PASTE.create(iww));
        addRetargetAction((RetargetAction) ActionFactory.PRINT.create(iww));
        addRetargetAction((RetargetAction) ActionFactory.REDO.create(iww));
        addRetargetAction((RetargetAction) ActionFactory.UNDO.create(iww));
        addRetargetAction(new ZoomInRetargetAction());
        addRetargetAction(new ZoomOutRetargetAction());
    }

    @Override
    public void contributeToToolBar(IToolBarManager toolBarManager) {
        toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
        toolBarManager.add(getAction(ActionFactory.REDO.getId()));
        toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
        toolBarManager.add(new Separator());
        toolBarManager.add(getAction(ActionFactory.CUT.getId()));
        toolBarManager.add(getAction(ActionFactory.COPY.getId()));
        toolBarManager.add(getAction(ActionFactory.PASTE.getId()));
        toolBarManager.add(new Separator());
        toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN));
        toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT));
        toolBarManager.add(new ZoomComboContributionItem(getPage()));
    }

    @Override
    protected void declareGlobalActionKeys() {
        addGlobalActionKey(ActionFactory.PRINT.getId());
        addGlobalActionKey(ActionFactory.UNDO.getId());
        addGlobalActionKey(ActionFactory.REDO.getId());
        addGlobalActionKey(ActionFactory.DELETE.getId());
        addGlobalActionKey(ActionFactory.CUT.getId());
        addGlobalActionKey(ActionFactory.COPY.getId());
        addGlobalActionKey(ActionFactory.PASTE.getId());
        addGlobalActionKey(ActionFactory.SELECT_ALL.getId());
    }
}
