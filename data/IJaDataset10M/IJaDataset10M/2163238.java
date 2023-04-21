package org.sqlexp.actions;

import org.sqlexp.controls.actions.Action;
import org.sqlexp.view.editorstab.EditorsTabView;
import org.sqlexp.view.modals.PrintDialogView;
import org.sqlexp.view.query.SqlQueryView;

/**
 * Close action.
 * @author Matthieu Réjou
 */
public class ActionPrint extends Action {

    @Override
    public final boolean isEnabled() {
        return EditorsTabView.getInstance().isTabItemFocused();
    }

    @Override
    public final void perform() {
        SqlQueryView queryView = EditorsTabView.getInstance().getCurrentQueryView();
        new PrintDialogView().open(queryView.getDocumentTitle(), queryView.getTextEditor());
    }
}
