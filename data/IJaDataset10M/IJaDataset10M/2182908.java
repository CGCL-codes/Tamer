package net.saml2j.saml20.tests;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import junit.framework.TestCase;
import net.saml2j.saml20.core.assertion.NameIDType;
import net.saml2j.saml20.core.protocol.NameIDPolicyType;
import net.saml2j.saml20.protocol.request.NameIDMappingRequest;
import net.saml2j.saml20.util.JAXBProcessor;
import net.saml2j.saml20.util.SAMLCoreFactory;
import net.saml2j.saml20.util.XMLEncWrapper;
import org.w3c.dom.Document;

public class NameIDMappingRequestTest extends TestCase {

    private SAMLCoreFactory factory = null;

    private JAXBProcessor processor = null;

    private XMLEncWrapper wrapper = null;

    public NameIDMappingRequestTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        factory = SAMLCoreFactory.init();
        processor = new JAXBProcessor();
        wrapper = new XMLEncWrapper();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetRrequestAsOutputStream() throws Exception {
        NameIDType nameid = factory.getNameID();
        nameid.setFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");
        nameid.setValue("C=DK,O=P�lsevognen,CN=Hans Jensen");
        NameIDPolicyType policy = factory.getNameIDPolicy();
        policy.setAllowCreate(true);
        NameIDMappingRequest request = new NameIDMappingRequest(nameid, policy, false);
        OutputStream os = new FileOutputStream("data\\NameIDMappingRequest.xml");
        request.getRrequestAsOutputStream(os, true);
        os.close();
        assertTrue(true);
    }

    public void testGetRequestAsDOMElement() throws Exception {
        InputStream in = new FileInputStream("data\\NameIDMappingRequest.xml");
        NameIDMappingRequest request = new NameIDMappingRequest(in, null, null);
        Document document = request.getRequestAsDOMElement(true);
        in.close();
        assertTrue(true);
    }

    public void testValidate() throws Exception {
        InputStream in = new FileInputStream("data\\NameIDMappingRequest.xml");
        NameIDMappingRequest request = new NameIDMappingRequest(in, null, null);
        request.validate();
        in.close();
        assertTrue(true);
    }
}
