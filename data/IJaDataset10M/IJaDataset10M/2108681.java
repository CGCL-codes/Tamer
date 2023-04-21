package nzdis.agent.soap;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import org.apache.axis.Message;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.utils.XMLUtils;
import org.apache.ws.axis.security.util.AxisUtil;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSSecurityEngine;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.message.WSEncryptBody;
import org.apache.ws.security.message.WSSignEnvelope;
import org.w3c.dom.Document;

/**
 * @author julien
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SoapSecurityProvider {

    private WSSecurityEngine secEngine = null;

    private Crypto client = null;

    private Message message = null;

    public SoapSecurityProvider(SOAPMessage aMsg) {
        setUp(aMsg);
    }

    private void setUp(SOAPMessage aMsg) {
        try {
            secEngine = new WSSecurityEngine();
            client = CryptoFactory.getInstance("client.properties");
            InputStream in = new ByteArrayInputStream(aMsg.getSOAPPart().getEnvelope().toString().getBytes());
            message = new Message(in);
        } catch (SOAPException e) {
            e.printStackTrace();
        }
    }

    public SOAPMessage encryptMsg() {
        SOAPMessage encSOAP = null;
        try {
            SOAPEnvelope unsignedEnvelope = message.getSOAPEnvelope();
            SOAPEnvelope envelope = null;
            WSEncryptBody builder = new WSEncryptBody();
            builder.setUserInfo("SecureServer", "churchillobjects");
            builder.setKeyIdentifierType(WSConstants.X509_KEY_IDENTIFIER);
            Document doc = unsignedEnvelope.getAsDocument();
            Document encryptedDoc = builder.build(doc, client);
            encSOAP = AxisUtil.toSOAPMessage(encryptedDoc);
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (WSSecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encSOAP;
    }

    public SOAPMessage signMsg() {
        SOAPMessage signSOAP = null;
        try {
            SOAPEnvelope unsignedEnvelope = message.getSOAPEnvelope();
            SOAPEnvelope envelope = null;
            WSSignEnvelope builder = new WSSignEnvelope();
            builder.setUserInfo("SecureClient", "churchillobjects");
            Document doc = unsignedEnvelope.getAsDocument();
            Document signedDoc = builder.build(doc, client);
            signSOAP = AxisUtil.toSOAPMessage(signedDoc);
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (WSSecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signSOAP;
    }
}
