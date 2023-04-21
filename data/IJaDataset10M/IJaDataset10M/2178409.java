package com.liferay.portlet.messageboards.action;

import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.Layout;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.messageboards.service.MBBanServiceUtil;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="BanUserAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Michael Young
 *
 */
public class BanUserAction extends PortletAction {

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        String cmd = ParamUtil.getString(actionRequest, Constants.CMD);
        try {
            if (cmd.equals("ban")) {
                banUser(actionRequest);
            } else if (cmd.equals("unban")) {
                unbanUser(actionRequest);
            }
            sendRedirect(actionRequest, actionResponse);
        } catch (Exception e) {
            if (e instanceof PrincipalException) {
                SessionErrors.add(actionRequest, e.getClass().getName());
                setForward(actionRequest, "portlet.message_boards.error");
            } else {
                throw e;
            }
        }
    }

    protected void banUser(ActionRequest actionRequest) throws Exception {
        Layout layout = (Layout) actionRequest.getAttribute(WebKeys.LAYOUT);
        long banUserId = ParamUtil.getLong(actionRequest, "banUserId");
        MBBanServiceUtil.addBan(layout.getPlid(), banUserId);
    }

    protected void unbanUser(ActionRequest actionRequest) throws Exception {
        Layout layout = (Layout) actionRequest.getAttribute(WebKeys.LAYOUT);
        long banUserId = ParamUtil.getLong(actionRequest, "banUserId");
        MBBanServiceUtil.deleteBan(layout.getPlid(), banUserId);
    }
}
