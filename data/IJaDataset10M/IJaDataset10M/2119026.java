package jreceiver.client.common.struts;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.RedirectingActionForward;
import jreceiver.common.JRecException;
import jreceiver.common.rec.security.User;
import jreceiver.util.HelperServlet;

/**
 * Base action bean to add/edit a single record.
 *
 * @author Reed Esau
 * @version $Revision: 1.4 $ $Date: 2002/09/24 18:55:24 $
 */
public abstract class JRecEditAction extends JRecAction {

    /**
    * load any special stuff in to the form, usually init'd from param list
    */
    public abstract void onLoad(User user, JRecEditForm edit_form, HttpServletRequest req, ActionErrors errors) throws JRecException;

    /** */
    public abstract void onSave(User user, JRecEditForm edit_form, HttpSession session, ActionErrors errors) throws JRecException;

    /**
     * subclasses should override if they wish to provide parameters
     * to the specified forward
     */
    protected void getForwardParams(HttpServletRequest req, String forward_name, Map params) {
    }

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     * <p>
     *
     * @param mapping The ActionMapping used to select this instance
     * @param action_form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward execute(ActionMapping mapping, ActionForm action_form, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        log.debug("execute\n" + HelperServlet.dumpParams(req) + "\n");
        ActionForward forward = null;
        if (isCancelled(req)) {
            log.debug("user hit cancel button");
            action_form.reset(mapping, req);
            forward = mapping.findForward(FORWARD_CANCEL);
        } else {
            ActionErrors errors = new ActionErrors();
            JRecEditForm form = (JRecEditForm) action_form;
            boolean is_submit = form.getSubmitSave() != null;
            User user = getUser(req);
            log.debug("is_submit=" + is_submit);
            form.setSubmitSave(null);
            try {
                if (is_submit) {
                    log.debug("saving");
                    onSave(user, form, req.getSession(), errors);
                    forward = getForward(req, mapping, FORWARD_SUCCESS);
                } else {
                    log.debug("loading");
                    onLoad(user, form, req, errors);
                }
            } catch (JRecException e) {
                log.error("jrec-problem loading or saving", e);
                reportError(errors, "edit_action.general_error", e);
            }
            if (!errors.empty()) saveErrors(req, errors);
        }
        return forward != null ? forward : mapping.findForward(FORWARD_CONTINUE);
    }

    /**
     * Obtain and embellish the forward as needed with parameters requested
     * by the subclass.
     */
    private ActionForward getForward(HttpServletRequest req, ActionMapping mapping, String forward_name) throws JRecException {
        Map params = new HashMap();
        getForwardParams(req, forward_name, params);
        StringBuffer path = new StringBuffer(128);
        ActionForward forward = mapping.findForward(forward_name);
        if (forward == null) throw new JRecException("no forward defined");
        path.append(forward.getPath());
        Iterator it = params.entrySet().iterator();
        boolean is_first = true;
        while (it.hasNext()) {
            if (is_first) {
                path.append('?');
                is_first = false;
            } else path.append('&');
            Map.Entry en = (Map.Entry) it.next();
            path.append(en.getKey()).append('=').append(en.getValue());
        }
        if (log.isDebugEnabled()) log.debug("getForward: path=" + path);
        return new RedirectingActionForward(path.toString());
    }

    /**
     * logging object
     */
    protected static Log log = LogFactory.getLog(JRecEditAction.class);
}
