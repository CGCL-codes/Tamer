package test.integ.be.fedict.trust;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import java.io.File;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import javax.servlet.ServletContext;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.utils.Constants;
import org.apache.xpath.XPathAPI;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import test.integ.be.fedict.trust.util.TestSOAPMessageContext;
import test.integ.be.fedict.trust.util.TestUtils;
import be.fedict.trust.client.WSSecurityClientHandler;
import be.fedict.trust.service.TrustService;
import be.fedict.trust.service.entity.KeyStoreType;
import be.fedict.trust.service.entity.WSSecurityConfigEntity;
import be.fedict.trust.xkms2.WSSecurityServerHandler;

/**
 * eID Trust Service WS-Security SOAP handler test.
 * 
 * @author wvdhaute
 * 
 */
public class WSSecurityTest {

    private static final Log LOG = LogFactory.getLog(WSSecurityTest.class);

    private WSSecurityServerHandler wsSecurityServerHandler;

    private WSSecurityClientHandler wsSecurityClientHandler;

    private TrustService mockTrustService;

    private ServletContext mockServletContext;

    private Object[] mockObjects;

    @Before
    public void setUp() throws Exception {
        this.wsSecurityServerHandler = new WSSecurityServerHandler();
        this.wsSecurityClientHandler = new WSSecurityClientHandler();
        this.wsSecurityServerHandler.postConstructCallback();
        this.mockTrustService = createMock(TrustService.class);
        this.mockServletContext = createMock(ServletContext.class);
        this.mockObjects = new Object[] { this.mockTrustService, this.mockServletContext };
    }

    @Test
    public void testWSSecurity() throws Exception {
        KeyPair keyPair = TestUtils.generateKeyPair();
        X509Certificate certificate = TestUtils.generateSelfSignedCertificate(keyPair, "CN=Test");
        KeyPair fooKeyPair = TestUtils.generateKeyPair();
        X509Certificate fooCertificate = TestUtils.generateSelfSignedCertificate(fooKeyPair, "CN=F00");
        this.wsSecurityClientHandler.setServerCertificate(certificate);
        KeyStoreType keyStoreType = KeyStoreType.PKCS12;
        String keyStorePassword = "secret";
        String keyEntryPassword = "secret";
        String alias = "alias";
        File tmpP12File = File.createTempFile("keystore-", ".p12");
        tmpP12File.deleteOnExit();
        TestUtils.persistInKeyStore(tmpP12File, "pkcs12", keyPair.getPrivate(), certificate, keyStorePassword, keyEntryPassword, alias);
        String keyStorePath = tmpP12File.getAbsolutePath();
        MessageFactory messageFactory = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL);
        InputStream testSoapMessageInputStream = WSSecurityTest.class.getResourceAsStream("/test-soap-message.xml");
        assertNotNull(testSoapMessageInputStream);
        SOAPMessage message = messageFactory.createMessage(null, testSoapMessageInputStream);
        SOAPMessageContext soapMessageContext = new TestSOAPMessageContext(message, true);
        soapMessageContext.put(MessageContext.SERVLET_CONTEXT, this.mockServletContext);
        expect(this.mockServletContext.getAttribute(TrustService.class.getName())).andReturn(mockTrustService);
        expect(this.mockTrustService.getWsSecurityConfig()).andReturn(new WSSecurityConfigEntity("test", true, keyStoreType, keyStorePath, keyStorePassword, keyEntryPassword, alias));
        replay(this.mockObjects);
        assertTrue(this.wsSecurityServerHandler.handleMessage(soapMessageContext));
        verify(this.mockObjects);
        SOAPMessage resultMessage = soapMessageContext.getMessage();
        SOAPPart resultSoapPart = resultMessage.getSOAPPart();
        LOG.debug("signed SOAP part:" + TestUtils.domToString(resultSoapPart));
        Element nsElement = resultSoapPart.createElement("nsElement");
        nsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
        nsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:wsse", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd");
        nsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:ds", "http://www.w3.org/2000/09/xmldsig#");
        nsElement.setAttributeNS(Constants.NamespaceSpecNS, "xmlns:wsu", "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
        Node resultNode = XPathAPI.selectSingleNode(resultSoapPart, "/soap:Envelope/soap:Header/wsse:Security[@soap:mustUnderstand = '1']", nsElement);
        assertNotNull(resultNode);
        assertNotNull("missing WS-Security timestamp", XPathAPI.selectSingleNode(resultSoapPart, "/soap:Envelope/soap:Header/wsse:Security/wsu:Timestamp/wsu:Created", nsElement));
        assertEquals(2.0, XPathAPI.eval(resultSoapPart, "count(//ds:Reference)", nsElement).num());
        soapMessageContext.put(MessageContext.MESSAGE_OUTBOUND_PROPERTY, false);
        assertTrue(this.wsSecurityClientHandler.handleMessage(soapMessageContext));
        this.wsSecurityClientHandler.setServerCertificate(fooCertificate);
        try {
            this.wsSecurityClientHandler.handleMessage(soapMessageContext);
            fail();
        } catch (SOAPFaultException e) {
            LOG.debug("SOAPFaultException: " + e.getMessage());
        }
    }
}
