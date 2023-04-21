package com.liferay.portlet.myglobaltags.action;

import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.util.PortletKeys;
import com.liferay.portlet.PortalPreferences;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.util.servlet.SessionMessages;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="EditPreferencesAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class EditPreferencesAction extends PortletAction {

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        String cmd = ParamUtil.getString(req, Constants.CMD);
        if (!cmd.equals(Constants.UPDATE)) {
            return;
        }
        PortalPreferences prefs = PortletPreferencesFactoryUtil.getPortalPreferences(req);
        String[] entries = StringUtil.split(ParamUtil.getString(req, "entries"));
        prefs.setValues(PortletKeys.MY_GLOBAL_TAGS, "entries", entries);
        SessionMessages.add(req, config.getPortletName() + ".doEdit");
    }

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        return mapping.findForward("portlet.my_global_tags.edit");
    }
}
