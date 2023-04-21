package com.liferay.portlet.portletconfiguration.action;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.struts.JSONAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.CachePortlet;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import java.util.Locale;
import javax.portlet.PortletPreferences;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.json.JSONObject;

/**
 * <a href="UpdateLookAndFeelAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class UpdateLookAndFeelAction extends JSONAction {

    public String getJSON(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession ses = req.getSession();
        ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
        Layout layout = themeDisplay.getLayout();
        PermissionChecker permissionChecker = themeDisplay.getPermissionChecker();
        String portletId = ParamUtil.getString(req, "portletId");
        if (!PortletPermissionUtil.contains(permissionChecker, themeDisplay.getPlid(), portletId, ActionKeys.CONFIGURATION)) {
            return null;
        }
        PortletPreferences portletSetup = PortletPreferencesFactoryUtil.getPortletSetup(layout, portletId);
        String css = ParamUtil.getString(req, "css");
        if (_log.isDebugEnabled()) {
            _log.debug("Updating css " + css);
        }
        JSONObject jsonObj = new JSONObject(css);
        JSONObject portletData = jsonObj.getJSONObject("portletData");
        jsonObj.remove("portletData");
        css = jsonObj.toString();
        boolean useCustomTitle = portletData.getBoolean("useCustomTitle");
        boolean showBorders = portletData.getBoolean("showBorders");
        long linkToPlid = GetterUtil.getLong(portletData.getString("portletLinksTarget"));
        JSONObject titles = portletData.getJSONObject("titles");
        Locale[] locales = LanguageUtil.getAvailableLocales();
        for (int i = 0; i < locales.length; i++) {
            String languageId = LocaleUtil.toLanguageId(locales[i]);
            String title = null;
            if (titles.has(languageId)) {
                title = GetterUtil.getString(titles.getString(languageId));
            }
            if (Validator.isNotNull(title)) {
                portletSetup.setValue("portlet-setup-title-" + languageId, title);
            } else {
                portletSetup.reset("portlet-setup-title-" + languageId);
            }
        }
        portletSetup.setValue("portlet-setup-use-custom-title", String.valueOf(useCustomTitle));
        portletSetup.setValue("portlet-setup-show-borders", String.valueOf(showBorders));
        if (linkToPlid > 0) {
            portletSetup.setValue("portlet-setup-link-to-plid", String.valueOf(linkToPlid));
        } else {
            portletSetup.reset("portlet-setup-link-to-plid");
        }
        portletSetup.setValue("portlet-setup-css", css);
        portletSetup.store();
        CachePortlet.clearResponse(ses, layout.getPrimaryKey(), portletId, LanguageUtil.getLanguageId(req));
        return null;
    }

    private static Log _log = LogFactory.getLog(UpdateLookAndFeelAction.class);
}
