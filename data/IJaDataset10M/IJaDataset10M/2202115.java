package com.liferay.portlet.breadcrumb.action;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.util.servlet.SessionMessages;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * <a href="ConfigurationActionImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ConfigurationActionImpl implements ConfigurationAction {

    public void processAction(PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        String cmd = ParamUtil.getString(req, Constants.CMD);
        if (!cmd.equals(Constants.UPDATE)) {
            return;
        }
        String displayStyle = ParamUtil.getString(req, "displayStyle");
        String portletResource = ParamUtil.getString(req, "portletResource");
        PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(req, portletResource, true, true);
        prefs.setValue("display-style", displayStyle);
        prefs.store();
        SessionMessages.add(req, config.getPortletName() + ".doConfigure");
    }

    public String render(PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        return "/html/portlet/breadcrumb/configuration.jsp";
    }
}
