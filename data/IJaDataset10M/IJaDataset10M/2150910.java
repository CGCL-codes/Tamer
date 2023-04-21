package com.liferay.samplestrutsportlet.struts.render;

import com.liferay.portal.struts.PortletAction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="UploadSuccessAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class UploadSuccessAction extends PortletAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws Exception {
        _log.info("render " + req.getParameter("file_name"));
        return mapping.findForward("portlet.sample_struts_portlet.upload_success");
    }

    private static Log _log = LogFactory.getLog(UploadAction.class);
}
