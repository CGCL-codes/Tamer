package org.apache.harmony.security.tests.provider.cert;

import java.math.BigInteger;
import java.util.Date;
import javax.security.auth.x500.X500Principal;
import org.apache.harmony.security.provider.cert.X509CRLEntryImpl;
import org.apache.harmony.security.x509.Extension;
import org.apache.harmony.security.x509.Extensions;
import org.apache.harmony.security.x509.ReasonCode;
import org.apache.harmony.security.x509.TBSCertList;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * X509CRLEntryImplTest test
 */
public class X509CRLEntryImplTest extends TestCase {

    /**
     * getExtensionValue(String oid) method testing.
     */
    public void testGetExtensionValue() throws Exception {
        X500Principal issuer = new X500Principal("O=Certificate Issuer");
        BigInteger serialNumber = BigInteger.valueOf(555);
        Extensions crlEntryExtensions = new Extensions();
        crlEntryExtensions.addExtension(new Extension("2.5.29.21", Extension.NON_CRITICAL, new ReasonCode(ReasonCode.KEY_COMPROMISE)));
        X509CRLEntryImpl crlEntry = new X509CRLEntryImpl(new TBSCertList.RevokedCertificate(serialNumber, new Date(), crlEntryExtensions), issuer);
        assertNotNull(crlEntry.getExtensionValue("2.5.29.21"));
        assertNull("Null value should be returned in the case of " + "nonexisting extension", crlEntry.getExtensionValue("2.5.29.24"));
    }

    public static Test suite() {
        return new TestSuite(X509CRLEntryImplTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
