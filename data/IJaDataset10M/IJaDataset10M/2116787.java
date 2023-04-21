package org.apache.harmony.security.tests.x509;

import java.util.Date;
import junit.framework.TestCase;
import org.apache.harmony.security.asn1.ASN1Constants;
import org.apache.harmony.security.x509.Time;

/**
 * Time test
 */
public class TimeTest extends TestCase {

    /**
     * Tests the result of encoding work on the data before and after 2050.
     */
    public void test_Encoding() throws Exception {
        long march2006 = 1143115180000L;
        long march2332 = 11431151800000L;
        byte[] enc = Time.ASN1.encode(new Date(march2006));
        assertEquals("UTCTime", ASN1Constants.TAG_UTCTIME, enc[0]);
        enc = Time.ASN1.encode(new Date(march2332));
        assertEquals("GeneralizedTime", ASN1Constants.TAG_GENERALIZEDTIME, enc[0]);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(TimeTest.class);
    }
}
