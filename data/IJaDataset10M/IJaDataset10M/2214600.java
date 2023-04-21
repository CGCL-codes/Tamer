package org.infoeng.ofbiz.ltans.test.ltap;

import junit.framework.TestCase;
import org.infoeng.ofbiz.ltans.ltap.Request;
import org.infoeng.ofbiz.ltans.ltap.Data;
import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.security.*;
import java.util.*;
import java.security.cert.*;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.infoeng.ofbiz.ltans.ltap.*;
import org.infoeng.ofbiz.ltans.util.*;
import java.math.BigInteger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import org.apache.commons.codec.binary.Base64;

/**
 *   <pre>
 *   Request ::= SEQUENCE {
 *       requestInformation RequestInformation,
 *       data               Data 
 *   }
 *   </pre>
 */
public class RequestTest extends TestCase {

    private String rfcStr = "rfc3280.txt";

    private String policyOID = "1.3.6.1.5.5.11.1.3.0.234";

    private String metaItemNS = "http://www.setcce.org/schemas/ltap";

    private String uriValue = "http://www.ietf.org/rfc/rfc3280.txt";

    private MessageDigest md;

    private Random rnd;

    private X509Name resourceName = new X509Name("CN=test resource,OU=Documents,DC=net,DC=sourceforge,DC=infoeng");

    public RequestTest() {
        super();
        rnd = new Random();
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
        }
    }

    public void testRequest() throws Exception {
        InputStream rfcIS = Thread.currentThread().getContextClassLoader().getResourceAsStream(rfcStr);
        Properties props = LtansUtils.getDefaultProperties();
        KeyStore ks = LtansUtils.getDefaultKeyStore();
        X509Certificate requestorCert = (X509Certificate) ks.getCertificate(props.getProperty("ltans.keystore.requestorAlias"));
        X509Certificate serviceCert = (X509Certificate) ks.getCertificate(props.getProperty("ltans.keystore.afmServiceKeyAlias"));
        Request req = new Request(rfcIS, new GeneralNames(new GeneralName(resourceName)), new MetaData(new MetaItem(new DERIA5String("http://metadata.dod.mil/mdr/ns/DDMS/1.3/identifier"), new DERUTF8String(uriValue))), requestorCert, serviceCert);
        byte[] objBytes = req.getEncoded();
        System.out.println("Wrote " + objBytes.length + " bytes.");
        Request newReq = Request.getInstance(objBytes);
    }
}
