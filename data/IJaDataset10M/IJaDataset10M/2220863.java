package tests.security.cert;

import dalvik.annotation.TestTargets;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargetClass;
import junit.framework.TestCase;
import java.security.cert.CertStoreParameters;
import java.security.cert.LDAPCertStoreParameters;

/**
 * Tests for <code>java.security.cert.LDAPCertStoreParameters</code>
 * fields and methods
 * 
 */
@TestTargetClass(LDAPCertStoreParameters.class)
public class LDAPCertStoreParametersTest extends TestCase {

    /**
     * Test #1 for <code>LDAPCertStoreParameters()</code> constructor<br>
     * Assertion: Creates an instance of <code>LDAPCertStoreParameters</code>
     * with the default parameter values (server name "localhost", port 389)
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "LDAPCertStoreParameters", args = {  })
    public final void testLDAPCertStoreParameters01() {
        CertStoreParameters cp = new LDAPCertStoreParameters();
        assertTrue("isLDAPCertStoreParameters", cp instanceof LDAPCertStoreParameters);
    }

    /**
     * Test #2 for <code>LDAPCertStoreParameters()</code> constructor<br>
     * Assertion: Creates an instance of <code>LDAPCertStoreParameters</code>
     * with the default parameter values (server name "localhost", port 389)
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "LDAPCertStoreParameters", args = {  })
    public final void testLDAPCertStoreParameters02() {
        LDAPCertStoreParameters cp = new LDAPCertStoreParameters();
        assertEquals("host", "localhost", cp.getServerName());
        assertEquals("port", 389, cp.getPort());
    }

    /**
     * Test #1 for <code>LDAPCertStoreParameters(String)</code> constructor<br>
     * Assertion: Creates an instance of <code>LDAPCertStoreParameters</code>
     * with the specified server name and a default port of 389
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "LDAPCertStoreParameters", args = { java.lang.String.class })
    public final void testLDAPCertStoreParametersString01() {
        CertStoreParameters cp = new LDAPCertStoreParameters("myhost");
        assertTrue("isLDAPCertStoreParameters", cp instanceof LDAPCertStoreParameters);
    }

    /**
     * Test #2 for <code>LDAPCertStoreParameters(String)</code> constructor<br>
     * Assertion: Creates an instance of <code>LDAPCertStoreParameters</code>
     * with the specified server name and a default port of 389
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "LDAPCertStoreParameters", args = { java.lang.String.class })
    public final void testLDAPCertStoreParametersString02() {
        String serverName = "myhost";
        LDAPCertStoreParameters cp = new LDAPCertStoreParameters(serverName);
        assertTrue("host", serverName.equals(cp.getServerName()));
        assertEquals("port", 389, cp.getPort());
    }

    /**
     * Test #3 for <code>LDAPCertStoreParameters(String)</code> constructor<br>
     * Assertion: throws <code>NullPointerException</code> -
     * if <code>serverName</code> is <code>null</code>
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Verifies NullPointerException.", method = "LDAPCertStoreParameters", args = { java.lang.String.class })
    public final void testLDAPCertStoreParametersString03() {
        try {
            new LDAPCertStoreParameters(null);
            fail("NPE expected");
        } catch (NullPointerException e) {
        }
    }

    /**
     * Test #1 for <code>LDAPCertStoreParameters(String, int)</code> constructor<br>
     * Assertion: Creates an instance of <code>LDAPCertStoreParameters</code>
     * with the specified parameter values
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "LDAPCertStoreParameters", args = { java.lang.String.class, int.class })
    public final void testLDAPCertStoreParametersStringint01() {
        CertStoreParameters cp = new LDAPCertStoreParameters("myhost", 1098);
        assertTrue("isLDAPCertStoreParameters", cp instanceof LDAPCertStoreParameters);
    }

    /**
     * Test #2 for <code>LDAPCertStoreParameters(String, int)</code> constructor<br>
     * Assertion: Creates an instance of <code>LDAPCertStoreParameters</code>
     * with the specified parameter values
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "", method = "LDAPCertStoreParameters", args = { java.lang.String.class, int.class })
    public final void testLDAPCertStoreParametersStringint02() {
        String serverName = "myhost";
        int portNumber = 1099;
        LDAPCertStoreParameters cp = new LDAPCertStoreParameters(serverName, portNumber);
        assertTrue("host", serverName.equals(cp.getServerName()));
        assertTrue("port", cp.getPort() == portNumber);
    }

    /**
     * Test #3 for <code>LDAPCertStoreParameters(String, int)</code> constructor<br>
     * Assertion: throws <code>NullPointerException</code> -
     * if <code>serverName</code> is <code>null</code>
     */
    @TestTargetNew(level = TestLevel.PARTIAL_COMPLETE, notes = "Verifies NullPointerException.", method = "LDAPCertStoreParameters", args = { java.lang.String.class, int.class })
    public final void testLDAPCertStoreParametersStringint03() {
        try {
            new LDAPCertStoreParameters(null, 0);
            fail("NPE expected");
        } catch (NullPointerException e) {
        }
        String serverName = "myhost";
        int[] portNumber = { -1, -100, Integer.MIN_VALUE, Integer.MAX_VALUE };
        for (int i = 0; i < portNumber.length; i++) {
            try {
                new LDAPCertStoreParameters(serverName, portNumber[i]);
            } catch (Exception e) {
                fail("Unexpected exception for incorrect integer parametr");
            }
        }
    }

    /**
     * Test for <code>clone()</code> method<br>
     * Assertion: Returns a copy of this object
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "clone", args = {  })
    public final void testClone() {
        LDAPCertStoreParameters cp1 = new LDAPCertStoreParameters("myhost", 1100);
        LDAPCertStoreParameters cp2 = (LDAPCertStoreParameters) cp1.clone();
        assertTrue("newObject", cp1 != cp2);
        assertTrue("hostsTheSame", cp1.getServerName().equals(cp2.getServerName()));
        assertTrue("portsTheSame", cp1.getPort() == cp2.getPort());
    }

    /**
     * Test for <code>toString()</code> method<br>
     * Assertion: returns the formatted string describing parameters
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "toString", args = {  })
    public final void testToString() {
        LDAPCertStoreParameters cp1 = new LDAPCertStoreParameters("myhost", 1101);
        assertNotNull(cp1.toString());
    }

    /**
     * Test for <code>toString()</code> method<br>
     * Assertion: returns the port number
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getPort", args = {  })
    public final void testGetPort() {
        int portNumber = -1099;
        LDAPCertStoreParameters cp = new LDAPCertStoreParameters("serverName", portNumber);
        assertTrue(cp.getPort() == portNumber);
    }

    /**
     * Test for <code>toString()</code> method<br>
     * Assertion: returns the server name (never <code>null</code>)
     */
    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getServerName", args = {  })
    public final void testGetServerName() {
        LDAPCertStoreParameters cp = new LDAPCertStoreParameters("serverName");
        assertNotNull(cp.getServerName());
    }
}
