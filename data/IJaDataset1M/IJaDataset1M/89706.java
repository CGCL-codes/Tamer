package net.sf.jguard.jee.authorization;

import groovy.lang.GroovyShell;
import groovy.security.GroovyCodeSourcePermission;
import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.jguard.core.authorization.policy.AccessControlContextUtils;
import net.sf.jguard.core.principals.RolePrincipal;
import org.codehaus.groovy.control.CompilationFailedException;
import java.security.*;
import java.util.HashSet;

public class AccessControlContextTest extends TestCase {

    public void getRestrictedAccessControlContext() {
        final String scriptText = "System.exit(0);";
        final GroovyShell gs = new GroovyShell();
        AccessControlContext acc;
        RolePrincipal principal = new RolePrincipal("toto", "sdfsdf");
        principal.setPermissions(new HashSet());
        principal.addPermission(new GroovyCodeSourcePermission("totos"));
        principal.addPermission(new SecurityPermission("createAccessControlContext"));
        acc = AccessControlContextUtils.getRestrictedAccessControlContext(principal);
        try {
            AccessController.doPrivileged(new PrivilegedAction() {

                public Object run() {
                    Object scriptResult = null;
                    try {
                        scriptResult = gs.evaluate(scriptText);
                    } catch (CompilationFailedException e) {
                        TestCase.fail(e.getMessage());
                    }
                    return scriptResult;
                }
            }, acc);
        } catch (AccessControlException ace) {
            System.out.println(" restricted area! OK");
            return;
        }
        Assert.fail(" an accessControlException should be thrown to prevent security operations done by scripting languages ");
    }

    public void testDummy() {
    }
}
