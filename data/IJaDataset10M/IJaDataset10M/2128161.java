package com.market.b2c.suport.validate.impl;

import javax.servlet.http.Cookie;
import com.market.b2c.suport.util.web.CookieUtils;
import com.market.b2c.suport.util.web.WebContext;
import com.market.b2c.suport.validate.IdentityValidator;
import com.market.b2c.suport.validate.PasswordVerifier;
import com.market.b2c.suport.validate.Principal;

/**
 * description author: zhangde date: Aug 27, 2009
 */
public abstract class CookieIdentityValidator implements IdentityValidator {

    /**
	 * ����һ��ֻ���û���ĳ־�cookie
	 * 
	 * @param userName
	 *            �û���
	 */
    public abstract Cookie createVisitorCookie(String userName);

    /**
	 * ����һ���û�����ĻỰcookie
	 * 
	 * @param user
	 *            �û�
	 * @return
	 */
    public abstract Cookie createPrincipalCookie(Principal principal);

    public boolean login(PasswordVerifier verifier) {
        Principal principal = getAuthenticationProvider().authenticate(verifier);
        if (principal != null) {
            CookieUtils.writeCookie(createVisitorCookie(principal.getName()));
            CookieUtils.writeCookie(createPrincipalCookie(principal));
            WebContext.currentRequest().setAttribute(VISITOR, principal.getName());
            WebContext.currentRequest().setAttribute(PRINCIPAL, principal);
            return true;
        } else {
            return false;
        }
    }
}
