package de.beas.explicanto.distribution.web.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import de.beas.explicanto.distribution.config.Constants;
import de.beas.explicanto.distribution.model.User;

/**
 * @author dorel
 *  
 */
public class LoadTestProxyAction extends BaseAction {

    private int count;

    /**
	 *  
	 */
    public LoadTestProxyAction() {
        super();
    }

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse resp) throws Exception {
        User u = (User) req.getSession().getAttribute(Constants.USER_SESSION_KEY);
        if (u == null) {
            System.out.println((count++) + " user not logged in, getting user from db !");
            u = getUserAdministrationService().getUserByUsernameAndPassword(req.getParameter("username"), req.getParameter("password"));
            if (u == null) {
                throw new RuntimeException("User '" + req.getParameter("username") + "' does not exist !");
            }
            req.getSession().setAttribute(Constants.USER_SESSION_KEY, u);
        }
        return new ActionForward(req.getParameter("url"));
    }
}
