package com.classactionpl.jaas.ldap;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import org.apache.directory.server.unit.AbstractServerTest;
import com.classactionpl.jaas.UserGroup;
import com.classactionpl.jaas.UserPrincipal;

public class BindingLoginModuleTest extends AbstractServerTest {

    protected void setUp() throws Exception {
        configuration.setAccessControlEnabled(true);
        configuration.setAllowAnonymousAccess(false);
        super.setUp();
        importLdif(this.getClass().getResourceAsStream("test.ldif"));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAuthenticate() throws LoginException, UnsupportedEncodingException {
        Subject subject = new Subject();
        {
            Principal principal = new UserPrincipal("fred2@yourcompany");
            Group group = new UserGroup("freds2");
            group.addMember(principal);
            subject.getPrincipals().add(principal);
            subject.getPrincipals().add(group);
        }
        BindingLoginModule loginModule = new BindingLoginModule();
        Map<String, String> options = new HashMap<String, String>(2);
        options.put("initialContextFactory", "org.apache.directory.server.jndi.ServerContextFactory");
        options.put("providerURL", "");
        options.put("securityAuthentication", "simple");
        options.put("userBase", "ou=users,ou=system");
        options.put("userRDN", "uid");
        options.put("userReturnAttributeAsUserName", "uid");
        options.put("groupSearchBase", "ou=groups,ou=system");
        options.put("groupSearchFilter", "(member={0})");
        options.put("groupSearchReturnAttributeAsGroupName", "cn");
        loginModule.initialize(subject, new CallbackHandler() {

            public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
                NameCallback nameCallback = (NameCallback) callbacks[0];
                nameCallback.setName("fred@yourcompany");
                PasswordCallback passwordCallback = (PasswordCallback) callbacks[1];
                passwordCallback.setPassword("secret".toCharArray());
            }
        }, null, options);
        assertTrue(loginModule.login());
        assertTrue(loginModule.commit());
        {
            Set<Principal> principals = subject.getPrincipals();
            assertEquals(4, principals.size());
            for (Principal principal : principals) {
                if (principal.getName().equals("fred@yourcompany")) {
                } else if (principal.getName().equals("freds")) {
                    Group group = (Group) principal;
                    Enumeration<? extends Principal> enumeration = group.members();
                    assertTrue(enumeration.hasMoreElements());
                    Principal groupPrincipal = enumeration.nextElement();
                    assertEquals("fred@yourcompany", groupPrincipal.getName());
                    assertFalse(enumeration.hasMoreElements());
                } else if (principal.getName().equals("fred2@yourcompany")) {
                } else if (principal.getName().equals("freds2")) {
                } else {
                    fail("Unknown principal: " + principal.getName());
                }
            }
        }
        assertTrue(loginModule.logout());
        {
            Set<Principal> principals = subject.getPrincipals();
            assertEquals(2, principals.size());
            for (Principal principal : principals) {
                if (principal.getName().equals("fred2@yourcompany")) {
                } else if (principal.getName().equals("freds2")) {
                } else {
                    fail("Unknown principal: " + principal.getName());
                }
            }
        }
    }
}
