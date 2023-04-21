package org.clin4j.framework.security.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.clin4j.framework.ioc.ServiceProvider;
import org.clin4j.framework.security.shiro.realm.JpaRealm;
import org.clin4j.framework.util.GlobalKeys;
import org.clin4j.framework.domain.model.Group.GroupName;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

public class JpaRealmTest {

    private DefaultSecurityManager securityManager;

    private JpaRealm jparealm;

    @Before
    public void setup() {
        ThreadContext.clear();
        ServiceProvider service = ServiceProvider.instantiate();
        jparealm = (JpaRealm) service.provide(GlobalKeys.JPA_REALM);
        securityManager = (DefaultSecurityManager) service.provide(GlobalKeys.DEFAULT_SECURITY_MANAGER);
        securityManager.setRealm(jparealm);
        SecurityUtils.setSecurityManager(securityManager);
    }

    @After
    public void tearDown() {
        ThreadContext.clear();
    }

    @Test
    public void testAuthentication() {
        Subject currentUser = SecurityUtils.getSubject();
        boolean isauth = currentUser.isAuthenticated();
        assertEquals(isauth, false);
        if (!isauth) {
            UsernamePasswordToken token = new UsernamePasswordToken("u1@hotmail.com", "u1");
            token.setRememberMe(true);
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                uae.printStackTrace();
            } catch (IncorrectCredentialsException ice) {
                ice.printStackTrace();
            } catch (LockedAccountException lae) {
                lae.printStackTrace();
            } catch (AuthenticationException ae) {
                ae.printStackTrace();
            }
            String account = (String) currentUser.getPrincipal();
            assertEquals(account, "u1@hotmail.com");
            boolean hasrole = currentUser.hasRole(GroupName.Root.toString());
            assertEquals(hasrole, true);
            boolean isPermitted = currentUser.isPermitted("/news:read:*");
            assertEquals(isPermitted, true);
        }
        currentUser.logout();
    }
}
