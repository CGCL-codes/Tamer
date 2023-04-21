package com.liferay.portlet.sitemap.action;

import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.Constants;
import com.liferay.util.ParamUtil;
import com.liferay.util.servlet.SessionMessages;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="EditPreferencesAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class EditPreferencesAction extends PortletAction {

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        String cmd = ParamUtil.getString(req, Constants.CMD);
        if (!cmd.equals(Constants.UPDATE)) {
            return;
        }
        String portletTitle = ParamUtil.getString(req, "portlet_title");
        boolean showPortletBorders = ParamUtil.getBoolean(req, "show_portlet_borders");
        PortletPreferences prefs = req.getPreferences();
        prefs.setValue("portlet-title", portletTitle);
        prefs.setValue("show-portlet-borders", Boolean.toString(showPortletBorders));
        prefs.store();
        SessionMessages.add(req, config.getPortletName() + ".doEdit");
    }

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        return mapping.findForward("portlet.site_map.edit");
    }
}
