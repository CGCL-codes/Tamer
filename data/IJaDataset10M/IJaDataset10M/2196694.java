package tudu.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.jdom.JDOMException;
import tudu.domain.model.TodoList;
import tudu.service.TodoListsManager;
import tudu.web.form.RestoreTodoListForm;

/**
 * Restore a Todo List.
 * 
 * @author Julien Dubois
 */
public class RestoreTodoListAction extends TuduDispatchAction {

    private final Log log = LogFactory.getLog(RestoreTodoListAction.class);

    private TodoListsManager todoListsManager = null;

    public void setTodoListsManager(TodoListsManager todoListsManager) {
        this.todoListsManager = todoListsManager;
    }

    /**
     * Display the main screen for restoring a Todo List.
     */
    @Override
    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Execute display action");
        RestoreTodoListForm restoreTodoListForm = (RestoreTodoListForm) form;
        String listId = restoreTodoListForm.getListId();
        TodoList todoList = todoListsManager.findTodoList(listId);
        request.setAttribute("todoList", todoList);
        if (restoreTodoListForm.getRestoreChoice() == null) {
            restoreTodoListForm.setRestoreChoice("create");
        }
        return mapping.findForward("restore");
    }

    /**
     * Restore a Todo List.
     */
    public ActionForward restore(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Execute restore action");
        ActionMessages errors = form.validate(mapping, request);
        if (errors.size() != 0) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        RestoreTodoListForm restoreTodoListForm = (RestoreTodoListForm) form;
        try {
            todoListsManager.restoreTodoList(restoreTodoListForm.getRestoreChoice(), restoreTodoListForm.getListId(), restoreTodoListForm.getBackupFile().getInputStream());
        } catch (FileNotFoundException e) {
            log.info("FileNotFoundException : " + e.getMessage());
            ActionMessage message = new ActionMessage("restore.file.error");
            errors.add(ActionMessages.GLOBAL_MESSAGE, message);
        } catch (IOException e) {
            log.info("IOException : " + e.getMessage());
            ActionMessage message = new ActionMessage("restore.file.error");
            errors.add(ActionMessages.GLOBAL_MESSAGE, message);
            if (log.isDebugEnabled()) {
                e.printStackTrace();
            }
        } catch (JDOMException e) {
            log.info("JDOMException : " + e.getMessage());
            ActionMessage message = new ActionMessage("restore.file.error");
            errors.add(ActionMessages.GLOBAL_MESSAGE, message);
            if (log.isDebugEnabled()) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            log.info("Exception : " + e.getMessage());
            ActionMessage message = new ActionMessage("restore.file.error");
            errors.add(ActionMessages.GLOBAL_MESSAGE, message);
            if (log.isDebugEnabled()) {
                e.printStackTrace();
            }
        }
        if (errors.size() != 0) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        return mapping.findForward("showTodosAction");
    }

    /**
     * @see tudu.web.TuduDispatchAction#cancel(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Execute cancel action");
        return mapping.findForward("showTodosAction");
    }
}
