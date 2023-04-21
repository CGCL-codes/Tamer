package com.liferay.samplestrutsportlet.struts.render;

import com.liferay.samplestrutsportlet.SampleException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="XAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class XAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws Exception {
        _log.info("render " + req.getParameter("x_param"));
        String exception = req.getParameter("render_exception");
        if ((exception != null) && (exception.equals("true"))) {
            throw new SampleException();
        }
        return mapping.findForward("portlet.sample_struts_portlet.x");
    }

    private static Log _log = LogFactory.getLog(XAction.class);
}
