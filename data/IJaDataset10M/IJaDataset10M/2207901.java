package com.liferay.portlet.announcements.action;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.announcements.AnnouncementsContentException;
import com.liferay.util.LocalizationUtil;
import com.liferay.util.servlet.SessionErrors;
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
        try {
            String cmd = ParamUtil.getString(req, Constants.CMD);
            if (!cmd.equals(Constants.UPDATE)) {
                return;
            }
            String content = ParamUtil.getString(req, "content");
            if (Validator.isNull(content)) {
                throw new AnnouncementsContentException();
            }
            String portletResource = ParamUtil.getString(req, "portletResource");
            String languageId = LanguageUtil.getLanguageId(req);
            PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(req, portletResource, false, false);
            LocalizationUtil.setPrefsValue(prefs, "content", languageId, content);
            prefs.store();
            SessionMessages.add(req, config.getPortletName() + ".doConfigure");
        } catch (AnnouncementsContentException ace) {
            SessionErrors.add(req, ace.getClass().getName());
        }
    }

    public String render(PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        return "/html/portlet/announcements/configuration.jsp";
    }
}
