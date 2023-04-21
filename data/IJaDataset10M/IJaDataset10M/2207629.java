package org.light.portal.controller;

import static org.light.portal.util.Constants._PORTAL_INDEX;
import static org.light.portal.util.Constants._PORTAL_MOBILE_BROWSER_VERSION;
import static org.light.portal.util.Constants._REQUEST_SUFFIX;
import static org.light.portal.util.Constants._SPACE_INDEX;
import static org.light.portal.util.Constants._SPACE_MOBILE_INDEX;
import static org.light.portal.util.Constants._SPACE_URL_PREFIX;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.light.portal.core.model.Portal;
import org.light.portal.user.model.User;
import org.light.portal.user.model.UserProfile;
import org.light.portal.util.OrganizationThreadLocal;
import org.light.portal.util.PropUtil;
import org.light.portal.util.ValidationUtil;

/**
 * 
 * @author Jianmin Liu
 **/
public class SpaceController extends GenericController {

    public void execute(HttpServletRequest request, HttpServletResponse response, ControllerChain chain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        int index = uri.lastIndexOf("/");
        String path = uri.substring(index + 1);
        if (uri.indexOf(_SPACE_URL_PREFIX) >= 0 && (uri.indexOf(_REQUEST_SUFFIX) < 0)) {
            String sub = uri.substring(uri.indexOf(_SPACE_URL_PREFIX) + _SPACE_URL_PREFIX.length());
            if (sub.indexOf("/") < 0) doViewProfile(request, response, path);
        }
        chain.execute(request, response);
    }

    private void doViewProfile(HttpServletRequest request, HttpServletResponse response, String uri) throws ServletException, IOException {
        User user = this.getUserService().getUserByUri(uri, OrganizationThreadLocal.getOrganizationId());
        if (user != null) {
            registerParameter(request);
            this.setVisitedUser(request, user);
            UserProfile userProfile = this.getUserService().getUserProfileById(user.getId());
            if (userProfile != null) request.setAttribute("userProfile", userProfile);
            Portal portal = getPortalService().getPortalByUser(user.getUserId(), user.getOrgId());
            this.setVisitedPortal(request, portal);
            this.setLocale(request, user.getLanguage());
            String browserInfo = request.getHeader("User-Agent");
            if (PropUtil.getBoolean(_PORTAL_MOBILE_BROWSER_VERSION) && ValidationUtil.isSmallMobile(browserInfo)) {
                request.getSession().getServletContext().getRequestDispatcher(_SPACE_MOBILE_INDEX).forward(request, response);
            } else {
                request.getSession().getServletContext().getRequestDispatcher(_SPACE_INDEX).forward(request, response);
            }
        } else {
            response.sendRedirect(request.getContextPath() + _PORTAL_INDEX);
        }
    }
}
