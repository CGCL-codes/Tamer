package com.semp.jadoma.admin.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import com.semp.jadoma.admin.bean.AdminBean;
import com.semp.jadoma.admin.utils.webapp.Forward;
import com.semp.jadoma.admin.utils.webapp.Keys;
import com.semp.jadoma.admin.utils.webapp.Session;

public abstract class RootAction extends Action {

    private static final Logger logger = Logger.getLogger(RootAction.class);

    /**
	 * Struts method
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            AdminBean adminBean = (AdminBean) request.getSession().getAttribute(Session.BEAN_ADMIN);
            if (adminBean == null && !(this instanceof UnexpireInterface)) {
                logger.info("Session timed out, redirecting to login page");
                return mapping.findForward(Forward.EXPIRE);
            }
            return mapping.findForward(executeChild(form, request, response));
        } catch (Exception e) {
            e.printStackTrace();
            return mapping.findForward(forwardError(request, Keys.ERROR_INTERNAL));
        }
    }

    /**
	 * Child method
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public abstract String executeChild(ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception;

    protected String forwardError(HttpServletRequest request, String errorCode) {
        ActionMessages messages = new ActionMessages();
        messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(errorCode));
        saveErrors(request, messages);
        return Forward.FAILURE;
    }
}
