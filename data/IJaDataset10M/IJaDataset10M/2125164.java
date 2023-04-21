package com.liferay.portlet.language.action;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Contact;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.User;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.admin.util.AdminUtil;
import com.liferay.util.ListUtil;
import java.util.List;
import java.util.Locale;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.Globals;
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

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws Exception {
        HttpServletRequest httpReq = PortalUtil.getHttpServletRequest(req);
        HttpServletResponse httpRes = PortalUtil.getHttpServletResponse(res);
        HttpSession httpSes = httpReq.getSession();
        ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
        Layout layout = themeDisplay.getLayout();
        String languageId = ParamUtil.getString(req, "languageId");
        Locale locale = LocaleUtil.fromLanguageId(languageId);
        List availableLocales = ListUtil.fromArray(LanguageUtil.getAvailableLocales());
        if (availableLocales.contains(locale)) {
            if (themeDisplay.isSignedIn()) {
                User user = themeDisplay.getUser();
                Contact contact = user.getContact();
                AdminUtil.updateUser(req, user.getUserId(), user.getScreenName(), user.getEmailAddress(), languageId, user.getTimeZoneId(), user.getGreeting(), user.getComments(), contact.getSmsSn(), contact.getAimSn(), contact.getIcqSn(), contact.getJabberSn(), contact.getMsnSn(), contact.getSkypeSn(), contact.getYmSn());
            }
            httpSes.setAttribute(Globals.LOCALE_KEY, locale);
            LanguageUtil.updateCookie(httpRes, locale);
        }
        String redirect = ParamUtil.getString(req, "redirect");
        if (Validator.isNull(redirect)) {
            redirect = PortalUtil.getLayoutURL(layout, themeDisplay);
        }
        res.sendRedirect(redirect);
    }

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig config, RenderRequest req, RenderResponse res) throws Exception {
        return mapping.findForward("portlet.language.view");
    }
}
