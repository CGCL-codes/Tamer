package org.light.portal.controller;

import static org.light.portal.util.Constants._PAGE_ERROR_NOTFOUND;
import static org.light.portal.util.Constants._PAGE_ERROR_PERMISSION;
import static org.light.portal.util.Constants._PORTAL_MOBILE_BROWSER_VERSION;
import static org.light.portal.util.Constants._PORTLET_INDEX;
import static org.light.portal.util.Constants._PRIVACY_CONNECTION;
import static org.light.portal.util.Constants._PRIVACY_MEMBER;
import static org.light.portal.util.Constants._PRIVACY_ONLYME;
import static org.light.portal.util.Constants._PRIVACY_PROFILE;
import static org.light.portal.util.Constants._PRIVACY_PUBLIC;
import static org.light.portal.util.Constants._PORTLET_URL_PREFIX;
import static org.light.portal.util.Constants._PORTLET_MODE_HEADER;
import static org.light.portal.util.Constants._PORTLET_RENDER_ID_PREFIX;
import static org.light.portal.util.Constants._PORTLET_TITLE_ID_PREFIX;
import static org.light.portal.util.Constants._DISABLE_PAGE_REFRESH;
import static org.light.portal.util.Constants._REQUEST_SUFFIX;
import static org.light.portal.util.Constants._PORTLET_MOBILE_INDEX;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import javax.portlet.Portlet;
import javax.portlet.PortletMode;
import javax.portlet.WindowState;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.light.portal.core.model.Portal;
import org.light.portal.core.model.PortalTab;
import org.light.portal.core.model.PortletObject;
import org.light.portal.portlet.core.impl.PortletEnvironment;
import org.light.portal.portlet.core.impl.PortletWindow;
import org.light.portal.portlet.factory.PortletContainerFactory;
import org.light.portal.user.model.User;
import org.light.portal.util.OrganizationThreadLocal;
import org.light.portal.util.PropUtil;
import org.light.portal.util.ValidationUtil;
import org.light.portlets.connection.model.Connection;

/**
 * 
 * @author Jianmin Liu
 **/
public class PortletPageController extends GenericController {

    public void execute(HttpServletRequest request, HttpServletResponse response, ControllerChain chain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (uri.indexOf(_PORTLET_URL_PREFIX) >= 0 && (uri.indexOf(_REQUEST_SUFFIX) < 0)) {
            String path = uri.substring(uri.indexOf(_PORTLET_URL_PREFIX) + _PORTLET_URL_PREFIX.length());
            if (path.indexOf("/") < 0) doWidget(request, response, path);
        }
        chain.execute(request, response);
    }

    private void doWidget(HttpServletRequest request, HttpServletResponse response, String uri) throws ServletException, IOException {
        long orgId = OrganizationThreadLocal.getOrganizationId();
        if (request.getParameter("orgId") != null) {
            try {
                orgId = Long.parseLong(request.getParameter("orgId"));
                request.getSession().setAttribute("orgId", orgId);
            } catch (Exception e) {
            }
        }
        PortletObject po = null;
        PortalTab tab = null;
        long id = 0;
        try {
            id = Long.parseLong(uri);
        } catch (Exception e) {
        }
        if (id > 0) {
            po = this.getPortalService().getPortletById(id);
        }
        if (po != null) {
            registerParameter(request);
            tab = this.getPortalService().getPortalTabById(po.getTabId());
            Portal portal = this.getPortalService().getPortalById(tab.getPortalId());
            this.setVisitedPortal(request, portal);
            this.setVisitedPage(request, tab);
            String locale = Locale.ENGLISH.toString();
            if (this.getUser(request) != null) locale = this.getUser(request).getLanguage();
            this.setLocale(request, locale);
            request.setAttribute("portletObject", po);
            request.setAttribute("tab", tab);
            String browserInfo = request.getHeader("User-Agent");
            if (PropUtil.getBoolean(_PORTAL_MOBILE_BROWSER_VERSION) && ValidationUtil.isSmallMobile(browserInfo)) request.getSession().getServletContext().getRequestDispatcher(_PORTLET_MOBILE_INDEX).forward(request, response); else request.getSession().getServletContext().getRequestDispatcher(_PORTLET_INDEX).forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + _PAGE_ERROR_NOTFOUND);
        }
    }
}
