package org.apache.harmony.security.tests.x509.tsp;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import junit.framework.TestCase;
import org.apache.harmony.security.x501.Name;
import org.apache.harmony.security.x509.AlgorithmIdentifier;
import org.apache.harmony.security.x509.Extension;
import org.apache.harmony.security.x509.Extensions;
import org.apache.harmony.security.x509.GeneralName;
import org.apache.harmony.security.x509.tsp.MessageImprint;
import org.apache.harmony.security.x509.tsp.TSTInfo;

public class TSTInfoTest extends TestCase {

    /**
     * @throws IOException 
     * @tests 'org.apache.harmony.security.x509.tsp.TSTInfo.getEncoded()'
     */
    public void testGetEncoded() throws IOException {
        String policy = "1.2.3.4.5";
        String sha1 = "1.3.14.3.2.26";
        MessageImprint msgImprint = new MessageImprint(new AlgorithmIdentifier(sha1), new byte[20]);
        Date genTime = new Date();
        BigInteger nonce = BigInteger.valueOf(1234567890L);
        int[] accuracy = new int[] { 1, 0, 0 };
        GeneralName tsa = new GeneralName(new Name("CN=AnAuthority"));
        Extensions exts = new Extensions();
        int[] timeStampingExtOID = new int[] { 1, 3, 6, 1, 5, 5, 7, 3, 8 };
        byte[] timeStampingExtValue = new byte[] { (byte) 1, (byte) 2, (byte) 3 };
        Extension ext = new Extension(timeStampingExtOID, true, timeStampingExtValue);
        exts.addExtension(ext);
        TSTInfo info = new TSTInfo(1, policy, msgImprint, BigInteger.TEN, genTime, accuracy, Boolean.FALSE, nonce, tsa, exts);
        byte[] encoding = TSTInfo.ASN1.encode(info);
        TSTInfo decoded = (TSTInfo) TSTInfo.ASN1.decode(encoding);
        assertEquals("Decoded version is incorrect", info.getVersion(), decoded.getVersion());
        assertEquals("Decoded policy is incorrect", policy, decoded.getPolicy());
        assertTrue("Decoded messageImprint is incorrect", Arrays.equals(MessageImprint.ASN1.encode(msgImprint), MessageImprint.ASN1.encode(decoded.getMessageImprint())));
        assertEquals("Decoded serialNumber is incorrect", BigInteger.TEN, decoded.getSerialNumber());
        assertEquals("Decoded genTime is incorrect", genTime, decoded.getGenTime());
        assertTrue("Decoded accuracy is incorrect", Arrays.equals(accuracy, decoded.getAccuracy()));
        assertFalse("Decoded ordering is incorrect", decoded.getOrdering().booleanValue());
        assertEquals("Decoded nonce is incorrect", nonce, decoded.getNonce());
        assertEquals("Decoded tsa is incorrect", tsa, decoded.getTsa());
        assertEquals("Decoded extensions is incorrect", exts, decoded.getExtensions());
    }
}
