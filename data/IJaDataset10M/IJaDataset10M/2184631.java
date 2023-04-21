package net.jonbuck.tassoo.ui.handler;

import net.jonbuck.tassoo.ui.view.TasksView;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

/**
 * 
 * 
 * @since 1.0.0
 */
public class ExpandAllHandler extends BaseViewActionHandler {

    /**
	 * 
	 */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        TasksView view = (TasksView) getView(event);
        view.getViewer().expandAll();
        return null;
    }
}
