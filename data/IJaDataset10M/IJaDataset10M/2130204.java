package com.liferay.portal.servlet.filters.autologin;

import com.liferay.portal.NoSuchUserException;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InstancePool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.AutoLogin;
import com.liferay.portal.security.pwd.PwdEncryptor;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.WebKeys;
import com.liferay.util.servlet.ProtectedServletRequest;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="AutoLoginFilter.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class AutoLoginFilter implements Filter {

    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) req;
        HttpServletResponse httpRes = (HttpServletResponse) res;
        HttpSession ses = httpReq.getSession();
        String remoteUser = httpReq.getRemoteUser();
        String jUserName = (String) ses.getAttribute("j_username");
        if ((remoteUser == null) && (jUserName == null)) {
            String[] autoLogins = PropsUtil.getArray(PropsUtil.AUTO_LOGIN_HOOKS);
            for (int i = 0; i < autoLogins.length; i++) {
                AutoLogin autoLogin = (AutoLogin) InstancePool.get(autoLogins[i]);
                try {
                    String[] credentials = autoLogin.login(httpReq, httpRes);
                    String redirect = (String) req.getAttribute(AutoLogin.AUTO_LOGIN_REDIRECT);
                    if (redirect != null) {
                        httpRes.sendRedirect(redirect);
                        return;
                    }
                    String loginRemoteUser = getLoginRemoteUser(httpReq, httpRes, ses, credentials);
                    if (loginRemoteUser != null) {
                        req = new ProtectedServletRequest(httpReq, loginRemoteUser);
                        if (GetterUtil.getBoolean(PropsUtil.get(PropsUtil.PORTAL_JAAS_ENABLE))) {
                            return;
                        }
                    }
                } catch (Exception e) {
                    _log.warn(e, e);
                    _log.error(e.getMessage());
                }
            }
        }
        chain.doFilter(req, res);
    }

    public void destroy() {
    }

    protected String getLoginRemoteUser(HttpServletRequest req, HttpServletResponse res, HttpSession ses, String[] credentials) throws Exception {
        if ((credentials != null) && (credentials.length == 3)) {
            String jUsername = credentials[0];
            String jPassword = credentials[1];
            boolean encPwd = GetterUtil.getBoolean(credentials[2]);
            if (Validator.isNotNull(jUsername) && Validator.isNotNull(jPassword)) {
                try {
                    long userId = GetterUtil.getLong(jUsername);
                    if (userId > 0) {
                        User user = UserLocalServiceUtil.getUserById(userId);
                        if (user.isLockout()) {
                            return null;
                        }
                    } else {
                        return null;
                    }
                } catch (NoSuchUserException nsue) {
                    return null;
                }
                ses.setAttribute("j_username", jUsername);
                if (encPwd) {
                    ses.setAttribute("j_password", jPassword);
                } else {
                    ses.setAttribute("j_password", PwdEncryptor.encrypt(jPassword));
                    ses.setAttribute(WebKeys.USER_PASSWORD, jPassword);
                }
                if (GetterUtil.getBoolean(PropsUtil.get(PropsUtil.PORTAL_JAAS_ENABLE))) {
                    res.sendRedirect(PortalUtil.getPathMain() + "/portal/touch_protected");
                }
                return jUsername;
            }
        }
        return null;
    }

    private static Log _log = LogFactory.getLog(AutoLoginFilter.class);
}
