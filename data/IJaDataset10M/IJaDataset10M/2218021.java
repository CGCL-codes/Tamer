package com.liferay.portlet.nestedportlets.action;

import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.LayoutTemplate;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.Theme;
import com.liferay.portal.service.LayoutTemplateLocalServiceUtil;
import com.liferay.portal.struts.PortletAction;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
 * @author Berentey Zsolt
 * @author Jorge Ferrer
 *
 */
public class ViewAction extends PortletAction {

    public ActionForward render(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {
        ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);
        Portlet portlet = (Portlet) renderRequest.getAttribute(WebKeys.RENDER_PORTLET);
        PortletPreferences preferences = PortletPreferencesFactoryUtil.getPortletSetup(renderRequest, portlet.getPortletId());
        String layoutTemplateId = preferences.getValue("layout-template-id", PropsValues.NESTED_PORTLETS_LAYOUT_TEMPLATE_DEFAULT);
        String content = StringPool.BLANK;
        if (Validator.isNotNull(layoutTemplateId)) {
            Theme theme = themeDisplay.getTheme();
            LayoutTemplate layoutTemplate = LayoutTemplateLocalServiceUtil.getLayoutTemplate(layoutTemplateId, false, theme.getThemeId());
            content = renameTemplateColumnsAndIds(layoutTemplate.getContent(), portlet);
        }
        renderRequest.setAttribute(WebKeys.LAYOUT_TEMPLATE_CONTENT, content);
        return mapping.findForward("portlet.nested_portlets.view");
    }

    protected String renameTemplateColumnsAndIds(String content, Portlet portlet) {
        Matcher matcher = _pattern.matcher(content);
        Set<String> columnIds = new HashSet<String>();
        while (matcher.find()) {
            if (Validator.isNotNull(matcher.group(1))) {
                columnIds.add(matcher.group(1));
            }
            if (Validator.isNotNull(matcher.group(2))) {
                columnIds.add(matcher.group(2));
            }
        }
        for (String columnId : columnIds) {
            if (columnId.indexOf(portlet.getPortletId()) == -1) {
                content = content.replaceAll(columnId, portlet.getPortletId() + "_" + columnId);
            }
        }
        return content;
    }

    private static Pattern _pattern = Pattern.compile("processColumn[(]\"(.*?)\"[)]|[<].*?id=[\"']([^ ]*?)[\"'].*?[>]", Pattern.DOTALL);
}
