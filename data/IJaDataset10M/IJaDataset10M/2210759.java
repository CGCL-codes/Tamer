package salto.test.fwk.mvc.ajax.table;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import salto.fwk.mvc.action.GenericAction;

/**
 *
 *
 */
public class WorkWithTableFormAction extends GenericAction {

    /**
     * 
     */
    public WorkWithTableFormAction() {
        super();
    }

    /**
     * @see salto.fwk.mvc.action.GenericAction#process(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.apache.struts.action.ActionForm)
     */
    protected ActionForward process(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, ActionForm form) throws Exception {
        String[] strings = request.getParameterValues("tableColumns");
        return mapping.findForward("success");
    }
}
