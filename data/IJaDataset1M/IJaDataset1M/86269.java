package com.liferay.portal.action;

import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.InstancePool;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringMaker;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutTypePortlet;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.model.impl.PortletImpl;
import com.liferay.portal.model.impl.ResourceImpl;
import com.liferay.portal.service.LayoutServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.service.ResourceLocalServiceUtil;
import com.liferay.portal.service.permission.LayoutPermissionUtil;
import com.liferay.portal.service.permission.PortletPermissionUtil;
import com.liferay.portal.servlet.NamespaceServletRequest;
import com.liferay.portal.struts.ActionConstants;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import com.liferay.util.servlet.DynamicServletRequest;
import javax.portlet.PortletPreferences;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * <a href="UpdateLayoutAction.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class UpdateLayoutAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest req, HttpServletResponse res) throws Exception {
        ThemeDisplay themeDisplay = (ThemeDisplay) req.getAttribute(WebKeys.THEME_DISPLAY);
        long userId = themeDisplay.getUserId();
        Layout layout = themeDisplay.getLayout();
        LayoutTypePortlet layoutTypePortlet = themeDisplay.getLayoutTypePortlet();
        PermissionChecker permissionChecker = themeDisplay.getPermissionChecker();
        String cmd = ParamUtil.getString(req, Constants.CMD);
        String portletId = ParamUtil.getString(req, "p_p_id");
        boolean updateLayout = true;
        boolean deletePortlet = false;
        if (cmd.equals(Constants.ADD)) {
            portletId = layoutTypePortlet.addPortletId(userId, portletId);
        } else if (cmd.equals(Constants.DELETE)) {
            if (layoutTypePortlet.hasPortletId(portletId)) {
                deletePortlet = true;
                layoutTypePortlet.removePortletId(portletId);
            }
        } else if (cmd.equals("drag")) {
            if (LayoutPermissionUtil.contains(permissionChecker, layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(), ActionKeys.UPDATE)) {
                String height = ParamUtil.getString(req, "height");
                String width = ParamUtil.getString(req, "width");
                String top = ParamUtil.getString(req, "top");
                String left = ParamUtil.getString(req, "left");
                PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(req, portletId, true, true);
                StringMaker sm = new StringMaker();
                sm.append("height=" + height + "\n");
                sm.append("width=" + width + "\n");
                sm.append("top=" + top + "\n");
                sm.append("left=" + left + "\n");
                prefs.setValue("portlet-freeform-styles", sm.toString());
                prefs.store();
            }
        } else if (cmd.equals("minimize")) {
            boolean restore = ParamUtil.getBoolean(req, "p_p_restore");
            if (restore) {
                layoutTypePortlet.removeStateMinPortletId(portletId);
            } else {
                layoutTypePortlet.addStateMinPortletId(portletId);
                if (layout.isShared()) {
                    updateLayout = false;
                }
            }
        } else if (cmd.equals("move")) {
            String columnId = ParamUtil.getString(req, "p_p_col_id");
            int columnPos = ParamUtil.getInteger(req, "p_p_col_pos");
            layoutTypePortlet.movePortletId(userId, portletId, columnId, columnPos);
        } else if (cmd.equals("template")) {
            String layoutTemplateId = ParamUtil.getString(req, "layoutTemplateId");
            layoutTypePortlet.setLayoutTemplateId(userId, layoutTemplateId);
        }
        if (updateLayout) {
            if (layout.isShared()) {
                layoutTypePortlet.resetStates();
            }
            LayoutServiceUtil.updateLayout(layout.getGroupId(), layout.isPrivateLayout(), layout.getLayoutId(), layout.getTypeSettings());
            if (deletePortlet) {
                String rootPortletId = PortletImpl.getRootPortletId(portletId);
                ResourceLocalServiceUtil.deleteResource(layout.getCompanyId(), rootPortletId, ResourceImpl.SCOPE_INDIVIDUAL, PortletPermissionUtil.getPrimaryKey(layout.getPlid(), portletId));
            }
        }
        if (ParamUtil.getBoolean(req, "refresh")) {
            return mapping.findForward(ActionConstants.COMMON_REFERER);
        } else {
            if (cmd.equals(Constants.ADD) && (portletId != null)) {
                Action renderPortletAction = (Action) InstancePool.get(RenderPortletAction.class.getName());
                long companyId = PortalUtil.getCompanyId(req);
                Portlet portlet = PortletLocalServiceUtil.getPortletById(companyId, portletId);
                DynamicServletRequest dynamicReq = null;
                if (portlet.isPrivateRequestAttributes()) {
                    String portletNamespace = PortalUtil.getPortletNamespace(portlet.getPortletId());
                    dynamicReq = new NamespaceServletRequest(req, portletNamespace, portletNamespace);
                } else {
                    dynamicReq = new DynamicServletRequest(req);
                }
                dynamicReq.setParameter("p_p_id", portletId);
                renderPortletAction.execute(mapping, form, dynamicReq, res);
            }
            return null;
        }
    }
}
