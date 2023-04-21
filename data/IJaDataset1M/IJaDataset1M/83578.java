package org.plazmaforge.studio.dbdesigner.editor;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.actions.ActionFactory;
import org.plazmaforge.studio.dbdesigner.actions.ExportImageAction;
import org.plazmaforge.studio.dbdesigner.actions.ExportProjectAction;
import org.plazmaforge.studio.dbdesigner.actions.GenerateHTMLAction;
import org.plazmaforge.studio.dbdesigner.actions.GenerateSQLAction;
import org.plazmaforge.studio.dbdesigner.actions.ResetRoutingAction;
import org.plazmaforge.studio.dbdesigner.actions.ShowTablesAction;
import org.plazmaforge.studio.dbdesigner.actions.ToggleTableDetailAction;

public class ERDContextMenuProvider extends ContextMenuProvider {

    private ActionRegistry actionRegistry;

    private ERDesignerEditor editorPart;

    public ERDContextMenuProvider(EditPartViewer editpartviewer, ActionRegistry actionregistry, ERDesignerEditor erdesignereditor) {
        super(editpartviewer);
        actionRegistry = actionregistry;
        editorPart = erdesignereditor;
    }

    public void buildContextMenu(IMenuManager imenumanager) {
        GEFActionConstants.addStandardActionGroups(imenumanager);
        IAction action = actionRegistry.getAction(ActionFactory.UNDO.getId());
        imenumanager.appendToGroup("org.eclipse.gef.group.undo", action);
        action = actionRegistry.getAction(ActionFactory.REDO.getId());
        imenumanager.appendToGroup("org.eclipse.gef.group.undo", action);
        action = actionRegistry.getAction(ActionFactory.DELETE.getId());
        if (action.isEnabled()) imenumanager.appendToGroup("org.eclipse.gef.group.edit", action);
        action = actionRegistry.getAction(ToggleTableDetailAction.TOGGLE_TABLE_DETAIL);
        if (action.isEnabled()) imenumanager.appendToGroup("org.eclipse.gef.group.edit", action);
        action = actionRegistry.getAction(ResetRoutingAction.RESET_ROUTING);
        if (action.isEnabled()) imenumanager.appendToGroup("org.eclipse.gef.group.edit", action);
        action = actionRegistry.getAction(ShowTablesAction.SELECT_TABLES);
        imenumanager.add(action);
        imenumanager.add(new Separator());
        action = new ExportImageAction(editorPart);
        imenumanager.add(action);
        imenumanager.add(new Separator());
        action = new ExportProjectAction(editorPart);
        imenumanager.add(action);
        imenumanager.add(new Separator());
        action = new GenerateSQLAction(editorPart);
        imenumanager.add(action);
        imenumanager.add(new Separator());
        action = new GenerateHTMLAction(editorPart);
        imenumanager.add(action);
    }
}
