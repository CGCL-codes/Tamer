package com.liferay.portal.service.impl;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutReference;
import com.liferay.portal.model.impl.ThemeImpl;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.PluginSettingLocalServiceUtil;
import com.liferay.portal.service.base.LayoutServiceBaseImpl;
import com.liferay.portal.service.permission.GroupPermissionUtil;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import java.io.File;
import java.util.Map;

/**
 * <a href="LayoutServiceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class LayoutServiceImpl extends LayoutServiceBaseImpl {

    public Layout addLayout(long groupId, boolean privateLayout, long parentLayoutId, String name, String title, String description, String type, boolean hidden, String friendlyURL) throws PortalException, SystemException {
        GroupPermissionUtil.check(getPermissionChecker(), groupId, ActionKeys.MANAGE_LAYOUTS);
        return LayoutLocalServiceUtil.addLayout(getUserId(), groupId, privateLayout, parentLayoutId, name, title, description, type, hidden, friendlyURL);
    }

    public void deleteLayout(long plid) throws PortalException, SystemException {
        LayoutPermissionUtil.check(getPermissionChecker(), plid, ActionKeys.DELETE);
        LayoutLocalServiceUtil.deleteLayout(plid);
    }

    public void deleteLayout(long groupId, boolean privateLayout, long layoutId) throws PortalException, SystemException {
        LayoutPermissionUtil.check(getPermissionChecker(), groupId, privateLayout, layoutId, ActionKeys.DELETE);
        LayoutLocalServiceUtil.deleteLayout(groupId, privateLayout, layoutId);
    }

    public String getLayoutName(long groupId, boolean privateLayout, long layoutId, String languageId) throws PortalException, SystemException {
        Layout layout = LayoutLocalServiceUtil.getLayout(groupId, privateLayout, layoutId);
        return layout.getName(languageId);
    }

    public LayoutReference[] getLayoutReferences(long companyId, String portletId, String prefsKey, String prefsValue) throws SystemException {
        return LayoutLocalServiceUtil.getLayouts(companyId, portletId, prefsKey, prefsValue);
    }

    public byte[] exportLayouts(long groupId, boolean privateLayout, Map parameterMap) throws PortalException, SystemException {
        GroupPermissionUtil.check(getPermissionChecker(), groupId, ActionKeys.MANAGE_LAYOUTS);
        return LayoutLocalServiceUtil.exportLayouts(groupId, privateLayout, parameterMap);
    }

    public void importLayouts(long groupId, boolean privateLayout, Map parameterMap, File file) throws PortalException, SystemException {
        GroupPermissionUtil.check(getPermissionChecker(), groupId, ActionKeys.MANAGE_LAYOUTS);
        LayoutLocalServiceUtil.importLayouts(getUserId(), groupId, privateLayout, parameterMap, file);
    }

    public void setLayouts(long groupId, boolean privateLayout, long parentLayoutId, long[] layoutIds) throws PortalException, SystemException {
        GroupPermissionUtil.check(getPermissionChecker(), groupId, ActionKeys.MANAGE_LAYOUTS);
        LayoutLocalServiceUtil.setLayouts(groupId, privateLayout, parentLayoutId, layoutIds);
    }

    public Layout updateLayout(long groupId, boolean privateLayout, long layoutId, long parentLayoutId, String name, String title, String languageId, String description, String type, boolean hidden, String friendlyURL) throws PortalException, SystemException {
        LayoutPermissionUtil.check(getPermissionChecker(), groupId, privateLayout, layoutId, ActionKeys.UPDATE);
        return LayoutLocalServiceUtil.updateLayout(groupId, privateLayout, layoutId, parentLayoutId, name, title, languageId, description, type, hidden, friendlyURL);
    }

    public Layout updateLayout(long groupId, boolean privateLayout, long layoutId, long parentLayoutId, String name, String title, String languageId, String description, String type, boolean hidden, String friendlyURL, Boolean iconImage, byte[] iconBytes) throws PortalException, SystemException {
        LayoutPermissionUtil.check(getPermissionChecker(), groupId, privateLayout, layoutId, ActionKeys.UPDATE);
        return LayoutLocalServiceUtil.updateLayout(groupId, privateLayout, layoutId, parentLayoutId, name, title, languageId, description, type, hidden, friendlyURL, iconImage, iconBytes);
    }

    public Layout updateLayout(long groupId, boolean privateLayout, long layoutId, String typeSettings) throws PortalException, SystemException {
        LayoutPermissionUtil.check(getPermissionChecker(), groupId, privateLayout, layoutId, ActionKeys.UPDATE);
        return LayoutLocalServiceUtil.updateLayout(groupId, privateLayout, layoutId, typeSettings);
    }

    public Layout updateLookAndFeel(long groupId, boolean privateLayout, long layoutId, String themeId, String colorSchemeId, String css, boolean wapTheme) throws PortalException, SystemException {
        LayoutPermissionUtil.check(getPermissionChecker(), groupId, privateLayout, layoutId, ActionKeys.UPDATE);
        PluginSettingLocalServiceUtil.checkPermission(getUserId(), themeId, ThemeImpl.PLUGIN_TYPE);
        return LayoutLocalServiceUtil.updateLookAndFeel(groupId, privateLayout, layoutId, themeId, colorSchemeId, css, wapTheme);
    }

    public Layout updateName(long plid, String name, String languageId) throws PortalException, SystemException {
        LayoutPermissionUtil.check(getPermissionChecker(), plid, ActionKeys.UPDATE);
        return LayoutLocalServiceUtil.updateName(plid, name, languageId);
    }

    public Layout updateName(long groupId, boolean privateLayout, long layoutId, String name, String languageId) throws PortalException, SystemException {
        LayoutPermissionUtil.check(getPermissionChecker(), groupId, privateLayout, layoutId, ActionKeys.UPDATE);
        return LayoutLocalServiceUtil.updateName(groupId, privateLayout, layoutId, name, languageId);
    }

    public Layout updateParentLayoutId(long plid, long parentPlid) throws PortalException, SystemException {
        LayoutPermissionUtil.check(getPermissionChecker(), plid, ActionKeys.UPDATE);
        return LayoutLocalServiceUtil.updateParentLayoutId(plid, parentPlid);
    }

    public Layout updateParentLayoutId(long groupId, boolean privateLayout, long layoutId, long parentLayoutId) throws PortalException, SystemException {
        LayoutPermissionUtil.check(getPermissionChecker(), groupId, privateLayout, layoutId, ActionKeys.UPDATE);
        return LayoutLocalServiceUtil.updateParentLayoutId(groupId, privateLayout, layoutId, parentLayoutId);
    }

    public Layout updatePriority(long plid, int priority) throws PortalException, SystemException {
        LayoutPermissionUtil.check(getPermissionChecker(), plid, ActionKeys.UPDATE);
        return LayoutLocalServiceUtil.updatePriority(plid, priority);
    }

    public Layout updatePriority(long groupId, boolean privateLayout, long layoutId, int priority) throws PortalException, SystemException {
        LayoutPermissionUtil.check(getPermissionChecker(), groupId, privateLayout, layoutId, ActionKeys.UPDATE);
        return LayoutLocalServiceUtil.updatePriority(groupId, privateLayout, layoutId, priority);
    }
}
