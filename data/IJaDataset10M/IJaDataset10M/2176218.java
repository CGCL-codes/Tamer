package com.liferay.struts.struts.render;

import com.liferay.struts.struts.form.UnsubscribeForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="UnsubscribeAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class UnsubscribeAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws Exception {
        UnsubscribeForm unsubscribeForm = (UnsubscribeForm) form;
        if (_log.isInfoEnabled()) {
            _log.info(unsubscribeForm.toString());
        }
        return mapping.findForward("portlet.sample_struts_portlet.unsubscribe");
    }

    private static Log _log = LogFactory.getLog(UnsubscribeAction.class);
}
