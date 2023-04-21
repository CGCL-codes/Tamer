package salto.fwk.mvc.ajax.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import salto.fwk.mvc.action.GenericAction;
import salto.fwk.mvc.ajax.util.AjaxUtil;

/**
 * @author Jerome DENOLF / Salto Consulting
 *
 */
public abstract class AbstractUploadAction extends GenericAction {

    protected ActionForward process(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionForward theForward = processUpload(mapping, request, response);
        if (theForward == null) {
            theForward = getRefreshForward();
        }
        ActionMessages err = (ActionMessages) request.getAttribute(Globals.ERROR_KEY);
        if (err != null && err.size() > 0) {
            return theForward;
        }
        AjaxUtil.addInnerHtml(request, "divScript", "<div>&nbsp;</div><script typeLoad='direct'>unMaskScreen();Element.hide('divScript');</script>");
        return getRefreshForward();
    }

    /** Method to implement by an Upload.
    * @param mapping The ActionMapping used to select this instance
    * @param request The non-HTTP request we are processing
    * @param response The non-HTTP response we are creating
    * @return ActionForward (null forward to RefreshForward)
    * @throws Exception
    */
    protected abstract ActionForward processUpload(ActionMapping mapping, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
