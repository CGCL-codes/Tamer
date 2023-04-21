package ch.unibe.a3ubAdmin.view.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ch.unibe.a3ubAdmin.control.IApplRightManager;
import ch.unibe.a3ubAdmin.exception.ValidationException;
import ch.unibe.a3ubAdmin.model.Application;

/**
 * MyEclipse Struts Creation date: 08-09-2005
 * 
 * XDoclet definition:
 * 
 * @struts:action validate="true"
 * @struts:action-forward name="rights" path="/go/rights" redirect="true"
 */
public class Rights extends Action {

    private Log log = LogFactory.getLog(getClass());

    /**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        boolean authorised = AuthorisationClass.getInstance().checkAuthorisation(request);
        if (!authorised) {
            return mapping.findForward("loginview");
        }
        IApplRightManager manager = (IApplRightManager) request.getSession().getAttribute("manager");
        Application appl = (Application) request.getSession().getAttribute("selectedApplication");
        try {
            appl = manager.loadApplication(appl.getAppId());
            request.getSession().setAttribute("selectedApplication", appl);
        } catch (ValidationException e) {
            log.error("Validationexception occurred: ", e);
            request.getSession().setAttribute("error", e);
            return mapping.findForward("error");
        }
        request.getSession().setAttribute("classes", appl.getRightClasses());
        request.getSession().setAttribute("selectedMethod", "rights");
        if (log.isDebugEnabled()) {
            log.debug("forward to rights");
        }
        return mapping.findForward("rights");
    }
}
