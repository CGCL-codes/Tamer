package salto.fwk.mvc.ajax.action;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.NotImplementedException;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import salto.fwk.mvc.action.GenericAction;
import salto.fwk.mvc.ajax.model.JavascriptAction;
import salto.fwk.mvc.ajax.util.AjaxUtil;

/**
 * Base class for an action used in a Wizard
 * 
 * @author eloiez / salto-consulting.com
 */
public abstract class AbstractWizardAction extends GenericAction {

    /**
     * @see salto.fwk.mvc.action.GenericAction#process(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionForm)
     */
    protected final ActionForward process(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {
        throw new NotImplementedException("This method shoud have never been called");
    }

    /**
	 * @see salto.fwk.mvc.action.GenericAction#process(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionForm)
	 */
    protected final ActionForward process(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws Exception {
        ActionForward contentOnglet = processWizard(mapping, request, response, form);
        if (contentOnglet == null) {
            contentOnglet = getRefreshForward();
        }
        ActionMessages err = (ActionMessages) request.getAttribute(Globals.ERROR_KEY);
        if (err != null && err.size() > 0) {
            return contentOnglet;
        }
        Map parameters = new HashMap(1);
        parameters.put("increment", Wizard.getIncrement(request));
        JavascriptAction javascriptAction = new JavascriptAction("Salto.wizardNext", null, parameters);
        AjaxUtil.addResponse(request, javascriptAction);
        return contentOnglet;
    }

    /**
     * Method to implement by Wizard steps.
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
	 * @return ActionForward (null forward to RefreshForward)
	 * @throws Exception
	 */
    protected abstract ActionForward processWizard(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws Exception;
}
