package it.webscience.kpeople.web.portlet.document.action;

import javax.portlet.ActionRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.JavaConstants;

/**
 * Struts action per la visualizzazione dei documenti.
 */
public class ViewAction extends Action {

    /** logger. */
    private static Log logger = LogFactoryUtil.getLog(ViewAction.class);

    /**
     * <p>Process the specified non-HTTP request, and create the
     * corresponding non-HTTP response (or forward to another web
     * component that will create it), with provision for handling
     * exceptions thrown by the business logic.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param req The non-HTTP request we are processing
     * @param res The non-HTTP response we are creating
     *
     * @exception Exception if the application business logic throws
     * an exception.
     * @return action forward
     */
    @Override
    public final ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (req.getAttribute(JavaConstants.JAVAX_PORTLET_REQUEST) instanceof ActionRequest) {
            return this.processAction(mapping, form, req, res);
        } else {
            return this.render(mapping, form, req, res);
        }
    }

    /**
     * metodo render.
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param req The non-HTTP request we are processing
     * @param res The non-HTTP response we are creating
     * @return action forward
     */
    private ActionForward render(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) {
        return mapping.findForward("/document-browser/view");
    }

    /**
     * Restituisce la view della portlet.
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param req The non-HTTP request we are processing
     * @param res The non-HTTP response we are creating
     * @return action forward
     * @throws Exception if the application business logic throws
     * an exception.
     */
    private ActionForward processAction(final ActionMapping mapping, final ActionForm form, final HttpServletRequest req, final HttpServletResponse res) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug("processAction");
        }
        return mapping.findForward("/document-browser/view");
    }
}
