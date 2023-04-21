package org.clin4j.framework.security.shiro;

import java.util.List;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.DefaultWebSecurityManager;
import org.clin4j.framework.ioc.Configurator;
import org.clin4j.framework.security.IAuthentication;
import org.clin4j.framework.security.IAuthorization;
import org.clin4j.framework.security.shiro.realm.JpaRealm;

public class SecurityService implements IAuthorization {

    private Subject currentUser;

    private DefaultWebSecurityManager securityManager;

    public SecurityService(DefaultWebSecurityManager securityManager) {
        this.securityManager = securityManager;
        if (null == this.securityManager) throw new NullPointerException("No SecurityManager configured!");
        SecurityUtils.setSecurityManager(this.securityManager);
        currentUser = SecurityUtils.getSubject();
    }

    public Boolean login(String id, String password) {
        UsernamePasswordToken token = new UsernamePasswordToken(id, password);
        token.setRememberMe(true);
        boolean successful = true;
        Throwable e = null;
        try {
            currentUser.login(token);
        } catch (AuthenticationException ae) {
            e = ae;
            successful = false;
        }
        if (!successful) throw new SecurityException(" Fail to login!", e);
        return successful;
    }

    public Boolean logout() {
        currentUser.logout();
        return true;
    }

    public List<String> getGroups() {
        return null;
    }

    public Boolean isPermitted(String uri) {
        return true;
    }
}
