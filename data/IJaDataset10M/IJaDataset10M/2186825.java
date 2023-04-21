package org.kablink.teaming.liferay.myaccount;

import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.RenderRequestImpl;
import com.liferay.util.servlet.DynamicServletRequest;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class EditUserAction extends org.kablink.teaming.liferay.enterpriseadmin.EditUserAction {

    public EditUserAction() throws Exception {
        super();
    }

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        if (redirectToLogin(req, res)) {
            return;
        }
        super.processAction(mapping, form, config, req, res);
    }

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        User user = PortalUtil.getUser(req);
        RenderRequestImpl renderReqImpl = (RenderRequestImpl) req;
        DynamicServletRequest dynamicReq = (DynamicServletRequest) renderReqImpl.getHttpServletRequest();
        dynamicReq.setParameter("p_u_i_d", String.valueOf(user.getUserId()));
        return super.render(mapping, form, config, req, res);
    }
}
