package org.osidx.junit;

import junit.framework.TestCase;
import java.util.Properties;
import java.util.Vector;
import org.osid.*;
import org.osid.shared.*;
import org.osid.authentication.*;

/**
 *  <p>
 *  AuthenticationTest is a JUnit test for the Authentication OSID. The OSID 
 *  implementation loaded is specified through using the TestCase constructor. 
 *  </p><p>
 *  CVS $Id: AuthenticationTest.java,v 1.1 2005/08/25 15:18:56 tom Exp $
 *  </p>
 *
 *  @author  Tom Coppeto
 *  @version $OSID: 2.0$ $Revision: 1.1 $
 */
public class AuthenticationTest extends TestCase {

    private String osid = "org.osid.authentication.AuthenticationManager";

    private AuthenticationManager mgr;

    protected void setUp() {
        try {
            log("loading AuthentcationManager(" + getName() + ")");
            mgr = (AuthenticationManager) OsidLoader.getManager(osid, getName(), new OsidContext(), new Properties());
            assertNotNull(mgr);
        } catch (OsidException oe) {
            oe.printStackTrace();
            fail("OsidException" + oe.getMessage());
            return;
        }
    }

    protected void tearDown() {
    }

    public void testAuthenticationManager_getAuthenticationTypes() {
        log("testing AuthenticationManager.getAuthenticationTypes()");
        try {
            TypeIterator types = mgr.getAuthenticationTypes();
            assertNotNull(types);
            assertTrue(types.hasNextType());
            log("\tTypes:");
            while (types.hasNextType()) {
                Type type = types.nextType();
                assertNotNull(type);
                log("\t" + formatType(type));
            }
        } catch (AuthenticationException ae) {
            ae.printStackTrace();
            fail("AuthenticationException" + ae.getMessage());
            return;
        } catch (SharedException se) {
            se.printStackTrace();
            fail("SharedException" + se.getMessage());
        }
    }

    public void testAuthenticationManager_authenticateUser() {
        log("testing AuthenticationManager.authenticateUser()");
        try {
            TypeIterator types = mgr.getAuthenticationTypes();
            assertNotNull(types);
            assertTrue(types.hasNextType());
            log("\tTypes:");
            while (types.hasNextType()) {
                Type type = types.nextType();
                assertNotNull(type);
                log("\t" + formatType(type));
                mgr.authenticateUser(type);
            }
        } catch (AuthenticationException ae) {
            ae.printStackTrace();
            fail("AuthenticationException" + ae.getMessage());
            return;
        } catch (SharedException se) {
            se.printStackTrace();
            fail("SharedException" + se.getMessage());
        }
    }

    public void testAuthenticationManager_isUserAuthenticated() {
        log("testing AuthenticationManager.isUserAuthenticated()");
        try {
            TypeIterator types = mgr.getAuthenticationTypes();
            assertNotNull(types);
            assertTrue(types.hasNextType());
            log("\tTypes:");
            while (types.hasNextType()) {
                Type type = types.nextType();
                assertNotNull(type);
                log("\t" + formatType(type));
                boolean b = mgr.isUserAuthenticated(type);
                log("\tauthenticated: " + b);
            }
        } catch (AuthenticationException ae) {
            ae.printStackTrace();
            fail("AuthenticationException" + ae.getMessage());
            return;
        } catch (SharedException se) {
            se.printStackTrace();
            fail("SharedException" + se.getMessage());
        }
    }

    public void testAuthenticationManager_getUserId() {
        log("testing AuthenticationManager.getUserId()");
        try {
            TypeIterator types = mgr.getAuthenticationTypes();
            assertNotNull(types);
            assertTrue(types.hasNextType());
            log("\tTypes:");
            while (types.hasNextType()) {
                Type type = types.nextType();
                assertNotNull(type);
                log("\t" + formatType(type));
                Id id = mgr.getUserId(type);
                assertNotNull(id);
            }
        } catch (AuthenticationException ae) {
            ae.printStackTrace();
            fail("AuthenticationException" + ae.getMessage());
            return;
        } catch (SharedException se) {
            se.printStackTrace();
            fail("SharedException" + se.getMessage());
        }
    }

    public void testAuthenticationManager_destroyAuthenticationForType() {
        log("testing AuthenticationManager.destroyAuthenticationForType()");
        try {
            TypeIterator types = mgr.getAuthenticationTypes();
            assertNotNull(types);
            assertTrue(types.hasNextType());
            log("\tTypes:");
            while (types.hasNextType()) {
                Type type = types.nextType();
                assertNotNull(type);
                log("\t" + formatType(type));
                mgr.destroyAuthenticationForType(type);
            }
        } catch (AuthenticationException ae) {
            ae.printStackTrace();
            fail("AuthenticationException" + ae.getMessage());
            return;
        } catch (SharedException se) {
            se.printStackTrace();
            fail("SharedException" + se.getMessage());
        }
    }

    private String formatType(Type type) {
        return (type.getDomain() + "/" + type.getKeyword() + "@" + type.getAuthority() + ": " + type.getDescription());
    }

    private void log(String msg) {
        System.out.println(msg);
    }
}
