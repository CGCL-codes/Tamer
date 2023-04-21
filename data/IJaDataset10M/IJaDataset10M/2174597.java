package com.liferay.portlet.shindig;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * <a href="ConfigurationActionImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Raymond Augé
 *
 */
public class ConfigurationActionImpl implements ConfigurationAction {

    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        String cmd = ParamUtil.getString(actionRequest, Constants.CMD);
        if (cmd.equals(Constants.UPDATE)) {
            String gadgetUrl = actionRequest.getParameter("gadgetUrl");
            String gadgetHeight = actionRequest.getParameter("gadgetHeight");
            if (Validator.isNotNull(gadgetUrl)) {
                String portletResource = ParamUtil.getString(actionRequest, "portletResource");
                PortletPreferences preferences = PortletPreferencesFactoryUtil.getPortletSetup(actionRequest, portletResource);
                preferences.setValue("gadget-url", gadgetUrl);
                preferences.setValue("gadget-height", gadgetHeight);
                preferences.store();
            }
        }
        actionResponse.sendRedirect(ParamUtil.getString(actionRequest, "redirect"));
    }

    public String render(PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {
        return "/configuration.jsp";
    }
}
