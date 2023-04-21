package org.sqlexp.actions;

import java.io.File;
import java.io.IOException;
import org.eclipse.swt.widgets.Shell;
import org.sqlexp.controls.actions.Action;
import org.sqlexp.controls.util.UiApplicationContext;
import org.sqlexp.controls.util.UiMessageUtils;
import org.sqlexp.sql.SqlDatabase;
import org.sqlexp.sql.SqlServer;
import org.sqlexp.view.editorstab.EditorsTabView;
import org.sqlexp.view.modals.SaveAsLocalScriptView;
import org.sqlexp.view.query.SqlQueryView;
import org.sqlexp.view.server.ServerView;

/**
 * Save as a local script action.
 * @author Matthieu Réjou
 */
public class ActionFileSaveAsLocalScript extends Action {

    @Override
    public final boolean isEnabled() {
        return EditorsTabView.getInstance().isTabItemFocused();
    }

    @Override
    public final void perform() {
        Shell shell = UiApplicationContext.getInstance().getMainShell();
        ServerView serverView = ServerView.getInstance();
        SqlServer server = serverView.getServer(true);
        SqlDatabase database = serverView.getDatabase(true);
        SaveAsLocalScriptView view = new SaveAsLocalScriptView(shell, getText(), getImage(), server, database);
        File file = view.getFile();
        if (file == null) {
            return;
        }
        try {
            EditorsTabView.getInstance().getCurrentQueryView().save(file.getAbsolutePath());
        } catch (IOException e) {
            UiMessageUtils.showError(SqlQueryView.class, "saveError", e);
        }
    }
}
