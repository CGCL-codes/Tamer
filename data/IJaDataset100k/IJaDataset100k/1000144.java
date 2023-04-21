package org.wijiscommons.ssaf.security.signatures;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.wijiscommons.ssaf.util.dom.DomUtils;

/**
 * This J-Unit test will test digitally signing a document and various
 * failure conditions.  See the comments in the method testSignVerifyDocument
 * for the cases that are tested.
 * 
 * @author yogesh
 *
 */
public class SSAFDigitalSignatureImplTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private final String KEYSTORE_WITH_PRIVATE_KEY = "/home/yogesh/yogesh.key";

    private final String KEYSTORE_WITH_PRIVATE_KEY_PASSWORD = "changeit";

    private final String KEYSTORE_WITH_PRIVATE_KEY_ALIAS = "tomcat";

    private final String TRUSTORE_MATCHING_SIGNERS_CERTIFICATE = "test-data/signatures/trustStores/yogeshPublicKey.keystore";

    private final String TRUSTORE_MATCHING_SIGNERS_CERTIFICATE_PASSWORD = "changeit";

    private final String TRUSTORE_MATCHING_SIGNERS_CERTIFICATE_ALIAS = "yogeshPublicKey";

    private final String TRUSTORE_WITH_WIJISROOT_CERTIFICATE = "test-data/signatures/trustStores/wijisRoot.keystore";

    private final String TRUSTORE_WITH_WIJISROOT_CERTIFICATE_PASSWORD = "changeit";

    private final String TRUSTORE_WITH_WIJISROOT_CERTIFICATE_ALIAS = "wijisRoot";

    public void testSignVerifyDocument() {
        Document doc = null;
        boolean verified = false;
        try {
            SSAFDigitalSignatureImpl ssafDigitalSignature = new SSAFDigitalSignatureImpl(KEYSTORE_WITH_PRIVATE_KEY, KEYSTORE_WITH_PRIVATE_KEY_PASSWORD, KEYSTORE_WITH_PRIVATE_KEY_ALIAS);
            doc = DomUtils.getDocumentFromFile(new File("test-data/signatures/beforeSigning.xml"));
            doc = ssafDigitalSignature.signDocument(doc);
            OutputStream os = new FileOutputStream("test-data/signatures/afterSigning.xml");
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            trans.transform(new DOMSource(doc), new StreamResult(os));
        } catch (Exception e) {
            fail(e.getMessage());
        }
        SSAFVerifyDigitalSignatureImpl ssafVerifyDigitalSignature = null;
        try {
            ssafVerifyDigitalSignature = new SSAFVerifyDigitalSignatureImpl(TRUSTORE_MATCHING_SIGNERS_CERTIFICATE, TRUSTORE_MATCHING_SIGNERS_CERTIFICATE_PASSWORD, TRUSTORE_MATCHING_SIGNERS_CERTIFICATE_ALIAS);
            verified = false;
            verified = ssafVerifyDigitalSignature.verifyDigitalSignature(doc);
            assertEquals(true, verified);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        ssafVerifyDigitalSignature = null;
        try {
            ssafVerifyDigitalSignature = new SSAFVerifyDigitalSignatureImpl("/usr/local/jdk1.5.0_06/jre/lib/security/cacerts", "changeit", "thawtepersonalbasicca");
            verified = false;
            verified = ssafVerifyDigitalSignature.verifyDigitalSignature(doc);
            assertEquals(true, verified);
        } catch (Exception e) {
            assertEquals("the keyselector did not find a validation key", e.getMessage());
        }
        ssafVerifyDigitalSignature = null;
        try {
            ssafVerifyDigitalSignature = new SSAFVerifyDigitalSignatureImpl(TRUSTORE_WITH_WIJISROOT_CERTIFICATE, TRUSTORE_WITH_WIJISROOT_CERTIFICATE_PASSWORD, TRUSTORE_WITH_WIJISROOT_CERTIFICATE_ALIAS);
            verified = false;
            verified = ssafVerifyDigitalSignature.verifyDigitalSignature(doc);
        } catch (Exception e) {
            assertEquals("the keyselector did not find a validation key", e.getMessage());
        }
        try {
            ssafVerifyDigitalSignature = new SSAFVerifyDigitalSignatureImpl("/home/yogesh/dev/ssaf/signatures/keystores/WijisServices.key", "REDACTED", "wijisservices");
            verified = false;
            verified = ssafVerifyDigitalSignature.verifyDigitalSignature(doc);
        } catch (Exception e) {
            assertEquals("requested entry requires a password", e.getMessage());
        }
        ssafVerifyDigitalSignature = null;
        try {
            ssafVerifyDigitalSignature = new SSAFVerifyDigitalSignatureImpl(TRUSTORE_MATCHING_SIGNERS_CERTIFICATE, TRUSTORE_MATCHING_SIGNERS_CERTIFICATE_PASSWORD, TRUSTORE_MATCHING_SIGNERS_CERTIFICATE_ALIAS);
            Element element = (Element) doc.getElementsByTagName("GetPerson2").item(0);
            element.getParentNode().removeChild(element);
            verified = false;
            verified = ssafVerifyDigitalSignature.verifyDigitalSignature(doc);
            assertFalse(verified);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            Document wrongSignatureDocument = dbf.newDocumentBuilder().parse(new FileInputStream("test-data/signatures/wrongSignature.xml"));
            ssafVerifyDigitalSignature = new SSAFVerifyDigitalSignatureImpl(TRUSTORE_MATCHING_SIGNERS_CERTIFICATE, TRUSTORE_MATCHING_SIGNERS_CERTIFICATE_PASSWORD, TRUSTORE_MATCHING_SIGNERS_CERTIFICATE_ALIAS);
            verified = false;
            verified = ssafVerifyDigitalSignature.verifyDigitalSignature(wrongSignatureDocument);
            assertEquals(false, verified);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
