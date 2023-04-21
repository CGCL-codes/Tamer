package com.liferay.portal.servlet.filters.servletauthorizing;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.ProtectedServletRequest;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.CompanyThreadLocal;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactory;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.liferay.portal.util.PortalInstances;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.WebKeys;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.Globals;

/**
 * <a href="ServletAuthorizingFilter.java.html"><b><i>View Source</i></b></a>
 *
 * @author Raymond Augé
 *
 */
public class ServletAuthorizingFilter extends BasePortalFilter {

    protected void processFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpSession session = request.getSession();
        long companyId = PortalInstances.getCompanyId(request);
        request.setAttribute(WebKeys.COMPANY_ID, new Long(companyId));
        long userId = PortalUtil.getUserId(request);
        String remoteUser = request.getRemoteUser();
        if (!PropsValues.PORTAL_JAAS_ENABLE) {
            String jRemoteUser = (String) session.getAttribute("j_remoteuser");
            if (jRemoteUser != null) {
                remoteUser = jRemoteUser;
                session.removeAttribute("j_remoteuser");
            }
        }
        if ((userId > 0) && (remoteUser == null)) {
            remoteUser = String.valueOf(userId);
        }
        request = new ProtectedServletRequest(request, remoteUser);
        PermissionChecker permissionChecker = null;
        if ((userId > 0) || (remoteUser != null)) {
            String name = String.valueOf(userId);
            if (remoteUser != null) {
                name = remoteUser;
            }
            PrincipalThreadLocal.setName(name);
            userId = GetterUtil.getLong(name);
            try {
                User user = UserLocalServiceUtil.getUserById(userId);
                permissionChecker = PermissionCheckerFactory.create(user, true);
                PermissionThreadLocal.setPermissionChecker(permissionChecker);
                session.setAttribute(WebKeys.USER_ID, new Long(userId));
                session.setAttribute(Globals.LOCALE_KEY, user.getLocale());
            } catch (Exception e) {
                _log.error(e, e);
            }
        }
        try {
            processFilter(ServletAuthorizingFilter.class, request, response, filterChain);
        } finally {
            try {
                PermissionCheckerFactory.recycle(permissionChecker);
            } catch (Exception e) {
                _log.error(e, e);
            }
            CompanyThreadLocal.setCompanyId(0);
            PrincipalThreadLocal.setName(null);
        }
    }

    private static Log _log = LogFactoryUtil.getLog(ServletAuthorizingFilter.class);
}
