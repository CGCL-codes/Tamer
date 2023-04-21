package es.limit.adigital.signatura.afirma;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Properties;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.SOAPPart;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.soap.MessageFactoryImpl;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.message.WSSecHeader;
import org.apache.ws.security.message.WSSecSignature;
import org.apache.ws.security.message.WSSecUsernameToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Handler per a  incorporar les capçaleres
 * WSS a les peticions a @Firma.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public class AfirmaClientHandler extends BasicHandler {

    private String username;

    private String password;

    private String keystoreLocation;

    private String keystoreType;

    private String keystorePassword;

    private String keystoreCertAlias;

    private String keystoreCertPassword;

    public AfirmaClientHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AfirmaClientHandler(String keystoreLocation, String keystoreType, String keystorePassword, String keystoreCertAlias, String keystoreCertPassword) {
        this.keystoreLocation = keystoreLocation;
        this.keystoreType = keystoreType;
        this.keystorePassword = keystorePassword;
        this.keystoreCertAlias = keystoreCertAlias;
        this.keystoreCertPassword = keystoreCertPassword;
    }

    public void invoke(MessageContext msgContext) throws AxisFault {
        try {
            SOAPMessage msg = msgContext.getCurrentMessage();
            Document doc = ((SOAPEnvelope) msg.getSOAPPart().getEnvelope()).getAsDocument();
            SOAPMessage secMsg;
            if (username != null && username.length() > 0) {
                secMsg = createUserNameToken(doc);
            } else if (keystoreLocation != null && keystoreLocation.length() > 0) {
                secMsg = createBinarySecurityToken(doc);
            } else {
                secMsg = msg;
            }
            ((SOAPPart) msgContext.getRequestMessage().getSOAPPart()).setCurrentMessage(secMsg.getSOAPPart().getEnvelope(), SOAPPart.FORM_SOAPENVELOPE);
        } catch (Exception ex) {
            throw new AxisFault("(Invoke) Error al incorporar les capçaleres WSS al missatge SOAP " + ex.getMessage(), ex);
        }
    }

    private SOAPMessage createUserNameToken(Document soapEnvelopeRequest) throws Exception {
        WSSecHeader wsSecHeader = new WSSecHeader(null, false);
        WSSecUsernameToken wsSecUsernameToken = new WSSecUsernameToken();
        wsSecUsernameToken.setPasswordType("PasswordDigest");
        wsSecUsernameToken.setUserInfo(username, password);
        wsSecHeader.insertSecurityHeader(soapEnvelopeRequest);
        wsSecUsernameToken.prepare(soapEnvelopeRequest);
        wsSecUsernameToken.addCreated();
        wsSecUsernameToken.addNonce();
        Document secSOAPReqDoc = wsSecUsernameToken.build(soapEnvelopeRequest, wsSecHeader);
        Element element = secSOAPReqDoc.getDocumentElement();
        DOMSource source = new DOMSource(element);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult(baos);
        TransformerFactory.newInstance().newTransformer().transform(source, streamResult);
        String secSOAPReq = new String(baos.toByteArray());
        SOAPMessage res = new MessageFactoryImpl().createMessage(null, new ByteArrayInputStream(secSOAPReq.getBytes()));
        return res;
    }

    private SOAPMessage createBinarySecurityToken(Document soapEnvelopeRequest) throws Exception {
        WSSecHeader wsSecHeader = new WSSecHeader(null, false);
        WSSecSignature wsSecSignature = new WSSecSignature();
        Crypto crypto = CryptoFactory.getInstance("org.apache.ws.security.components.crypto.Merlin", initializateCryptoProperties());
        wsSecSignature.setKeyIdentifierType(WSConstants.BST_DIRECT_REFERENCE);
        wsSecSignature.setUserInfo(keystoreCertAlias, keystoreCertPassword);
        wsSecHeader.insertSecurityHeader(soapEnvelopeRequest);
        wsSecSignature.prepare(soapEnvelopeRequest, crypto, wsSecHeader);
        Document secSOAPReqDoc = wsSecSignature.build(soapEnvelopeRequest, crypto, wsSecHeader);
        Element element = secSOAPReqDoc.getDocumentElement();
        DOMSource source = new DOMSource(element);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        StreamResult streamResult = new StreamResult(baos);
        TransformerFactory.newInstance().newTransformer().transform(source, streamResult);
        String secSOAPReq = new String(baos.toByteArray());
        SOAPMessage res = new MessageFactoryImpl().createMessage(null, new ByteArrayInputStream(secSOAPReq.getBytes()));
        return res;
    }

    private Properties initializateCryptoProperties() {
        Properties props = new Properties();
        props.setProperty("org.apache.ws.security.crypto.provider", "org.apache.ws.security.components.crypto.Merlin");
        props.setProperty("org.apache.ws.security.crypto.merlin.keystore.type", keystoreType);
        props.setProperty("org.apache.ws.security.crypto.merlin.keystore.password", keystorePassword);
        props.setProperty("org.apache.ws.security.crypto.merlin.keystore.alias", keystoreCertAlias);
        props.setProperty("org.apache.ws.security.crypto.merlin.alias.password", keystoreCertPassword);
        props.setProperty("org.apache.ws.security.crypto.merlin.file", keystoreLocation);
        return props;
    }

    private static final long serialVersionUID = 1L;
}
