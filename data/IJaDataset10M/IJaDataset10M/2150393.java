package com.liferay.portal.wsrp.producer.usermanager;

import com.liferay.portal.model.Group;
import com.liferay.portal.model.GroupConstants;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.model.Portlet;
import com.liferay.portal.portletcontainer.PortletWindowContextImpl;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.PortletLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalInstances;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.WebKeys;
import com.sun.portal.container.PortletWindowContext;
import com.sun.portal.wsrp.common.WSRPSpecKeys;
import com.sun.portal.wsrp.common.stubs.v2.UserContext;
import com.sun.portal.wsrp.producer.Producer;
import com.sun.portal.wsrp.producer.ProducerException;
import com.sun.portal.wsrp.producer.usermanager.UserManager;
import java.util.List;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * <a href="UserManagerImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Manish Gupta
 * @author Brian Wing Shun Chan
 *
 */
public class UserManagerImpl implements UserManager {

    public UserManagerImpl(Producer producer) {
    }

    public void createConsumerUserStore(String registrationHandle) {
    }

    public void createDefaultUserStore() {
    }

    public void deleteConsumerUserStore(String registrationHandle) {
    }

    public void deleteDefaultUserStore() {
    }

    public PortletWindowContext getPortletWindowContext(HttpServletRequest request, String registrationHandle, String userKey) throws ProducerException {
        try {
            request.setAttribute(_IS_WSRP_REQUEST, "true");
            String portletId = (String) request.getAttribute(_PORTAL_ID);
            Portlet portlet = PortletLocalServiceUtil.getPortletById(PortalInstances.getCompanyId(request), portletId);
            PortletWindowContext portletWindowContext = new PortletWindowContextImpl(request, portlet, PortletRequest.ACTION_PHASE);
            initializeLiferayRequest(request);
            return portletWindowContext;
        } catch (Exception e) {
            throw new ProducerException(e);
        }
    }

    public String getUserKey(UserContext userContext) {
        String userKey = null;
        if (userContext != null) {
            userKey = userContext.getUserContextKey();
            if ((userKey != null) && (userKey.equals(WSRPSpecKeys.WSRP_GUEST_KEY))) {
                userKey = null;
            }
        }
        return userKey;
    }

    public boolean supportsRoleCreation() {
        return false;
    }

    public boolean supportsUserCreation() {
        return true;
    }

    protected void initializeLiferayRequest(HttpServletRequest request) throws Exception {
        ThemeDisplay themeDisplay = new ThemeDisplay();
        themeDisplay.setCompany(PortalUtil.getCompany(request));
        themeDisplay.setUser(themeDisplay.getDefaultUser());
        Group guestGroup = GroupLocalServiceUtil.getGroup(PortalInstances.getCompanyId(request), GroupConstants.GUEST);
        List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(guestGroup.getGroupId(), false, LayoutConstants.DEFAULT_PARENT_LAYOUT_ID);
        Layout layout = null;
        if (layouts.size() > 0) {
            layout = layouts.get(0);
        }
        themeDisplay.setLayout(layout);
        request.setAttribute(WebKeys.THEME_DISPLAY, themeDisplay);
        request.setAttribute(WebKeys.LAYOUT, layout);
    }

    private static final String _IS_WSRP_REQUEST = "is.wsrp.request";

    private static final String _PORTAL_ID = "com.sun.portal.portlet.id";
}
