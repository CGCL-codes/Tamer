package org.tolven.client.examples.ws.common;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author Joseph Isaac
 */
public class HeaderHandler implements SOAPHandler<SOAPMessageContext> {

    private static String WS_SECEXT = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

    private static String WS_ADDRESSING = "http://www.w3.org/2005/08/addressing";

    private static String WS_SECUTILITY = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd";

    private static String WS_USER_TOKEN_PROFILE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText";

    private String username;

    private char[] password;

    private int expiresInSeconds;

    public HeaderHandler(String username, char[] password, int expiresInSeconds) {
        setUsername(username);
        setPassword(password);
        setExpiresInSeconds(expiresInSeconds);
    }

    private String getUsername() {
        return username;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private char[] getPassword() {
        return password;
    }

    private void setPassword(char[] password) {
        this.password = password;
    }

    private int getExpiresInSeconds() {
        return expiresInSeconds;
    }

    private void setExpiresInSeconds(int expiresInSeconds) {
        this.expiresInSeconds = expiresInSeconds;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (outboundProperty.booleanValue()) {
            try {
                SOAPEnvelope envelope = smc.getMessage().getSOAPPart().getEnvelope();
                SOAPHeader header = null;
                if (envelope.getHeader() == null) {
                    header = envelope.addHeader();
                } else {
                    header = envelope.getHeader();
                }
                SOAPElement messageID = header.addChildElement("MessageID", "wsa", WS_ADDRESSING);
                messageID.addTextNode(UUID.randomUUID().toString());
                SOAPElement security = header.addChildElement("Security", "wsse", WS_SECEXT);
                security.addAttribute(new QName("wsse:mustUnderstand"), "1");
                SOAPElement timestamp = security.addChildElement("Timestamp", "wsu", WS_SECUTILITY);
                DatatypeFactory factory = DatatypeFactory.newInstance();
                SOAPElement createdElement = timestamp.addChildElement("Created", "wsu");
                GregorianCalendar gCal = new GregorianCalendar();
                String xmlNow = factory.newXMLGregorianCalendar(gCal).toXMLFormat();
                createdElement.addTextNode(xmlNow);
                SOAPElement expiresElement = timestamp.addChildElement("Expires", "wsu");
                gCal.add(GregorianCalendar.SECOND, getExpiresInSeconds());
                String xmlExpires = factory.newXMLGregorianCalendar(gCal).toXMLFormat();
                expiresElement.addTextNode(xmlExpires);
                SOAPElement usernameToken = security.addChildElement("UsernameToken", "wsse");
                SOAPElement username = usernameToken.addChildElement("Username", "wsse");
                username.addTextNode(getUsername());
                SOAPElement password = usernameToken.addChildElement("Password", "wsse");
                password.setAttribute("Type", WS_USER_TOKEN_PROFILE);
                password.addTextNode(new String(getPassword()));
                SOAPElement createdUser = usernameToken.addChildElement("Created", "wsu", WS_SECUTILITY);
                createdUser.addTextNode(xmlNow);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return outboundProperty;
    }

    public Set<QName> getHeaders() {
        QName securityHeader = new QName(WS_SECEXT, "Security", "wsse");
        HashSet<QName> headers = new HashSet<QName>();
        headers.add(securityHeader);
        return headers;
    }

    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    public void close(MessageContext context) {
        return;
    }
}
