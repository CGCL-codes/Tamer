package com.liferay.portlet.nestedportlets.action;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTemplate;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Theme;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.LayoutTemplateLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.util.UniqueList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * <a href="ConfigurationActionImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Jorge Ferrer
 *
 */
public class ConfigurationActionImpl implements ConfigurationAction {

    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        String layoutTemplateId = ParamUtil.getString(actionRequest, "layoutTemplateId");
        String portletSetupShowBorders = ParamUtil.getString(actionRequest, "portletSetupShowBorders");
        String portletResource = ParamUtil.getString(actionRequest, "portletResource");
        PortletPreferences preferences = PortletPreferencesFactoryUtil.getPortletSetup(actionRequest, portletResource);
        String oldLayoutTemplateId = preferences.getValue("layout-template-id", PropsValues.NESTED_PORTLETS_LAYOUT_TEMPLATE_DEFAULT);
        if (!oldLayoutTemplateId.equals(layoutTemplateId)) {
            reorganizeNestedColumns(actionRequest, portletResource, layoutTemplateId, oldLayoutTemplateId);
        }
        preferences.setValue("layout-template-id", layoutTemplateId);
        preferences.setValue("portlet-setup-show-borders", portletSetupShowBorders);
        preferences.store();
        SessionMessages.add(actionRequest, portletConfig.getPortletName() + ".doConfigure");
    }

    public String render(PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {
        return "/html/portlet/nested_portlets/configuration.jsp";
    }

    protected List<String> getColumnNames(String content, String portletId) {
        Matcher matcher = _pattern.matcher(content);
        Set<String> columnIds = new HashSet<String>();
        while (matcher.find()) {
            if (Validator.isNotNull(matcher.group(1))) {
                columnIds.add(matcher.group(1));
            }
        }
        List<String> columnNames = new UniqueList<String>();
        for (String columnId : columnIds) {
            if (columnId.indexOf(portletId) == -1) {
                columnNames.add(portletId + StringPool.UNDERLINE + columnId);
            }
        }
        return columnNames;
    }

    protected void reorganizeNestedColumns(ActionRequest actionRequest, String portletResource, String newLayoutTemplateId, String oldLayoutTemplateId) throws PortalException, SystemException {
        ThemeDisplay themeDisplay = (ThemeDisplay) actionRequest.getAttribute(WebKeys.THEME_DISPLAY);
        Layout layout = themeDisplay.getLayout();
        LayoutTypePortlet layoutTypePortlet = themeDisplay.getLayoutTypePortlet();
        Theme theme = themeDisplay.getTheme();
        LayoutTemplate newLayoutTemplate = LayoutTemplateLocalServiceUtil.getLayoutTemplate(newLayoutTemplateId, false, theme.getThemeId());
        List<String> newColumns = getColumnNames(newLayoutTemplate.getContent(), portletResource);
        LayoutTemplate oldLayoutTemplate = LayoutTemplateLocalServiceUtil.getLayoutTemplate(oldLayoutTemplateId, false, theme.getThemeId());
        List<String> oldColumns = getColumnNames(oldLayoutTemplate.getContent(), portletResource);
        layoutTypePortlet.reorganizePortlets(newColumns, oldColumns);
        layoutTypePortlet.setStateMax(StringPool.BLANK);
        LayoutLocalServiceUtil.updateLayout(layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(), layout.getTypeSettings());
    }

    private static Pattern _pattern = Pattern.compile("processColumn[(]\"(.*?)\"[)]", Pattern.DOTALL);
}
