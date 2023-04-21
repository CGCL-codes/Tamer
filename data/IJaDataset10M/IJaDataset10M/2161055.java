package com.liferay.portlet.journalcontent.action;

import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.model.Layout;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.portlet.journal.service.JournalContentSearchLocalServiceUtil;
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

    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        String cmd = ParamUtil.getString(actionRequest, Constants.CMD);
        if (!cmd.equals(Constants.UPDATE)) {
            return;
        }
        long groupId = ParamUtil.getLong(actionRequest, "groupId");
        String articleId = ParamUtil.getString(actionRequest, "articleId").toUpperCase();
        String templateId = ParamUtil.getString(actionRequest, "templateId").toUpperCase();
        boolean showAvailableLocales = ParamUtil.getBoolean(actionRequest, "showAvailableLocales");
        boolean enableRatings = ParamUtil.getBoolean(actionRequest, "enableRatings");
        boolean enableComments = ParamUtil.getBoolean(actionRequest, "enableComments");
        boolean enableCommentRatings = ParamUtil.getBoolean(actionRequest, "enableCommentRatings");
        boolean enableLastUpdate = ParamUtil.getBoolean(actionRequest, "enableLastUpdate");
        boolean enableViewHits = ParamUtil.getBoolean(actionRequest, "enableViewHits");
        boolean enableEmailToFriend = ParamUtil.getBoolean(actionRequest, "enableEmailToFriend");
        boolean enablePrint = ParamUtil.getBoolean(actionRequest, "enablePrint");
        boolean exportTo = ParamUtil.getBoolean(actionRequest, "exportTo");
        boolean enableSocialBookmark = ParamUtil.getBoolean(actionRequest, "enableSocialBookmark");
        boolean enableTextToSpeech = ParamUtil.getBoolean(actionRequest, "enableTextToSpeech");
        String portletResource = ParamUtil.getString(actionRequest, "portletResource");
        PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(actionRequest, portletResource);
        prefs.setValue("group-id", String.valueOf(groupId));
        prefs.setValue("article-id", articleId);
        prefs.setValue("template-id", templateId);
        prefs.setValue("show-available-locales", String.valueOf(showAvailableLocales));
        prefs.setValue("enable-ratings", String.valueOf(enableRatings));
        prefs.setValue("enable-comments", String.valueOf(enableComments));
        prefs.setValue("enable-comment-ratings", String.valueOf(enableCommentRatings));
        prefs.setValue("enable-last-update", String.valueOf(enableLastUpdate));
        prefs.setValue("enable-view-hits", String.valueOf(enableViewHits));
        prefs.setValue("enable-email-to-friend", String.valueOf(enableEmailToFriend));
        prefs.setValue("enable-print", String.valueOf(enablePrint));
        prefs.setValue("export-to", String.valueOf(exportTo));
        prefs.setValue("enable-social-bookmark", String.valueOf(enableSocialBookmark));
        prefs.setValue("enable-Text-to-Speech", String.valueOf(enableTextToSpeech));
        prefs.store();
        updateContentSearch(actionRequest, portletResource, articleId);
        actionResponse.sendRedirect(ParamUtil.getString(actionRequest, "redirect"));
    }

    public String render(PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {
        return "/html/portlet/journal_content/configuration.jsp";
    }

    protected void updateContentSearch(ActionRequest actionRequest, String portletResource, String articleId) throws Exception {
        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
        Layout layout = themeDisplay.getLayout();
        JournalContentSearchLocalServiceUtil.updateContentSearch(layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(), portletResource, articleId, true);
    }
}
