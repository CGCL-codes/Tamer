package tudu.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Show the Todo Lists belonging to the current user.
 * 
 * @author Julien Dubois
 */
public class ShowTodoListsAction extends Action {

    private final Log log = LogFactory.getLog(ShowTodoListsAction.class);

    /**
     * Show the Todo Lists main page.
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Execute show action");
        return mapping.findForward("show");
    }
}
