package net.jonbuck.tassoo.ui.handler;

import net.jonbuck.tassoo.model.Task;
import net.jonbuck.tassoo.persistence.dao.TassooDao;
import net.jonbuck.tassoo.persistence.dao.TassooDaoImpl;
import net.jonbuck.tassoo.persistence.dao.util.TassooDaoHelperUtil;
import net.jonbuck.tassoo.util.DateHelperUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * <p>
 * <b>Marks the currently selected task as complete by setting the
 * <em>Status</em> field to the status where the associated percentage is 100
 * and also sets the <em>CompletedDate</em> field to todays date.</b>
 * </p>
 * 
 * @since 1.0.0
 */
public class MarkCompleteHandler extends AbstractHandler implements IHandler {

    private TassooDao tassooDao = TassooDaoImpl.getInstance();

    /**
	 * 
	 */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
        Task currentTask = (Task) selection.getFirstElement();
        currentTask.setStatus(TassooDaoHelperUtil.getStatusByPercentageValue(tassooDao, 100));
        currentTask.setCompletedDate(DateHelperUtils.addDays(0));
        return null;
    }
}
