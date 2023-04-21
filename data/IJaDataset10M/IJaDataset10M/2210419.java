package com.liferay.portal.security.auth;

import com.liferay.portal.model.Company;
import com.liferay.portal.service.spring.UserLocalServiceUtil;
import com.liferay.portal.util.CookieKeys;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.CookieUtil;
import com.liferay.util.KeyValuePair;
import com.liferay.util.StringPool;
import com.liferay.util.Validator;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <a href="BasicAutoLogin.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 *
 */
public class BasicAutoLogin implements AutoLogin {

    public String[] login(HttpServletRequest req, HttpServletResponse res) throws AutoLoginException {
        try {
            String[] credentials = null;
            String autoUserId = CookieUtil.get(req.getCookies(), CookieKeys.ID);
            String autoPassword = CookieUtil.get(req.getCookies(), CookieKeys.PASSWORD);
            if (Validator.isNotNull(autoUserId) && Validator.isNotNull(autoPassword)) {
                Company company = PortalUtil.getCompany(req);
                KeyValuePair kvp = null;
                if (company.isAutoLogin() && !req.isSecure()) {
                    kvp = UserLocalServiceUtil.decryptUserId(company.getCompanyId(), autoUserId, autoPassword);
                    credentials = new String[3];
                    credentials[0] = kvp.getKey();
                    credentials[1] = kvp.getValue();
                    credentials[2] = Boolean.FALSE.toString();
                }
            }
            return credentials;
        } catch (Exception e) {
            Cookie cookie = new Cookie(CookieKeys.ID, StringPool.BLANK);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            CookieKeys.addCookie(res, cookie);
            cookie = new Cookie(CookieKeys.PASSWORD, StringPool.BLANK);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            CookieKeys.addCookie(res, cookie);
            throw new AutoLoginException(e);
        }
    }
}
