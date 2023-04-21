package net.jonbuck.tassoo.ui.handler;

import net.jonbuck.tassoo.ui.dialog.TaskPreferencesDialog;
import net.jonbuck.tassoo.ui.view.TasksView;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * 
 * @author 12052490
 * 
 */
public class TaskColumnPreferencesHandler extends BaseViewActionHandler {

    /**
	 * 
	 */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        TasksView view = (TasksView) getView(event);
        if (view == null) {
            return this;
        }
        new TaskPreferencesDialog(view).open();
        return null;
    }
}
