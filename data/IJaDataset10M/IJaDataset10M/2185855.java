package com.liferay.portlet.iframe.action;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.PortletConfigImpl;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="ViewAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class ViewAction extends PortletAction {

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        String src = transformSrc(config, req, res);
        if (Validator.isNull(src)) {
            return mapping.findForward("/portal/portlet_not_setup");
        }
        req.setAttribute(WebKeys.IFRAME_SRC, src);
        return mapping.findForward("portlet.iframe.view");
    }

    protected String getSrc(RenderRequest req, RenderResponse res) {
        PortletPreferences prefs = req.getPreferences();
        return prefs.getValue("src", StringPool.BLANK);
    }

    protected String getUserName(RenderRequest req, RenderResponse res) {
        PortletPreferences prefs = req.getPreferences();
        String userName = prefs.getValue("user-name", StringPool.BLANK);
        if (Validator.isNull(userName)) {
            userName = req.getRemoteUser();
        }
        return userName;
    }

    protected String getPassword(RenderRequest req, RenderResponse res) throws PortalException, SystemException {
        PortletPreferences prefs = req.getPreferences();
        String password = prefs.getValue("password", StringPool.BLANK);
        if (Validator.isNull(password)) {
            password = PortalUtil.getUserPassword(req);
        }
        return password;
    }

    protected String transformSrc(PortletConfig config, RenderRequest req, RenderResponse res) throws PortalException, SystemException {
        PortletPreferences prefs = req.getPreferences();
        String src = getSrc(req, res);
        boolean auth = GetterUtil.getBoolean(prefs.getValue("auth", StringPool.BLANK));
        String authType = prefs.getValue("auth-type", StringPool.BLANK);
        String userName = getUserName(req, res);
        String password = getPassword(req, res);
        if (auth) {
            if (authType.equals("basic")) {
                int pos = src.indexOf("://");
                String protocol = src.substring(0, pos + 3);
                String url = src.substring(pos + 3, src.length());
                src = protocol + userName + ":" + password + "@" + url;
            } else {
                ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
                PortletConfigImpl configImpl = (PortletConfigImpl) config;
                String portletId = configImpl.getPortletId();
                Portlet portlet = PortletLocalServiceUtil.getPortletById(themeDisplay.getCompanyId(), portletId);
                src = themeDisplay.getPathMain() + "/" + portlet.getStrutsPath() + "/proxy?p_l_id=" + themeDisplay.getPlid() + "&p_p_id=" + portletId;
            }
        }
        return src;
    }
}
